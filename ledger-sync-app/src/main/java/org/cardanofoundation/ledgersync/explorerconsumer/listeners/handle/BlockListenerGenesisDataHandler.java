package org.cardanofoundation.ledgersync.explorerconsumer.listeners.handle;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.explorerconsumer.service.GenesisDataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Profile("!test-unit & !test-integration")
public class BlockListenerGenesisDataHandler {

    final GenesisDataService genesisDataService;

    @PostConstruct
    void setupGenesisData() {
        genesisDataService.setupData();
    }
}
