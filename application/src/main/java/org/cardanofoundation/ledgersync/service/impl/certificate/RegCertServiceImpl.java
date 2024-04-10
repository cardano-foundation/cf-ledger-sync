package org.cardanofoundation.ledgersync.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.RegCert;
import com.bloxbean.cardano.yaci.core.model.certs.StakeRegistration;
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
public class RegCertServiceImpl extends CertificateSyncService<RegCert> {
    StakeRegistrationServiceImpl stakeRegistrationService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       RegCert certificate, int certificateIdx, Tx tx, Redeemer redeemer,
                       Map<String, StakeAddress> stakeAddressMap) {
        var stakeRegistration = StakeRegistration.builder().stakeCredential(certificate.getStakeCredential()).build();

        stakeRegistrationService.handle(aggregatedBlock, stakeRegistration, certificateIdx, tx, redeemer, stakeAddressMap);
    }
}
