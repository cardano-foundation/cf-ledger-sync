package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainConstitutionStorage extends JpaRepository<OffChainConstitution, Long> {

    List<OffChainConstitution> findByConstitutionActiveEpochIn(Set<Integer> constitutionActiveEpoch);

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO(
            ce.anchorUrl,
            ce.anchorHash,
            ce.slot,
            ce.activeEpoch,
            COALESCE(fe.retryCount, 0))
        FROM OffChainConstitution c
        LEFT JOIN ConstitutionEntity ce
            ON ce.activeEpoch = c.constitutionActiveEpoch
        LEFT JOIN OffChainFetchError fe
            ON fe.anchorUrl = ce.anchorUrl AND fe.anchorHash = ce.anchorHash
        WHERE c.checkValid = :checkValid
        """)
    List<ConstitutionAnchorDTO> findByInvalid(CheckValid checkValid);
}
