package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.Optional;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainDataCheckpointStorage extends JpaRepository<OffChainDataCheckpoint, Long> {

    @Query("""
            SELECT d FROM OffChainDataCheckpoint d
            WHERE d.type = :type
            ORDER BY d.slotNo
            DESC LIMIT 1""")
    Optional<OffChainDataCheckpoint> findFirstByType(@Param("type") OffChainCheckpointType type);
}
