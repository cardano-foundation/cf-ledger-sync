package org.cardanofoundation.ledgersync.scheduler.dto.offchain;

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
public class OffChainCommitteeDeregFetchResultDTO extends OffChainFetchResultDTO {
    private String committeeDeregTxHash;
    private Long committeeDeregCertIndex;

    public OffChainCommitteeDeregFetchResultDTO(OffChainFetchResultDTO o) {
        super(o);
    }
}
