package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.StakeDeregistration;
import com.bloxbean.cardano.yaci.core.model.certs.UnregCert;
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
public class UnregCertServiceImpl extends CertificateSyncService<UnregCert> {

    StakeDeregistrationServiceImpl stakeDeregistrationService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock, UnregCert certificate, int certificateIdx, Tx tx,
                       Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        var stakeDerigistration = StakeDeregistration.builder().stakeCredential(certificate.getStakeCredential()).build();

        stakeDeregistrationService.handle(aggregatedBlock, stakeDerigistration, certificateIdx, tx, redeemer, stakeAddressMap);
    }

}
