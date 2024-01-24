package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.projection.LatestTxOutProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TxOutRepository extends JpaRepository<TxOut, Long>, CustomTxOutRepository {

    @Query("SELECT "
            + "new org.cardanofoundation.ledgersync.projection.LatestTxOutProjection("
            + "txOut.txId, txOut.address) FROM TxOut txOut "
            + "WHERE txOut.tx IN (:txs)")
    List<LatestTxOutProjection> findAllByTxIn(@Param("txs") Collection<Tx> txs);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
