package org.cardanofoundation.ledgersync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockMessage {

    private String name;
    private Instant createdDate = Instant.now();
}
