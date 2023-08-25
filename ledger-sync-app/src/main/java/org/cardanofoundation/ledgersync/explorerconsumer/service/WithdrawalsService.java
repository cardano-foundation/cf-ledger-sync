package org.cardanofoundation.ledgersync.explorerconsumer.service;

import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Map;

public interface WithdrawalsService {

    /**
     * Handle all withdrawals data
     *
     * @param successTxs      collection of success txs containing withdrawals data
     * @param txMap           a map with key is tx hash and value is the respective tx entity
     * @param stakeAddressMap a map with key is raw stake address hex and value is the respective
     *                        stake address entity
     * @param redeemersMap    a map of redeemers, with key is tx hash, and value is another map of
     *                        redeemers executed within a transaction (key is redeemer pointer,
     *                        value is target redeemer record)
     */
    void handleWithdrawal(Collection<AggregatedTx> successTxs, Map<String, Tx> txMap,
                          Map<String, StakeAddress> stakeAddressMap,
                          Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap);
}
