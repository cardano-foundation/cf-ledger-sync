package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Epoch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EpochRepository extends JpaRepository<Epoch, Long> {

    @Transactional(readOnly = true)
    Optional<Epoch> findEpochByNo(Integer no);

    List<Epoch> findAllByNoIn(Collection<Integer> no);

    Optional<Epoch> findFirstByOrderByNoDesc();

    @Query("""
            SELECT e.no FROM Epoch e WHERE e.maxSlot = :maxSlot AND e.no > :lastEpochParam ORDER BY e.no
            """)
    List<Integer> findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            @Param("maxSlot") Integer maxSlot, @Param("lastEpochParam") Integer lastEpochParam);
}
