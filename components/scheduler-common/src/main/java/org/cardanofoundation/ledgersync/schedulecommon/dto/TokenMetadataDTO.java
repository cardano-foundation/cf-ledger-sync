package org.cardanofoundation.ledgersync.schedulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenMetadataDTO {
    private String url;
    private String ticker;
    private Integer decimals;
    private String logo;
    private String description;
}
