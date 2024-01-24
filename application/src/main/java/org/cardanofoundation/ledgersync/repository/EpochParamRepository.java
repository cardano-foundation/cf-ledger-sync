package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.consumercommon.entity.EpochParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface EpochParamRepository extends JpaRepository<EpochParam, Long> {

    @Query(value = "SELECT ep from EpochParam ep"
            + " WHERE ep.epochNo = (SELECT MAX(e.epochNo) FROM EpochParam e)")
    Optional<EpochParam> findLastEpochParam();

    Optional<EpochParam> findEpochParamByEpochNo(Integer epochNo);

    @Modifying
    void deleteAllByBlockIn(Collection<Block> blocks);
}
