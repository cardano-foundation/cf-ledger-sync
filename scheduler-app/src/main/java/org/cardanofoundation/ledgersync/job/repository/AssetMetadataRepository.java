package org.cardanofoundation.ledgersync.job.repository;

import org.cardanofoundation.explorer.consumercommon.entity.AssetMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssetMetadataRepository extends JpaRepository<AssetMetadata, Long> {
  List<AssetMetadata> findBySubjectIn(@Param("subjects") Set<String> subjects);
}
