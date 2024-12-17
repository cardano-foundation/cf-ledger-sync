package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitteeDeregistrationDTO extends AnchorDTO {

    private String committeeDeregTxHash;
    private Long committeeDeregCertIndex;

    public CommitteeDeregistrationDTO(String anchorUrl, String anchorHash, Long slot, String committeeDeregTxHash,
        Long committeeDeregCertIndex, Integer retryCount) {

        super(anchorUrl, anchorHash, slot, retryCount);
        this.committeeDeregCertIndex = committeeDeregCertIndex;
        this.committeeDeregTxHash = committeeDeregTxHash;
    }
}
