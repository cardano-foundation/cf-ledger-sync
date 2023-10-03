package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.GenesisKeyDelegation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CertificateSyncService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GenesisKeyDelegationServiceImpl extends CertificateSyncService<GenesisKeyDelegation> {

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       GenesisKeyDelegation certificate, int certificateIdx, Tx tx, Redeemer redeemer,
                       Map<String, StakeAddress> stakeAddressMap) {
        // No-op
    }
}
