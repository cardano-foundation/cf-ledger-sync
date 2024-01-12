package org.cardanofoundation.ledgersync.aggregate.account.service.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.account.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockDataService;
import org.cardanofoundation.ledgersync.aggregate.account.service.TransactionService;
import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddress;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    AddressBalanceService addressBalanceService;
    BlockDataService blockDataService;

    @Override
    public void prepareAndHandleTxs(Collection<AggregatedBlock> aggregatedBlocks) {
        List<AggregatedTx> aggregatedTxList = aggregatedBlocks.stream()
                .map(AggregatedBlock::getTxList)
                .flatMap(Collection::stream)
                .toList();
        Collection<AggregatedTx> successTxs = new ConcurrentLinkedQueue<>();
        Collection<AggregatedTx> failedTxs = new ConcurrentLinkedQueue<>();

        if (CollectionUtils.isEmpty(aggregatedTxList)) {
            return;
        }

        aggregatedTxList.stream().forEach(aggregatedTx -> {
            if (aggregatedTx.isValidContract()) {
                successTxs.add(aggregatedTx);
            } else {
                failedTxs.add(aggregatedTx);
            }
        });

        handleTxs(successTxs, failedTxs);
    }

    private void handleTxs(Collection<AggregatedTx> successTxs,
                           Collection<AggregatedTx> failedTxs) {

        // Handle Tx contents
        handleTxContents(successTxs, failedTxs);
    }

    private void handleTxContents(Collection<AggregatedTx> successTxs,
                                  Collection<AggregatedTx> failedTxs) {
        if (CollectionUtils.isEmpty(successTxs) && CollectionUtils.isEmpty(failedTxs)) {
            return;
        }

        Map<String, String> stakeAddressMap = new ConcurrentHashMap<>();
        blockDataService.getStakeAddressTxHashMap().forEach((stakeAddressHex, txHash) -> {
            byte[] addressBytes = HexUtil.decodeHexString(stakeAddressHex);
            ShelleyAddress shelleyAddress = new ShelleyAddress(addressBytes);
            stakeAddressMap.put(stakeAddressHex, shelleyAddress.getAddress());
        });

        // Handle address balances
        addressBalanceService.handleAddressBalance(
                blockDataService.getAggregatedAddressBalanceMap(), stakeAddressMap);
    }

}
