package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.VotingProcedureEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.VotingProcedureId;

@Repository
public interface VotingProcedureRepo extends JpaRepository<VotingProcedureEntity, VotingProcedureId> {

    @Query("""
        SELECT MAX(vp.slot) as maxSlotNo
        FROM VotingProcedureEntity vp
        """)
    Optional<Long> maxSlotNo();

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO(vp.anchorUrl, vp.anchorHash, vp.slot, vp.id, 0)
        FROM VotingProcedureEntity vp
        WHERE vp.slot >= :fromSlot and vp.slot <= :toSlot
        AND vp.anchorUrl IS NOT NULL
        AND vp.anchorHash IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM OffChainVotingData oc WHERE oc.votingProcedureId = vp.id)
        """)
    List<VotingDataAnchorDTO> getAnchorInfoBySlotRange(@Param("fromSlot") Long fromSlot, @Param("toSlot") Long toSlot);
}
