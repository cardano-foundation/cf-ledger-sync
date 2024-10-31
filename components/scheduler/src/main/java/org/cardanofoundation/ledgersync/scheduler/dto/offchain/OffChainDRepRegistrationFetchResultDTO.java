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
public class OffChainDRepRegistrationFetchResultDTO extends OffChainFetchResultDTO {
    private String drepRegTxHash;
    private Long drepRegCertIndex;

    public OffChainDRepRegistrationFetchResultDTO(OffChainFetchResultDTO o) {
        super(o);
    }
}
