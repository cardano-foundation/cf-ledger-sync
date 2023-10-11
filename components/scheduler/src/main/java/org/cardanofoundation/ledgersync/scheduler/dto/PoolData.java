package org.cardanofoundation.ledgersync.scheduler.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PoolData {

    Long poolId;
    Long metadataRefId;
    String errorMessage;
    String hash;
    String logoUrl;
    String iconUrl;
    int status;
    byte[] json;
    boolean valid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PoolData data = (PoolData) o;
        return poolId.equals(data.poolId) && metadataRefId.equals(data.metadataRefId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(poolId, metadataRefId);
    }
}
