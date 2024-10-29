package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.ConstitutionEntity;

@Repository
public interface ConstitutionRepo extends JpaRepository<ConstitutionEntity, Integer> {

    @Query("""
        SELECT MAX(ce.slot) as maxSlotNo
        FROM ConstitutionEntity ce
        """)
    Optional<Long> maxSlotNo();

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO(ce.anchorUrl, ce.anchorHash, ce.slot, ce.id, 0)
        FROM ConstitutionEntity ce
        WHERE ce.slot > :fromSlot and ce.slot <= :toSlot
        AND ce.anchorUrl IS NOT NULL
        AND ce.anchorHash IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM OffChainConstitution oc WHERE oc.constitutionActiveEpoch = ce.activeEpoch)
        """)
    List<ConstitutionAnchorDTO> getAnchorInfoBySlotRange(@Param("fromSlot") Long fromSlot, @Param("toSlot") Long toSlot);
}
