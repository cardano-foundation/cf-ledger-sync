package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.UniqueAccountTxCountProjection;
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
            SELECT
                (CASE
                    WHEN a.stakeAddress IS NULL THEN a.address
                    ELSE CAST(a.stakeAddress.id AS STRING)
                END) AS account,
            COUNT(atb) AS txCount
            FROM Block b
            JOIN Tx tx ON tx.block = b
            JOIN AddressTxBalance atb on atb.tx = tx
            JOIN Address a ON atb.address = a
            WHERE b.epochNo = :epochNo
            GROUP BY account
            """)
    List<UniqueAccountTxCountProjection> findUniqueAccountsInEpoch(@Param("epochNo") Integer epochNo);

    @Query("""
            SELECT e.no FROM Epoch e WHERE e.maxSlot = :maxSlot AND e.no > :lastEpochParam ORDER BY e.no
            """)
    List<Integer> findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            @Param("maxSlot") Integer maxSlot, @Param("lastEpochParam") Integer lastEpochParam);
}
