package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationId;

@Repository
public interface DRepRegistrationRepo extends JpaRepository<DRepRegistrationEntity, DRepRegistrationId> {

    @Query("""
        SELECT MAX(dr.slot) as maxSlotNo
        FROM DRepRegistrationEntity dr
        """)
    Optional<Long> maxSlotNo();

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO(
            dr.anchorUrl,
            dr.anchorHash,
            dr.slot,
            dr.txHash,
            dr.certIndex,
            0)
        FROM DRepRegistrationEntity dr
        WHERE dr.slot >= :fromSlot and dr.slot <= :toSlot
        AND dr.anchorUrl IS NOT NULL
        AND dr.anchorHash IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM OffChainDrepRegistration oc
                        WHERE oc.drepRegTxHash = dr.txHash AND oc.drepRegCertIndex = dr.certIndex)
        """)
    List<DRepRegistrationDTO> getAnchorInfoBySlotRange(@Param("fromSlot") Long fromSlot, @Param("toSlot") Long toSlot);
}
