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
public class OffChainConstitutionFetchResultDTO extends OffChainFetchResultDTO {
    private Integer constitutionActiveEpoch;

    public OffChainConstitutionFetchResultDTO(OffChainFetchResultDTO o) {
        super(o);
    }
}
