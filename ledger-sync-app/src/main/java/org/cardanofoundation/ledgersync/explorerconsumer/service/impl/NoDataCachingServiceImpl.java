package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Profile("!caching")
@Service
public class NoDataCachingServiceImpl implements AggregatedDataCachingService {
    @Override
    public void addBlockCount(int extraBlockCount) {
        //do nothing
    }

    @Override
    public void subtractBlockCount(int excessBlockCount) {
        //do nothing
    }

    @Override
    public void addTxCount(int extraTxCount) {
        //do nothing
    }

    @Override
    public void subtractTxCount(int excessTxCount) {
        //do nothing
    }

    @Override
    public void addTokenCount(int extraTokenCount) {
        //do nothing
    }

    @Override
    public void addAccountTxCountAtEpoch(int epoch, String account, int extraTxCount) {
        //do nothing
    }

    @Override
    public void subtractAccountTxCountAtEpoch(int epoch, String account, int excessTxCount) {
        //do nothing
    }

    @Override
    public void saveLatestTxs() {
        //do nothing
    }

    @Override
    public void saveCurrentEpoch(Epoch currentEpoch) {
        //do nothing
    }

    @Override
    public void commit() {
        //do nothing
    }
}
