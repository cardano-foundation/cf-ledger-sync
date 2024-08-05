package org.cardanofoundation.ledgersync.custom;

import com.bloxbean.cardano.yaci.store.epoch.domain.EpochParam;
import com.bloxbean.cardano.yaci.store.epoch.storage.EpochParamStorage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
// custom epoch param storage to ignore epoch param data writing in epoch store
public class EpochParamStorageImpl implements EpochParamStorage {
    @Override
    public void save(EpochParam epochParam) {

    }

    @Override
    public Optional<EpochParam> getProtocolParams(int epoch) {
        return Optional.empty();
    }

    @Override
    public Integer getMaxEpoch() {
        return 0;
    }

    @Override
    public int deleteBySlotGreaterThan(long slot) {
        return 0;
    }
}
