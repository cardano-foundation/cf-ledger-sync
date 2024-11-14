package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDrepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainDRepRegistrationId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainDRepRegistrationStorage extends JpaRepository<OffChainDrepRegistration, Long> {

    List<OffChainDrepRegistration> findByDrepRegistrationIdIn(
            Set<OffChainDRepRegistrationId> dRepRegistrationIds);

    @Query("""
            SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO(
                cd.anchorUrl,
                cd.anchorHash,
                cd.slot,
                cd.txHash,
                cd.certIndex,
                COALESCE(fe.retryCount, 0))
            FROM OffChainDrepRegistration odr
            JOIN DRepRegistrationEntity cd
                ON cd.txHash = odr.drepRegTxHash AND cd.certIndex = odr.drepRegCertIndex
            LEFT JOIN OffChainFetchError fe
                ON fe.anchorUrl = cd.anchorUrl AND fe.anchorHash = cd.anchorHash AND fe.type = :type
            WHERE odr.checkValid = :checkValid
            """)
    List<DRepRegistrationDTO> findByInvalid(CheckValid checkValid, Integer type);
}
