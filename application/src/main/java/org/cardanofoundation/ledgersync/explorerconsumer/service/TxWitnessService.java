package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;

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
