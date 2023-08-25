package org.cardanofoundation.ledgersync.explorerconsumer.service;

import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
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
