package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainVotingDataStorage extends JpaRepository<OffChainVotingData, Long> {

}
