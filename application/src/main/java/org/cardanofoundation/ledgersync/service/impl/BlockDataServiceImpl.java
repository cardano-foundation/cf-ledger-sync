package org.cardanofoundation.ledgersync.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBatchBlockData;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockDataServiceImpl implements BlockDataService {

    AggregatedBatchBlockData aggregatedBatchBlockData;

    @Override
    public Map<String, String> getStakeAddressTxHashMap() {
        return aggregatedBatchBlockData.getStakeAddressTxHashMap();
    }

    @Override
    public void saveFirstAppearedTxHashForStakeAddress(String stakeAddress, String txHash) {
        aggregatedBatchBlockData.getStakeAddressTxHashMap().putIfAbsent(stakeAddress, txHash);
    }

    @Override
    public AggregatedAddressBalance getAggregatedAddressBalanceFromAddress(String address) {
        return aggregatedBatchBlockData
                .getAggregatedAddressBalanceMap()
                .computeIfAbsent(address, AggregatedAddressBalance::from);
    }

    @Override
    public Map<String, AggregatedAddressBalance> getAggregatedAddressBalanceMap() {
        return aggregatedBatchBlockData.getAggregatedAddressBalanceMap();
    }

    @Override
    public Pair<Long, Long> getFingerprintFirstAppearedBlockNoAndTxIdx(String fingerprint) {
        return aggregatedBatchBlockData.getFingerprintFirstAppearedMap().get(fingerprint);
    }

    @Override
    public void setFingerprintFirstAppearedBlockNoAndTxIdx(
            String fingerprint, Long blockNo, Long txIdx) {
        Pair<Long, Long> firstAppearedBlockNoAndTxIdx = Pair.of(blockNo, txIdx);
        aggregatedBatchBlockData.getFingerprintFirstAppearedMap()
                .putIfAbsent(fingerprint, firstAppearedBlockNoAndTxIdx);
    }

    @Override
    public AggregatedBlock getAggregatedBlock(String blockHash) {
        return aggregatedBatchBlockData.getAggregatedBlockMap().get(blockHash);
    }

    @Override
    public void saveAggregatedBlock(AggregatedBlock aggregatedBlock) {
        aggregatedBatchBlockData.getAggregatedBlockMap()
                .put(aggregatedBlock.getHash(), aggregatedBlock);
    }

    @Override
    public Collection<AggregatedBlock> getAllAggregatedBlocks() {
        return aggregatedBatchBlockData.getAggregatedBlockMap().values();
    }

    /**
     * Use for log only because method will return new aggregated block if block map is empty *
     *
     * @return
     */
    public Pair<AggregatedBlock, AggregatedBlock> getFirstAndLastBlock() {
        AggregatedBlock first;
        AggregatedBlock last = new AggregatedBlock();
        var mBlock = aggregatedBatchBlockData.getAggregatedBlockMap();
        if (!mBlock.entrySet().iterator().hasNext()) {
            return Pair.of(new AggregatedBlock(), new AggregatedBlock());
        }
        first = mBlock.entrySet().iterator().next().getValue();
        for (Map.Entry<String, AggregatedBlock> entry : mBlock.entrySet()) {
            last = entry.getValue();
        }
        return Pair.of(first, last);
    }

    @Override
    public boolean isAssetFingerprintNotMintedInTx(String fingerprint, String txHash) {
        Pair<String, String> pair = Pair.of(txHash, fingerprint);
        return aggregatedBatchBlockData.getNotMintedAssetFingerprintTxHashSet().contains(pair);
    }

    @Override
    public void saveAssetFingerprintNotMintedAtTx(String fingerprint, String txHash) {
        Pair<String, String> pair = Pair.of(txHash, fingerprint);
        aggregatedBatchBlockData.getNotMintedAssetFingerprintTxHashSet().add(pair);
    }

    public int getBlockSize() {
        return aggregatedBatchBlockData.getAggregatedBlockMap().size();
    }

    @Override
    public void clearBatchBlockData() {
        aggregatedBatchBlockData.clear();
    }
}
