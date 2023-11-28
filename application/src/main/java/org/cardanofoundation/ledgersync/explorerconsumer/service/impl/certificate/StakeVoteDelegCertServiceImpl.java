package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.StakeDelegation;
import com.bloxbean.cardano.yaci.core.model.certs.StakePoolId;
import com.bloxbean.cardano.yaci.core.model.certs.StakeVoteDelegCert;
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
public class StakeVoteDelegCertServiceImpl extends CertificateSyncService<StakeVoteDelegCert> {

    StakeDelegationServiceImpl stakeDelegationService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock, StakeVoteDelegCert certificate, int certificateIdx, Tx tx,
                       Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        var stakeDelegation = StakeDelegation.builder().stakeCredential(certificate.getStakeCredential())
                .stakePoolId(StakePoolId
                        .builder()
                        .poolKeyHash(certificate.getPoolKeyHash())
                        .build())
                .build();

        stakeDelegationService.handle(aggregatedBlock, stakeDelegation, certificateIdx, tx, redeemer, stakeAddressMap);
    }
}
