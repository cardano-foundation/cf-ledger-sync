package org.cardanofoundation.ledgersync.aggregate.account.repository;


import org.cardanofoundation.ledgersync.aggregate.account.model.MultiAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface MultiAssetRepository extends JpaRepository<MultiAsset, Long> {

    /*
     * Selected entities' changes are not meant to be reflected immediately in database
     * due to slow update time or any unexpected behaviour, hence this select query should
     * be marked as read-only transaction, as the changes will be applied later through
     * JPA save() or saveAll() methods
     */
    @Transactional(readOnly = true)
    List<MultiAsset> findMultiAssetsByFingerprintIn(Collection<String> fingerprints);
//
//    @Query("SELECT at.multiAsset.id AS identId, "
//            + "COUNT(DISTINCT at.tx) as txCount "
//            + "FROM AddressToken at "
//            + "WHERE at.tx IN (:txs) "
//            + "GROUP BY at.multiAsset")
//    List<MultiAssetTxCountProjection> getMultiAssetTxCountByTxs(@Param("txs") Collection<Tx> txs);
//
//    @Query("SELECT at.multiAsset.id as identId,"
//            + "SUM(at.balance) as totalVolume "
//            + "FROM AddressToken at "
//            + "WHERE at.tx IN (:txs) AND at.balance > 0"
//            + "GROUP BY at.multiAsset")
//    List<MultiAssetTotalVolumeProjection> getMultiAssetTotalVolumeByTxs(
//            @Param("txs") Collection<Tx> txs);
//}
}