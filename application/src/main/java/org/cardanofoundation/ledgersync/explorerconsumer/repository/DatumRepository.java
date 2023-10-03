package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.Datum;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.DatumProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface DatumRepository extends JpaRepository<Datum, Long> {


    @Query("SELECT d "
            + " FROM Datum as d"
            + " WHERE d.hash IN (:hashes)")
    Set<Datum> getExistHashByHashIn(@Param("hashes") Set<String> datumHashes);

    @Query("SELECT d.hash as hash,"
            + " d.id as id"
            + " FROM Datum as d"
            + " WHERE d.hash IN (:hashes)")
    List<DatumProjection> getDatumByHashes(@Param("hashes") Set<String> hashes);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
