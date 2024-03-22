package org.cardanofoundation.ledgersync.verifier.data.app.repository.ledgersync;

import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.RangeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TxOutRepository extends JpaRepository<TxOut, Long> {

    @Query("select min(txout.id) as min, max(txout.id) as max from TxOut  txout")
    RangeProjection getRangeOfId();

    @Query("select txout.address from TxOut txout where txout.id between :minId and :maxId")
    Set<String> getSetAddressesByIdRange(long minId, long maxId);

}
