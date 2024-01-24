package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx_;
import org.cardanofoundation.ledgersync.projection.TxTimeProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TxRepository extends JpaRepository<Tx, Long> {

    List<Tx> findAllByBlockIn(Collection<Block> blocks);

    Tx findFirstByOrderByIdDesc();

    @Query("SELECT tx.id as txId, "
            + "tx.block.time as txTime, "
            + "(SUM(CASE WHEN r IS NOT NULL THEN 1 ELSE 0 END) != 0) AS txWithSc, "
            + "(SUM(CASE WHEN r IS NULL AND tm IS NOT NULL THEN 1 ELSE 0 END) != 0) AS txWithMetadataWithoutSc, "
            + "(SUM(CASE WHEN r IS NULL AND tm IS NULL THEN 1 ELSE 0 END) != 0) AS simpleTx "
            + "FROM Tx tx "
            + "LEFT JOIN Redeemer r on r.tx = tx "
            + "LEFT JOIN TxMetadata tm ON tm.tx = tx "
            + "WHERE :id IS NULL OR tx.id > :id "
            + "GROUP BY txId, txTime")
    List<TxTimeProjection> findTxWithTimeByIdGreaterThanOrNull(@Param("id") Long id);

    @Query("SELECT tx.id as txId, "
            + "tx.block.time as txTime, "
            + "(SUM(CASE WHEN r IS NOT NULL THEN 1 ELSE 0 END) != 0) AS txWithSc, "
            + "(SUM(CASE WHEN r IS NULL AND tm IS NOT NULL THEN 1 ELSE 0 END) != 0) AS txWithMetadataWithoutSc, "
            + "(SUM(CASE WHEN r IS NULL AND tm IS NULL THEN 1 ELSE 0 END) != 0) AS simpleTx "
            + "FROM Tx tx "
            + "LEFT JOIN Redeemer r on r.tx = tx "
            + "LEFT JOIN TxMetadata tm ON tm.tx = tx "
            + "WHERE tx IN (:txs) "
            + "GROUP BY txId, txTime")
    List<TxTimeProjection> findTxWithTimeByTxIn(@Param("txs") Collection<Tx> txs);

    @EntityGraph(attributePaths = Tx_.BLOCK)
    List<Tx> findAllByOrderByBlockIdDescBlockIndexAsc(Pageable pageable);
}
