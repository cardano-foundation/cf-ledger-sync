package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.Datum;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Repository
public interface DatumRepository extends JpaRepository<Datum, Long> {


    @Query("SELECT d "
            + " FROM Datum as d"
            + " WHERE d.hash IN (:hashes)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Set<Datum> getExistHashByHashIn(@Param("hashes") Set<String> datumHashes);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
