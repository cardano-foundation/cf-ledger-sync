package org.cardanofoundation.ledgersync.aggregate.account.service;

import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.account.model.BlockInfo;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Map;

public interface BlockDataService {

    /**
     * Get stake address with its first appeared tx hash map
     *
     * @return a map with key is stake address hex, value is first appeared tx hash
     */
    Map<String, String> getStakeAddressTxHashMap();

    /**
     * Save a stake address's first appeared tx hash
     *
     * @param stakeAddress target stake address hex string
     * @param txHash       first appeared tx hash
     */
    void saveFirstAppearedTxHashForStakeAddress(String stakeAddress, String txHash);

    /**
     * Get aggregated address balance object from address string (Base58 or Bech32 form) If there
     * isn't any, create a new one, push it to aggregated address balance map and return it
     *
     * @param address address string (Base58 or Bech32 form)
     * @return aggregated address balance object
     */
    AggregatedAddressBalance getAggregatedAddressBalanceFromAddress(String address);

    /**
     * Get aggregated address balance map
     *
     * @return a map with key is address string (Base58 or Bech32 form) and value is associated
     * aggregated address balance object
     */
    Map<String, AggregatedAddressBalance> getAggregatedAddressBalanceMap();

    /**
     * Get an asset fingerprint's first appeared block no and tx idx
     *
     * @param fingerprint target asset fingerprint
     * @return a pair of first appeared block no and that block's tx idx
     */
    Pair<Long, Long> getFingerprintFirstAppearedBlockNoAndTxIdx(String fingerprint);

    /**
     * Set an asset fingerprint's first appeared block no and tx idx
     *
     * @param fingerprint target asset fingerprint
     * @param blockNo     asset's first appeared block no
     * @param txIdx       asset's first appeared tx idx within specified block no
     */
    void setFingerprintFirstAppearedBlockNoAndTxIdx(String fingerprint, Long blockNo, Long txIdx);

    /**
     * Get aggregated block object by its block hash
     *
     * @param blockHash block hash
     * @return aggregated block object
     */
    AggregatedBlock getAggregatedBlock(String blockHash);

    /**
     * Save aggregated block object
     *
     * @param aggregatedBlock aggregated block object
     */
    void saveAggregatedBlock(AggregatedBlock aggregatedBlock);

    /**
     * Get all aggregated block objects
     *
     * @return all aggregated block objects
     */
    Collection<AggregatedBlock> getAllAggregatedBlocks();

    /**
     * Get first and last block in block map (for log only)
     */
    Pair<AggregatedBlock, AggregatedBlock> getFirstAndLastBlock();

    /**
     * Check if this asset is not minted at tx
     *
     * @param fingerprint target asset fingerprint to check
     * @param txHash      asset fingerprint's associated tx hash
     * @return true if asset is not minted at tx, false otherwise
     */
    boolean isAssetFingerprintNotMintedInTx(String fingerprint, String txHash);

    /**
     * Save asset's not minted state at a tx
     *
     * @param fingerprint asset's fingerprint to save
     * @param txHash      asset fingerprint's associated tx hash
     */
    void saveAssetFingerprintNotMintedAtTx(String fingerprint, String txHash);

    void saveBlockInfoOfTx(BlockInfo block, String txHash);

    BlockInfo getBlockInfoOfTx(String txHash);

    int getBlockSize();

    /**
     * Clear batch aggregated block data
     */
    void clearBatchBlockData();
}
