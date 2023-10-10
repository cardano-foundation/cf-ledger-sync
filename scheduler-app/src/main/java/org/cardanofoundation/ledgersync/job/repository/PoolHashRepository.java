package org.cardanofoundation.ledgersync.job.repository;

import org.cardanofoundation.explorer.consumercommon.entity.PoolHash;
import org.cardanofoundation.ledgersync.job.projection.PoolHashUrlProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PoolHashRepository extends JpaRepository<PoolHash, Long> {

  @Query(
      value =
          "SELECT ph.id AS poolId, pmr.url as url, pmr.id as metadataId FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON (ph = pu.poolHash AND pu.id = (SELECT max(pu2.id) from PoolUpdate pu2 WHERE pu2.activeEpochNo <= (SELECT MAX(e.no) FROM Epoch e) AND pu2.poolHash = ph)) "
              + "JOIN PoolMetadataRef pmr ON (pmr = pu.meta) "
              + "LEFT JOIN PoolOfflineFetchError pofe ON (pofe.poolMetadataRef = pmr AND pofe.poolHash = ph) "
              + "WHERE (pofe.retryCount < 5 OR pofe.retryCount IS NULL)"
              + "AND NOT EXISTS(SELECT pod.id FROM PoolOfflineData pod WHERE pod.pool = ph AND pod.poolMetadataRef = pmr) "
              + "ORDER BY ph.id ASC"
  )
  List<PoolHashUrlProjection> findPoolHashAndUrl(Pageable pageable);

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  List<PoolHash> findByIdIn(List<Long> poolId);
}
