package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ParamProposalService {

    /**
     * Handle CDDL param proposals
     *
     * @param successTxs collection of success txs
     * @param txMap      a map with key is tx hash and value is the
     *                   respective tx entity
     * @return a list of handled param proposal entities
     */
    List<ParamProposal> handleParamProposals(
            Collection<AggregatedTx> successTxs, Map<String, Tx> txMap);
}
