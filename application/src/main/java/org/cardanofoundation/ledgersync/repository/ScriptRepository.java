package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Script;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.projection.ScriptProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {


    @Query("SELECT s.hash as hash,"
            + " s.id as id"
            + " FROM Script as s"
            + " WHERE s.hash IN (:hashes)")
    @Transactional(readOnly = true)
    List<ScriptProjection> getScriptByHashes(@Param("hashes") Set<String> hashes);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
