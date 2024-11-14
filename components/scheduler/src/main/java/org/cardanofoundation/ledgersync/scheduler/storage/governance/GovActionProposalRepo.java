package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.GovActionProposalEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.GovActionProposalId;
import java.util.List;
import java.util.Optional;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GovActionProposalRepo extends JpaRepository<GovActionProposalEntity, GovActionProposalId> {
    @Query("""
        SELECT MAX(gap.slot) as maxSlotNo
        FROM GovActionProposalEntity gap
        """)
    Optional<Long> maxSlotNo();

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO(gap.anchorUrl, gap.anchorHash, gap.slot, gap.txHash, gap.index, 0)
        FROM GovActionProposalEntity gap
        WHERE gap.slot >= :fromSlot and gap.slot <= :toSlot
        AND gap.anchorUrl IS NOT NULL
        AND gap.anchorHash IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM OffChainGovAction oc WHERE oc.govActionTxHash = gap.txHash AND oc.govActionIdx = gap.index)
        """)
    List<GovAnchorDTO> getAnchorInfoBySlotRange(@Param("fromSlot") Long fromSlot, @Param("toSlot") Long toSlot);
}
