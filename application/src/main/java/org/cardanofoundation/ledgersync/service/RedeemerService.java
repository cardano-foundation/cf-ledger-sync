package org.cardanofoundation.ledgersync.service;

import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Map;

public interface RedeemerService {

    /**
     * This method handles redeemers of a transaction
     *
     * @param txs         transaction bodies
     * @param txMap       transaction entity map
     * @param newTxOutMap a map of newly created txOut entities that are not inserted yet
     * @return a map of redeemers, with key is tx hash, and value is another map of redeemers executed
     * within a transaction (key is redeemer pointer, value is target redeemer record)
     */
    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> handleRedeemers( // NOSONAR
                                                                            Collection<AggregatedTx> txs, Map<String, Tx> txMap,
                                                                            Map<Pair<String, Short>, TxOut> newTxOutMap);
}
