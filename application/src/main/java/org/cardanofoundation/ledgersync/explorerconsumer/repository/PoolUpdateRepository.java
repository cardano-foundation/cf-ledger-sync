package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.PoolHash;
import org.cardanofoundation.explorer.consumercommon.entity.PoolUpdate;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface PoolUpdateRepository extends JpaRepository<PoolUpdate, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Boolean existsByPoolHash(PoolHash poolHash);

    @Modifying
    void deleteAllByRegisteredTxIn(Collection<Tx> registeredTxs);
}
