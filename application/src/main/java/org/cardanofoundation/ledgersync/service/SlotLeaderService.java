package org.cardanofoundation.ledgersync.service;

import com.bloxbean.cardano.yaci.core.model.Block;
import com.bloxbean.cardano.yaci.core.model.byron.ByronMainBlock;
import org.cardanofoundation.ledgersync.consumercommon.entity.SlotLeader;
import org.cardanofoundation.ledgersync.aggregate.AggregatedSlotLeader;
//import org.cardanofoundation.ledgersync.common.common.Block;
//import org.cardanofoundation.ledgersync.common.common.byron.ByronMainBlock;

public interface SlotLeaderService {
    /**
     * find slot leader after Byron era with issuerVkey,
     *
     * @param blockCddl block {@link Block}
     * @return
     */
    AggregatedSlotLeader getSlotLeaderHashAndPrefix(Block blockCddl);

    /**
     * find slot leader in Byron era pubKey
     *
     * @param blockCddl
     * @return
     */
    AggregatedSlotLeader getSlotLeaderHashAndPrefix(ByronMainBlock blockCddl);

    /**
     * Get slot leader entity by its raw hash and prefix
     *
     * @param hashRaw raw hash
     * @param prefix  prefix string
     * @return slot leader entity
     */
    SlotLeader getSlotLeader(String hashRaw, String prefix);
}
