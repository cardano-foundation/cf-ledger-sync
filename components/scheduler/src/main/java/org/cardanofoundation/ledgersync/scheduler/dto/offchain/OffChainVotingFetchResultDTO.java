package org.cardanofoundation.ledgersync.scheduler.dto.offchain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainVotingFetchResultDTO extends OffChainFetchResultDTO {
    UUID votingProcedureId;

    public OffChainVotingFetchResultDTO(OffChainFetchResultDTO o) {
        super(o);
    }
}
