package org.cardanofoundation.ledgersync.schedulecommon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetMetadataProperty {
    private String value;
    private String sequenceNumber;
    private Object[] signatures;
}
