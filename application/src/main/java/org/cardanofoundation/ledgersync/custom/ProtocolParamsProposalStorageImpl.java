package org.cardanofoundation.ledgersync.custom;

import com.bloxbean.cardano.yaci.store.epoch.domain.ProtocolParamsProposal;
import com.bloxbean.cardano.yaci.store.epoch.storage.ProtocolParamsProposalStorage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProtocolParamsProposalStorageImpl implements ProtocolParamsProposalStorage {
    @Override
    public void saveAll(List<ProtocolParamsProposal> protocolParamsProposals) {

    }

    @Override
    public List<ProtocolParamsProposal> getProtocolParamsProposalsByTargetEpoch(int epoch) {
        return List.of();
    }

    @Override
    public int deleteBySlotGreaterThan(long slot) {
        return 0;
    }
}
