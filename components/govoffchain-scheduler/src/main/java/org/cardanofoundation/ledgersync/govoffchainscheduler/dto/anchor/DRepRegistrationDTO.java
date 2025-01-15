package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DRepRegistrationDTO extends AnchorDTO {

    private String drepRegTxHash;
    private Long drepRegCertIndex;

    public DRepRegistrationDTO(String anchorUrl, String anchorHash, Long slot, String drepRegTxHash,
        Long drepRegCertIndex, Integer retryCount) {

        super(anchorUrl, anchorHash, slot, retryCount);
        this.drepRegCertIndex = drepRegCertIndex;
        this.drepRegTxHash = drepRegTxHash;
    }
}
