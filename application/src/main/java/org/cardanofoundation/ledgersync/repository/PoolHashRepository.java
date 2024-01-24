package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PoolHashRepository extends JpaRepository<PoolHash, Long> {

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    Optional<PoolHash> findPoolHashByHashRaw(String hashBytes);
}
