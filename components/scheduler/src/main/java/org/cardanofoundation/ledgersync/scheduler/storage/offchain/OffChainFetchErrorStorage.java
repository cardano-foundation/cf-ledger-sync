package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorCpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainFetchErrorStorage extends JpaRepository<OffChainFetchError, Long> {

    List<OffChainFetchError> findByCpIdIn(Set<OffChainFetchErrorCpId> offChainFetchErrorCompoundId);

    List<OffChainFetchError> findByRetryCountLessThanEqualAndValidAfterRetryFalse(Integer maxRetry);
}
