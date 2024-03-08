package org.cardanofoundation.ledgersync.account.processor;

import com.bloxbean.cardano.yaci.store.client.utxo.UtxoClient;
import com.bloxbean.cardano.yaci.store.common.domain.AddressUtxo;
import com.bloxbean.cardano.yaci.store.common.domain.Amt;
import com.bloxbean.cardano.yaci.store.common.domain.UtxoKey;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import com.bloxbean.cardano.yaci.store.events.RollbackEvent;
import com.bloxbean.cardano.yaci.store.events.internal.ReadyForBalanceAggregationEvent;
import com.bloxbean.cardano.yaci.store.utxo.domain.AddressUtxoEvent;
import com.bloxbean.cardano.yaci.store.utxo.domain.TxInputOutput;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.account.domain.AddressTxAmount;
import org.cardanofoundation.ledgersync.account.storage.AddressTxAmountStorage;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigInteger;
import java.util.*;

import static org.cardanofoundation.ledgersync.account.util.AddressUtil.getAddress;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressTxAmountProcessor {
    private final AddressTxAmountStorage addressTxAmountStorage;
    private final UtxoClient utxoClient;

    private List<Pair<EventMetadata, TxInputOutput>> txInputOutputListCache = Collections.synchronizedList(new ArrayList<>());
    private List<AddressTxAmount> addressTxAmountListCache = Collections.synchronizedList(new ArrayList<>());

    private Map<Integer, List<AddressTxAmount>> addressTxAmountListCacheMap = new HashMap<>();
    private int bucketSize = 10;

    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        initAddressTxAmtCache();
    }

    private void initAddressTxAmtCache() {
        for (int i = 0; i < bucketSize; i++) {
            addressTxAmountListCacheMap.put(i, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    private void save(long blockNo, List<AddressTxAmount> addressTxAmounts) {
        int index = (int) (blockNo % bucketSize);
        List<AddressTxAmount> addressTxAmountList = addressTxAmountListCacheMap.get(index);
        addressTxAmountList.addAll(addressTxAmounts);

        if (addressTxAmountList.size() > 1000) {
            synchronized (addressTxAmountList) {
                addressTxAmountStorage.save(addressTxAmountList);
                if (log.isDebugEnabled())
                    log.debug("-- Saved address_tx_amounts records : {}", addressTxAmountList.size());
                addressTxAmountList.clear();
            }
        }
    }

    private void clearCache() {
        for (int i = 0; i < bucketSize; i++) {
            List<AddressTxAmount> addressTxAmountList = addressTxAmountListCacheMap.get(i);
            if (addressTxAmountList.size() > 0) {
                //addressTxAmountStorage.save(addressTxAmountList);
                addressTxAmountList.clear();
            }
        }
    }

    @EventListener
    @Transactional
    public void processAddressUtxoEvent(AddressUtxoEvent addressUtxoEvent) {
        //Ignore Genesis Txs as it's handled by GEnesisBlockAddressTxAmtProcessor
        if (addressUtxoEvent.getEventMetadata().getSlot() == -1)
            return;

        var txInputOutputList = addressUtxoEvent.getTxInputOutputs();
        if (txInputOutputList == null || txInputOutputList.isEmpty())
            return;

        List<AddressTxAmount> addressTxAmountList = new ArrayList<>();

        for (var txInputOutput : txInputOutputList) {
            var txAddressTxAmountEntities = processAddressAmountForTx(addressUtxoEvent.getEventMetadata(), txInputOutput, false);
            if (txAddressTxAmountEntities == null || txAddressTxAmountEntities.isEmpty()) continue;

            addressTxAmountList.addAll(txAddressTxAmountEntities);
        }

//        if (addressTxAmountList.size() > 100) {
//            addressTxAmountStorage.save(addressTxAmountList); //Save
//            return;
//        }

        if (addressTxAmountList.size() > 0) {
           //TODO -- addressTxAmountListCache.addAll(addressTxAmountList);
            save(addressUtxoEvent.getEventMetadata().getBlock(), addressTxAmountList);
        }
    }

    private List<AddressTxAmount> processAddressAmountForTx(EventMetadata metadata, TxInputOutput txInputOutput,
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

            return Collections.emptyList();
        }

        var txAddressTxAmount =
                processTxAmount(txHash, metadata, inputAddressUtxos, outputs);
        return txAddressTxAmount;
    }

    private List<AddressTxAmount> processTxAmount(String txHash, EventMetadata metadata, List<AddressUtxo> inputs, List<AddressUtxo> outputs) {
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

        return (List<AddressTxAmount>) addressTxAmountMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().compareTo(BigInteger.ZERO) != 0)
                .map(entry -> {
                    var addressDetails = addressToAddressDetailsMap.get(entry.getKey().getFirst());
                    var assetDetails = unitToAssetDetailsMap.get(entry.getKey().getSecond());

                    //address and full address if the address is too long
                    var addressTuple = getAddress(entry.getKey().getFirst());

                    return AddressTxAmount.builder()
                            .address(addressTuple._1)
                            .unit(entry.getKey().getSecond())
                            .txHash(txHash)
                            .slot(metadata.getSlot())
                            .quantity(entry.getValue())
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

    @EventListener
    @Transactional //We can also listen to CommitEvent here
    public void handleRemainingTxInputOuputs(ReadyForBalanceAggregationEvent readyForBalanceAggregationEvent) {
        try {
            List<AddressTxAmount> addressTxAmountList = new ArrayList<>();
            for (var pair : txInputOutputListCache) {
                EventMetadata metadata = pair.getFirst();
                TxInputOutput txInputOutput = pair.getSecond();

                var addrAmountEntitiesForTx = processAddressAmountForTx(metadata, txInputOutput, true);

                if (addrAmountEntitiesForTx != null) {
                    addressTxAmountList.addAll(addrAmountEntitiesForTx);
                }
            }

            if (addressTxAmountList.size() > 0) {
                addressTxAmountListCache.addAll(addressTxAmountList);
            }

            var remainingAddressAmtList = addressTxAmountListCacheMap.values().stream()
                    .flatMap(List::stream)
                    .toList();
            if (remainingAddressAmtList.size() > 0) {
                addressTxAmountListCache.addAll(remainingAddressAmtList);
            }

            long t1 = System.currentTimeMillis();
            if (addressTxAmountListCache.size() > 0) {
                addressTxAmountStorage.save(addressTxAmountListCache);
            }

            long t2 = System.currentTimeMillis();
            log.info("Time taken to save address_tx_amounts records : {}, time: {} ms", addressTxAmountListCache.size(),  (t2 - t1));

        } finally {
            txInputOutputListCache.clear();
            addressTxAmountListCache.clear();
            clearCache();
        }
    }

    @EventListener
    @Transactional
    public void handleRollback(RollbackEvent rollbackEvent) {
        int addressTxAmountDeleted = addressTxAmountStorage.deleteAddressBalanceBySlotGreaterThan(rollbackEvent.getRollbackTo().getSlot());

        log.info("Rollback -- {} address_tx_amounts records", addressTxAmountDeleted);
    }

    record AssetDetails(String policy, String assetName) {
    }

    record AddressDetails(String ownerStakeAddress, String ownerPaymentCredential, String ownerStakeCredential) {
    }
}
