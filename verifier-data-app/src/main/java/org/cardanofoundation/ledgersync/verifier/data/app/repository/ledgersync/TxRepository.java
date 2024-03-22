package org.cardanofoundation.ledgersync.verifier.data.app.repository.ledgersync;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.RangeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TxRepository extends JpaRepository<Tx, Long> {
    @Query("select min(tx.id) as min, max(tx.id) as max from Tx tx")
    RangeProjection getRangeOfId();

    @Query("select tx.hash from Tx tx where tx.id between :minId and :maxId")
    Set<String> getTxsByIdRange(long minId, long maxId);
}
