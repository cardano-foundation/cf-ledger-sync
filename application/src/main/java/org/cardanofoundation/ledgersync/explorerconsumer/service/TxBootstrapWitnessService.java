package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.Map;

public interface TxBootstrapWitnessService {

    void handleBootstrapWitnesses(Collection<AggregatedTx> txs, Map<String, Tx> txMap);
}
