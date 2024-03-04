package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.Datum;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.dto.EUTXOWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TxOutService {

    /**
     * Get all tx outs can use from aggregated tx ins
     *
     * @param txIns aggregated tx ins for tx outs selection
     * @return collection of all tx outs can use
     */
    Collection<TxOut> getTxOutCanUseByAggregatedTxIns(Collection<AggregatedTxIn> txIns);

    /**
     * Prepare all tx outs data from aggregated tx outs, including asset tx outputs
     *
     * @param aggregatedTxOutMap a multivalued map with key is tx hash, and value is a list of
     *                           associated aggregated tx outs
     * @param txMap              a map with key is tx hash and value is the respective tx entity
     * @param stakeAddressMap    a map of stake address entities associated with all entities inside
     *                           the current block batch
     * @param datumMap
     * @return an extended transaction output wrapper class, consists of a collection of prepared tx
     * outs and multi asset tx outs
     */
    EUTXOWrapper prepareTxOuts(
            Map<String, List<AggregatedTxOut>> aggregatedTxOutMap, Map<String, Tx> txMap,
            Map<String, StakeAddress> stakeAddressMap, Map<String, Datum> datumMap);

    /**
     * Handle failed tx outs data of success txs (aggregated collateral return) and failed txs
     * (aggregated tx outputs)
     *
     * @param successTxs      a collection of success txs
     * @param failedTxs       a collection of failed txs
     * @param txMap           a map with key is tx hash and value is the respective tx entity
     * @param stakeAddressMap a map of stake address entities associated with all entities inside the
     *                        current block batch
     * @param datumMap
     */
    void handleFailedTxOuts(Collection<AggregatedTx> successTxs,
                            Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap,
                            Map<String, StakeAddress> stakeAddressMap, Map<String, Datum> datumMap);
}
