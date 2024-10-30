package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainCommitteeDeregistrationId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainCommitteeDeregStorage extends JpaRepository<OffChainCommitteeDeregistration, Long> {

    List<OffChainCommitteeDeregistration> findByCommitteeDeregistrationIdIn(
            Set<OffChainCommitteeDeregistrationId> offChainFetchErrorCompoundId);

    @Query("""
            SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.CommitteeDeregistrationDTO(
                cd.anchorUrl,
                cd.anchorHash,
                cd.slot,
                cd.txHash,
                cd.certIndex,
                COALESCE(fe.retryCount, 0))
            FROM OffChainCommitteeDeregistration ocd
            LEFT JOIN CommitteeDeRegistrationEntity cd
                ON cd.txHash = ocd.committeeDeregTxHash AND cd.certIndex = ocd.committeeDeregCertIndex
            LEFT JOIN OffChainFetchError fe
                ON fe.anchorUrl = cd.anchorUrl AND fe.anchorHash = cd.anchorHash
            WHERE ocd.checkValid = :checkValid
            """)
    List<CommitteeDeregistrationDTO> findByInvalid(CheckValid checkValid);
}
