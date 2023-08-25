package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxIn;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxInProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TxInRepository extends JpaRepository<TxIn, Long> {

    @Query("SELECT "
            + "new org.cardanofoundation.ledgersync.explorerconsumer.projection.LatestTxInProjection("
            + "txIn.txInput.id, "
            + "txOut.address"
            + ") FROM TxIn txIn "
            + "JOIN TxOut txOut ON txOut.tx = txIn.txOut AND txOut.index = txIn.txOutIndex "
            + "WHERE txIn.txInput IN (:txInputs)")
    List<LatestTxInProjection> findAllByTxInputIn(@Param("txInputs") Collection<Tx> txInputs);

    @Modifying
    void deleteAllByTxInputIn(Collection<Tx> txInputs);
}
