package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain;

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
public class OffChainGovFetchResultDTO extends OffChainFetchResultDTO {
    private String govActionTxHash;
    private Long govActionIdx;

    public OffChainGovFetchResultDTO(OffChainFetchResultDTO o) {
        super(o);
    }
}
