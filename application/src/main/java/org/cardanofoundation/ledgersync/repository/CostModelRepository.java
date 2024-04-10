package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CostModelRepository extends JpaRepository<CostModel, Long> {

    @Query("SELECT MAX(c.id) FROM CostModel c")
    Optional<Long> findCostModeMaxId();

    Optional<CostModel> findByHash(String hash);

    @Query("SELECT c.hash FROM CostModel c WHERE c.hash in :hashes")
    Set<String> existHash(@Param("hashes") Set<String> hashes);
}
