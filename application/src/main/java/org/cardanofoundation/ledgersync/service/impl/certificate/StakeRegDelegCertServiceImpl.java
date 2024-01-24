package org.cardanofoundation.ledgersync.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.service.CertificateSyncService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StakeRegDelegCertServiceImpl extends CertificateSyncService<StakeRegDelegCert> {

    StakeDelegationServiceImpl stakeDelegationService;
    StakeRegistrationServiceImpl stakeRegistrationService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock, StakeRegDelegCert certificate, int certificateIdx,
                       Tx tx, Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        var stakeDelegation = StakeDelegation.builder().stakeCredential(certificate.getStakeCredential())
                .stakePoolId(StakePoolId
                        .builder()
                        .poolKeyHash(certificate.getPoolKeyHash())
                        .build()).build();
        var stakeRegistration = StakeRegistration.builder().stakeCredential(certificate.getStakeCredential()).build();

        stakeDelegationService.handle(aggregatedBlock, stakeDelegation, certificateIdx, tx, redeemer, stakeAddressMap);
        stakeRegistrationService.handle(aggregatedBlock, stakeRegistration, certificateIdx, tx, redeemer, stakeAddressMap);
    }
}
