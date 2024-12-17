package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotingDataAnchorDTO extends AnchorDTO {

    private UUID votingProcedureId;

    public VotingDataAnchorDTO(String anchorUrl, String anchorHash, Long slot, UUID votingProcedureId,
            Integer retryCount) {

        super(anchorUrl, anchorHash, slot, retryCount);
        this.votingProcedureId = votingProcedureId;
    }
}
