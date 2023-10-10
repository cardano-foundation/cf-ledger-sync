package org.cardanofoundation.ledgersync.job.repository;

import org.cardanofoundation.explorer.consumercommon.entity.PoolHash;
import org.cardanofoundation.explorer.consumercommon.entity.PoolMetadataRef;
import org.cardanofoundation.explorer.consumercommon.entity.PoolOfflineFetchError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolOfflineFetchErrorRepository
    extends JpaRepository<PoolOfflineFetchError, Long> {

  @Query(
      "SELECT poe FROM PoolOfflineFetchError poe WHERE poe.poolMetadataRef.id IN :poolMetadataIds ")
  List<PoolOfflineFetchError> findPoolOfflineFetchErrorByPoolMetadataRefIn(
      @Param("poolMetadataIds") List<Long> poolMetadataId);

  PoolOfflineFetchError findByPoolHashAndPoolMetadataRef(PoolHash poolHash, PoolMetadataRef poolMetadataRef);
}
