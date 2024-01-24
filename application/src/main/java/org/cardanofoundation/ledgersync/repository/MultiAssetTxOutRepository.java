package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.MaTxOut;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.projection.MaTxOutProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface MultiAssetTxOutRepository extends JpaRepository<MaTxOut, Long> {

    @Query(
            "SELECT new org.cardanofoundation.ledgersync.projection.MaTxOutProjection("
                    + "mto.ident.fingerprint, "
                    + "mto.txOutId, "
                    + "mto.quantity) "
                    + "FROM MaTxOut mto "
                    + "WHERE mto.txOutId IN (:txOutIds)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<MaTxOutProjection> findAllByTxOutIdsIn(@Param("txOutIds") Collection<Long> txOutIds);

    @Modifying
    void deleteAllByTxOutTxIn(Collection<Tx> txs);
}
