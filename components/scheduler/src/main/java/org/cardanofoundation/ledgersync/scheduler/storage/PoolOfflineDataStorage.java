package org.cardanofoundation.ledgersync.scheduler.storage;

import org.cardanofoundation.explorer.consumercommon.entity.PoolOfflineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface PoolOfflineDataStorage extends JpaRepository<PoolOfflineData, Long> {
  @Query(
      "SELECT pod "
          + "FROM PoolOfflineData pod "
          + "WHERE pod.poolMetadataRef.id IN :ids "
          + "ORDER BY pod.pool.id ASC, "
          + "pod.poolMetadataRef.id ASC")
  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  Set<PoolOfflineData> findPoolOfflineDataHashByPoolMetadataRefIds(@Param("ids") List<Long> ids);

  List<PoolOfflineData> findPoolOfflineDataByPoolIdIn(List<Long> poolIds);
}
