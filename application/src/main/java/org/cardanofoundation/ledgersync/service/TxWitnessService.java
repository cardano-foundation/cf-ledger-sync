package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.Map;

public interface TxWitnessService {

    /**
     * handle tx witness in vkWitness from shelly era.
     * handle tx witness in bootstrapWitness for byron era.
     *
     * @param txs
     * @param txMap
     */
    void handleTxWitness(Collection<AggregatedTx> txs, Map<String, Tx> txMap);
}
