package org.cardanofoundation.ledgersync.service;

import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TxInService {

    /**
     * Handle all tx ins data
     *
     * @param txs           collection of aggregated txs with tx ins data
     * @param txInMap       a multivalued tx in map with key is tx hash and value is a set of
     *                      associated tx ins
     * @param txMap         a map with key is tx hash and value is the respective tx entity
     * @param newTxOutMap   a map of newly created txOut entities that are not inserted yet
     * @param newMaTxOutMap a map of newly created maTxOut entities that are not inserted yet
     * @param redeemersMap  a map of redeemers, with key is tx hash, and value is another map of
     *                      redeemers executed within a transaction (key is redeemer pointer, value
     *                      is target redeemer record)
     */
    void handleTxIns(Collection<AggregatedTx> txs, Map<String, Set<AggregatedTxIn>> txInMap,
                     Map<String, Tx> txMap, Map<Pair<String, Short>, TxOut> newTxOutMap,
                     Map<Long, List<MaTxOut>> newMaTxOutMap,
                     Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap);

    /**
     * Handle unconsumed tx ins
     *
     * @param unconsumedTxInMap a multivalued tx in map with key is tx hash and value is a set of
     *                          associated unconsumed tx ins
     * @param newTxOutMap       a map of newly created txOut entities that are not inserted yet
     * @param txMap             a map with key is tx hash and value is the respective tx entity
     * @param redeemersMap      a map of redeemers, with key is tx hash, and value is another map of
     *                          redeemers executed within a transaction (key is redeemer pointer,
     *                          value is target redeemer record)
     * @return a list of handled unconsumed tx ins
     */
    List<UnconsumeTxIn> handleUnconsumeTxIn(
            Map<String, Set<AggregatedTxIn>> unconsumedTxInMap,
            Map<Pair<String, Short>, TxOut> newTxOutMap, Map<String, Tx> txMap,
            Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap);
}
