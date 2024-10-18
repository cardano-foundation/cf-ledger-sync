package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionCpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainGovActionStorage extends JpaRepository<OffChainGovAction, Long> {

    List<OffChainGovAction> findByCpIdIn(Set<OffChainGovActionCpId> offChainFetchErrorCompoundId);
}
