package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainVotingDataStorage extends JpaRepository<OffChainVotingData, Long> {

    List<OffChainVotingData> findByVotingProcedureIdIn(Set<UUID> votingProcedureId);

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO(
            vp.anchorUrl,
            vp.anchorHash,
            vp.slot,
            vp.id,
            COALESCE(fe.retryCount, 0))
        FROM OffChainVotingData ov
        LEFT JOIN VotingProcedureEntity vp
            ON vp.id = ov.votingProcedureId
        LEFT JOIN OffChainFetchError fe
            ON fe.anchorUrl = vp.anchorUrl AND fe.anchorHash = vp.anchorHash
        WHERE ov.checkValid = :checkValid
        """)
    List<VotingDataAnchorDTO> findByInvalid(CheckValid checkValid);
}
