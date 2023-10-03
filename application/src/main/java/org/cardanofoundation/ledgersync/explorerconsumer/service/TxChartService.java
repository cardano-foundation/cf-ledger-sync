package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;

import java.util.Collection;

public interface TxChartService {

    /**
     * Handle transaction charts
     *
     * @param latestSavedTx most recent tx persisted in database before updating new txs
     */
    void handleTxChart(Tx latestSavedTx);

    /**
     * Rollback transaction chart statistics
     *
     * @param txs collection of tx being rolled back
     */
    void rollbackTxChart(Collection<Tx> txs);
}
