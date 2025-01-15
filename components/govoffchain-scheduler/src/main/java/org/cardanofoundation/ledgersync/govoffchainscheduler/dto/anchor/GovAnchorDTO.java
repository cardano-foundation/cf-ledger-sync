package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GovAnchorDTO extends AnchorDTO {

    private String govActionTxHash;
    private Long govActionIdx;

    public GovAnchorDTO(String anchorUrl, String anchorHash, Long slot, String govActionTxHash,
        Long govActionIdx, Integer retryCount) {

        super(anchorUrl, anchorHash, slot, retryCount);
        this.govActionIdx = govActionIdx;
        this.govActionTxHash = govActionTxHash;
    }
}
