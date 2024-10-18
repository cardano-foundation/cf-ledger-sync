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
public class OffChainFetchResultDTO {
    String anchorUrl;
    String anchorHash;
    String rawData;
    private boolean isValid;
    private String fetchFailError;
    private Long slotNo;

    public OffChainFetchResultDTO(OffChainFetchResultDTO o){
        this.anchorHash = o.getAnchorHash();
        this.anchorUrl = o.getAnchorUrl();
        this.rawData = o.getRawData();
        this.isValid = o.isValid();
        this.fetchFailError = o.getFetchFailError();
        this.slotNo = o.getSlotNo();
    }
}
