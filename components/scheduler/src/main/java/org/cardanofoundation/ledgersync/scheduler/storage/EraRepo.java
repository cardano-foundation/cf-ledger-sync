package org.cardanofoundation.ledgersync.scheduler.storage;

import com.bloxbean.cardano.yaci.store.core.storage.impl.model.EraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EraRepo extends JpaRepository<EraEntity, Integer> {

    @Query("""
        SELECT COALESCE(e.startSlot, 0) FROM EraEntity e
        WHERE e.era = :eraNo
        ORDER BY e.startSlot DESC
        LIMIT 1
        """)
    Long getStartSlotByEra(Integer eraNo);
}
