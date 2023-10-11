package org.cardanofoundation.ledgersync.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetMetadataDTO {
    private String subject;
    private AssetMetadataProperty name;
    private AssetMetadataProperty description;
    private String policy;
    private AssetMetadataProperty ticker;
    private AssetMetadataProperty url;
    private AssetMetadataProperty logo;
    private AssetMetadataProperty decimals;
}
