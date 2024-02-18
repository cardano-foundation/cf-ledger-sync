package org.cardanofoundation.ledgersync.aggregate.account.service;

import com.bloxbean.cardano.yaci.store.events.GenesisBlockEvent;

public interface GenesisBalanceDataService {
    void handleGenesisBalance(GenesisBlockEvent genesisBlockEvent);
}
