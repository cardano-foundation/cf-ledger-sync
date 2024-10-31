package org.cardanofoundation.ledgersync.scheduler.storage.offchain;

import java.util.List;
import java.util.Set;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffChainGovActionStorage extends JpaRepository<OffChainGovAction, Long> {

    List<OffChainGovAction> findByGovActionIdIn(Set<OffChainGovActionId> govActionIds);

    @Query("""
        SELECT new org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO(
            gap.anchorUrl,
            gap.anchorHash,
            gap.slot,
            gap.txHash,
            gap.index,
            COALESCE(fe.retryCount, 0))
        FROM OffChainGovAction oga
        LEFT JOIN GovActionProposalEntity gap
            ON gap.txHash = oga.govActionTxHash AND gap.index = oga.govActionIdx
        LEFT JOIN OffChainFetchError fe
            ON fe.anchorUrl = gap.anchorUrl AND fe.anchorHash = gap.anchorHash
        WHERE oga.checkValid = :checkValid
        """)
    List<GovAnchorDTO> findByInvalid(CheckValid checkValid);
}
