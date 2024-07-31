package org.cardanofoundation.ledgersync.custom;

import com.bloxbean.cardano.yaci.store.common.model.Order;
import com.bloxbean.cardano.yaci.store.epoch.domain.ProtocolParamsProposal;
import com.bloxbean.cardano.yaci.store.epoch.storage.ProtocolParamsProposalStorageReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProtocolParamsProposalStorageReaderImpl implements ProtocolParamsProposalStorageReader {
    @Override
    public List<ProtocolParamsProposal> getProtocolParamsProposals(int page, int count, Order order) {
        return List.of();
    }

    @Override
    public List<ProtocolParamsProposal> getProtocolParamsProposalsByCreateEpoch(int epoch) {
        return List.of();
    }
}
