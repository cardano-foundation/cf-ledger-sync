package org.cardanofoundation.ledgersync.govoffchainscheduler.storage.governance;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeDeRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeDeRegistrationId;

@Repository
public interface CommitteeDeregistrationRepo extends JpaRepository<CommitteeDeRegistrationEntity, CommitteeDeRegistrationId> {

    @Query("""
        SELECT MAX(cd.slot) as maxSlotNo
        FROM CommitteeDeRegistrationEntity cd
        """)
    Optional<Long> maxSlotNo();

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.CommitteeDeregistrationDTO(
            cd.anchorUrl,
            cd.anchorHash,
            cd.slot,
            cd.txHash,
            cd.certIndex,
            0)
        FROM CommitteeDeRegistrationEntity cd
        WHERE cd.slot >= :fromSlot and cd.slot <= :toSlot
        AND cd.anchorUrl IS NOT NULL
        AND cd.anchorHash IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM OffChainCommitteeDeregistration oc 
                        WHERE oc.committeeDeregTxHash = cd.txHash AND oc.committeeDeregCertIndex = cd.certIndex)
        """)
    List<CommitteeDeregistrationDTO> getAnchorInfoBySlotRange(@Param("fromSlot") Long fromSlot, @Param("toSlot") Long toSlot);
}
