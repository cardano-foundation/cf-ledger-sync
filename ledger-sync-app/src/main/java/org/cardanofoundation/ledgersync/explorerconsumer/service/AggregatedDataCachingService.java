package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Epoch;

public interface AggregatedDataCachingService {

    /**
     * Add block count to cached value
     *
     * @param extraBlockCount extra block count to add
     */
    void addBlockCount(int extraBlockCount);

    /**
     * Subtract block count from cached value
     *
     * @param excessBlockCount excess block count to remove
     */
    void subtractBlockCount(int excessBlockCount);

    /**
     * Add tx count to cached value
     *
     * @param extraTxCount extra tx count to add
     */
    void addTxCount(int extraTxCount);

    /**
     * Subtract tx count from cached value
     *
     * @param excessTxCount excess tx count to remove
     */
    void subtractTxCount(int excessTxCount);

    /**
     * Add token count to cached value
     *
     * @param extraTokenCount extra token count to add
     */
    void addTokenCount(int extraTokenCount);

    /**
     * Add unique account's tx count at epoch to cached data
     *
     * @param epoch        epoch that txs of the account currently in
     * @param account      account involved to the txs
     * @param extraTxCount extra tx count to add
     */
    void addAccountTxCountAtEpoch(int epoch, String account, int extraTxCount);

    /**
     * Subtract unique account's tx count at epoch from cached data
     *
     * @param epoch         epoch that txs of the account currently in
     * @param account       account involved to the txs
     * @param excessTxCount excess tx count to remove
     */
    void subtractAccountTxCountAtEpoch(int epoch, String account, int excessTxCount);

    /**
     * Cache the latest transactions that were recently saved
     */
    void saveLatestTxs();

    /**
     * Cache current epoch's data
     *
     * @param currentEpoch epoch entity representing the current epoch
     */
    void saveCurrentEpoch(Epoch currentEpoch);

    /**
     * Commit all cached data
     */
    void commit();
}
