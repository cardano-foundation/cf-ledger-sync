package org.cardanofoundation.ledgersync.account.processor;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.address.AddressProvider;
import com.bloxbean.cardano.yaci.core.util.HexUtil;
import com.bloxbean.cardano.yaci.store.events.GenesisBlockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.account.model.AddressTxAmountEntity;
import org.cardanofoundation.ledgersync.account.repository.AddressTxAmountRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.bloxbean.cardano.yaci.core.util.Constants.LOVELACE;
import static org.cardanofoundation.ledgersync.account.util.AddressUtil.getAddress;

@Component
@RequiredArgsConstructor
@Slf4j
public class GensisBlockAddressTxAmtProcessor {
    private final AddressTxAmountRepository addressTxAmountRepository;

    @EventListener
    @Transactional
    public void handleAddressTxAmtForGenesisBlock(GenesisBlockEvent genesisBlockEvent) {
        var genesisBalances = genesisBlockEvent.getGenesisBalances();
        if (genesisBalances == null || genesisBalances.isEmpty()) {
            log.info("No genesis balances found");
            return;
        }

        List<AddressTxAmountEntity> genesisAddressTxAmtList = new ArrayList<>();
        for (var genesisBalance : genesisBalances) {
            var receiverAddress = genesisBalance.getAddress();
            var txnHash = genesisBalance.getTxnHash();
            var balance = genesisBalance.getBalance();

            if (balance == null || balance.compareTo(BigInteger.ZERO) == 0) {
                continue;
            }

            //address and full address if the address is too long
            var addressTuple = getAddress(receiverAddress);

            String ownerPaymentCredential = null;
            String stakeAddress = null;
            if (genesisBalance.getAddress() != null &&
                    genesisBalance.getAddress().startsWith("addr")) { //If shelley address
                try {
                    Address address = new Address(genesisBalance.getAddress());
                    ownerPaymentCredential = address.getPaymentCredential().map(paymentKeyHash -> HexUtil.encodeHexString(paymentKeyHash.getBytes()))
                            .orElse(null);
                    stakeAddress = address.getDelegationCredential().map(delegationHash -> AddressProvider.getStakeAddress(address).toBech32())
                            .orElse(null);
                } catch (Exception e) {
                    log.warn("Error getting address details: " + genesisBalance.getAddress());
                }
            }

            var addressTxAmountEntity = AddressTxAmountEntity.builder()
                    .address(addressTuple._1)
                    .unit(LOVELACE)
                    .txHash(txnHash)
                    .slot(genesisBlockEvent.getSlot())
                    .quantity(balance)
                    .addrFull(addressTuple._2)
                    .stakeAddress(stakeAddress)
                    .assetName(LOVELACE)
                    .policy(null)
                    .paymentCredential(ownerPaymentCredential)
                    .epoch(0)
                    .blockNumber(genesisBlockEvent.getBlock())
                    .blockHash(genesisBlockEvent.getBlockHash())
                    .blockTime(genesisBlockEvent.getBlockTime())
                    .build();

            genesisAddressTxAmtList.add(addressTxAmountEntity);
        }

        if (genesisAddressTxAmtList.size() > 0) {
            addressTxAmountRepository.saveAll(genesisAddressTxAmtList);
        }
    }

}
