package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.Map;

public interface TxBootstrapWitnessService {

    void handleBootstrapWitnesses(Collection<AggregatedTx> txs, Map<String, Tx> txMap);
}
