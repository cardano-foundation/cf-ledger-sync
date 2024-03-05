package org.cardanofoundation.ledgersync.account.processor;

import com.bloxbean.cardano.yaci.store.client.utxo.UtxoClient;
import com.bloxbean.cardano.yaci.store.common.domain.AddressUtxo;
import com.bloxbean.cardano.yaci.store.common.domain.Amt;
import com.bloxbean.cardano.yaci.store.common.domain.UtxoKey;
import com.bloxbean.cardano.yaci.store.common.util.Tuple;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import com.bloxbean.cardano.yaci.store.events.RollbackEvent;
import com.bloxbean.cardano.yaci.store.events.internal.ReadyForBalanceAggregationEvent;
import com.bloxbean.cardano.yaci.store.utxo.domain.AddressUtxoEvent;
import com.bloxbean.cardano.yaci.store.utxo.domain.TxInputOutput;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.account.model.AddressTxAmountEntity;
import org.cardanofoundation.ledgersync.account.repository.AddressTxAmountRepository;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigInteger;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressTxAmountProcessor {
    private static final int MAX_ADDR_SIZE = 500; //Required for Byron Addresses

    private final AddressTxAmountRepository addressTxAmountRepository;
    private final UtxoClient utxoClient;

    private List<Pair<EventMetadata, TxInputOutput>> txInputOutputListCache = Collections.synchronizedList(new ArrayList<>());

    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @EventListener
    @Transactional
    public void processAddressUtxoEvent(AddressUtxoEvent addressUtxoEvent) {
        //Ignore Genesis Txs for now -- TODO
        if (addressUtxoEvent.getEventMetadata().getSlot() == -1)
            return;

        var txInputOutputList = addressUtxoEvent.getTxInputOutputs();
        if (txInputOutputList == null || txInputOutputList.isEmpty())
            return;

        List<AddressTxAmountEntity> addressTxAmountEntities = new ArrayList<>();

        for (var txInputOutput : txInputOutputList) {
            var txAddressTxAmountEntities = processAddressAmountForTx(addressUtxoEvent.getEventMetadata(), txInputOutput, false);
            if (txAddressTxAmountEntities == null) continue;

            addressTxAmountEntities.addAll(txAddressTxAmountEntities);
        }

        if (addressTxAmountEntities.size() > 0) {
            addressTxAmountRepository.saveAll(addressTxAmountEntities);
        }
    }

    private List<AddressTxAmountEntity> processAddressAmountForTx(EventMetadata metadata, TxInputOutput txInputOutput,
                                                                  boolean throwExceptionOnFailure) {
        var txHash = txInputOutput.getTxHash();
        var inputs = txInputOutput.getInputs();
        var outputs = txInputOutput.getOutputs();
        if (inputs == null || inputs.isEmpty() || outputs == null || outputs.isEmpty())
            return null;

        var inputUtxoKeys = inputs.stream()
                .map(input -> new UtxoKey(input.getTxHash(), input.getOutputIndex()))
                .toList();

        var inputAddressUtxos = utxoClient.getUtxosByIds(inputUtxoKeys)
                .stream()
                .filter(Objects::nonNull)
                .toList();

        if (inputAddressUtxos.size() != inputUtxoKeys.size()) {
            log.debug("Unable to get inputs for all input keys for account balance calculation. Add this Tx to cache to process later : " + txHash);
            if (throwExceptionOnFailure)
                throw new IllegalStateException("Unable to get inputs for all input keys for account balance calculation : " + inputUtxoKeys);
            else
                txInputOutputListCache.add(Pair.of(metadata, txInputOutput));
        }

        var txAddressTxAmountEntities =
                processTxAmount(txHash, metadata, inputAddressUtxos, outputs);
        return txAddressTxAmountEntities;
    }

    private List<AddressTxAmountEntity> processTxAmount(String txHash, EventMetadata metadata, List<AddressUtxo> inputs, List<AddressUtxo> outputs) {
        Map<Pair<String, String>, BigInteger> addressTxAmountMap = new HashMap<>();
        Map<String, AddressDetails> addressToAddressDetailsMap = new HashMap<>();
        Map<String, AssetDetails> unitToAssetDetailsMap = new HashMap<>();

        //Subtract input amounts
        for (var input : inputs) {
            for (Amt amt : input.getAmounts()) {
                var key = Pair.of(input.getOwnerAddr(), amt.getUnit());
                var amount = addressTxAmountMap.getOrDefault(key, BigInteger.ZERO);
                amount = amount.subtract(amt.getQuantity());
                addressTxAmountMap.put(key, amount);

                var addressDetails = new AddressDetails(input.getOwnerStakeAddr(), input.getOwnerPaymentCredential(), input.getOwnerStakeCredential());
                addressToAddressDetailsMap.put(input.getOwnerAddr(), addressDetails);

                var assetDetails = new AssetDetails(amt.getPolicyId(), amt.getAssetName());
                unitToAssetDetailsMap.put(amt.getUnit(), assetDetails);
            }
        }

        //Add output amounts
        for (var output : outputs) {
            for (Amt amt : output.getAmounts()) {
                var key = Pair.of(output.getOwnerAddr(), amt.getUnit());
                var amount = addressTxAmountMap.getOrDefault(key, BigInteger.ZERO);
                amount = amount.add(amt.getQuantity());
                addressTxAmountMap.put(key, amount);

                var addressDetails = new AddressDetails(output.getOwnerStakeAddr(), output.getOwnerPaymentCredential(), output.getOwnerStakeCredential());
                addressToAddressDetailsMap.put(output.getOwnerAddr(), addressDetails);

                var assetDetails = new AssetDetails(amt.getPolicyId(), amt.getAssetName());
                unitToAssetDetailsMap.put(amt.getUnit(), assetDetails);
            }
        }

        return (List<AddressTxAmountEntity>) addressTxAmountMap.entrySet()
                .stream()
                .map(entry -> {
                    var addressDetails = addressToAddressDetailsMap.get(entry.getKey().getFirst());
                    var assetDetails = unitToAssetDetailsMap.get(entry.getKey().getSecond());

                    //address and full address if the address is too long
                    var addressTuple = getAddress(entry.getKey().getFirst());

                    return AddressTxAmountEntity.builder()
                            .address(addressTuple._1)
                            .unit(entry.getKey().getSecond())
                            .txHash(txHash)
                            .slot(metadata.getSlot())
                            .quantity(entry.getValue())
                            .addrFull(addressTuple._2)
                            .stakeAddress(addressDetails.ownerStakeAddress)
                            .assetName(assetDetails.assetName)
                            .policy(assetDetails.policy)
                            .paymentCredential(addressDetails.ownerPaymentCredential)
                            .epoch(metadata.getEpochNumber())
                            .blockNumber(metadata.getBlock())
                            .blockHash(metadata.getBlockHash())
                            .blockTime(metadata.getBlockTime())
                            .build();
                }).toList();
    }

    //Return the address and full address if the address is too long
    //Using Tuple as Pair doesn't allow null values
    private Tuple<String, String> getAddress(String address) {
        if (address != null && address.length() > MAX_ADDR_SIZE) {
            String addr = address.substring(0, MAX_ADDR_SIZE);
            String fullAddr = address;
            return new Tuple<>(addr, fullAddr);
        } else {
            return new Tuple<>(address, null);
        }
    }

    @EventListener
    @Transactional //We can also listen to CommitEvent here
    public void handleRemainingTxInputOuputs(ReadyForBalanceAggregationEvent readyForBalanceAggregationEvent) {
        try {
            List<AddressTxAmountEntity> addressTxAmountEntities = new ArrayList<>();
            for (var pair : txInputOutputListCache) {
                EventMetadata metadata = pair.getFirst();
                TxInputOutput txInputOutput = pair.getSecond();

                var addrAmountEntitiesForTx = processAddressAmountForTx(metadata, txInputOutput, true);

                if (addrAmountEntitiesForTx != null) {
                    addressTxAmountEntities.addAll(addrAmountEntitiesForTx);
                }
            }

            if (addressTxAmountEntities.size() > 0) {
                addressTxAmountRepository.saveAll(addressTxAmountEntities);
            }
        } finally {
            txInputOutputListCache.clear();
        }
    }

    @EventListener
    @Transactional
    public void handleRollback(RollbackEvent rollbackEvent) {
        int addressTxAmountDeleted = addressTxAmountRepository.deleteAddressBalanceBySlotGreaterThan(rollbackEvent.getRollbackTo().getSlot());

        log.info("Rollback -- {} address_tx_amounts records", addressTxAmountDeleted);
    }

    record AssetDetails(String policy, String assetName) {
    }

    record AddressDetails(String ownerStakeAddress, String ownerPaymentCredential, String ownerStakeCredential) {
    }
}
