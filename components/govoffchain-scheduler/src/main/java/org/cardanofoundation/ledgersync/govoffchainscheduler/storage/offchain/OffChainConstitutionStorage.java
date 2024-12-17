package org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain;

import java.util.List;
import java.util.Set;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.ConstitutionAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainConstitutionStorage extends JpaRepository<OffChainConstitution, Long> {

    List<OffChainConstitution> findByConstitutionActiveEpochIn(Set<Integer> constitutionActiveEpochs);

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.ConstitutionAnchorDTO(
            ce.anchorUrl,
            ce.anchorHash,
            ce.slot,
            ce.activeEpoch,
            COALESCE(fe.retryCount, 0))
        FROM OffChainConstitution c
        JOIN ConstitutionEntity ce
            ON ce.activeEpoch = c.constitutionActiveEpoch
        LEFT JOIN OffChainFetchError fe
            ON fe.anchorUrl = ce.anchorUrl AND fe.anchorHash = ce.anchorHash AND fe.type = :type
        WHERE c.checkValid = :checkValid
        """)
    List<ConstitutionAnchorDTO> findByInvalid(CheckValid checkValid, Integer type);
}
