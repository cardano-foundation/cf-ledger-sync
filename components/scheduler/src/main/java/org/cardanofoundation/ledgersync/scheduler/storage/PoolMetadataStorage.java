package org.cardanofoundation.ledgersync.scheduler.storage;

import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolMetadataRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoolMetadataStorage extends JpaRepository<PoolMetadataRef, Long> {

  Optional<PoolMetadataRef> findPoolMetadataRefByPoolHashAndUrlAndHash(
      PoolHash poolHash, String url, String hash);

  Optional<PoolMetadataRef> findById(Long id);

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  List<PoolMetadataRef> findByIdIn(List<Long> poolMetadataIds);
}
