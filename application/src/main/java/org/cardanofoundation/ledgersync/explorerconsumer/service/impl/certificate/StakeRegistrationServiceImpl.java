package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.StakeRegistration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.StakeRegistration.StakeRegistrationBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CertificateSyncService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.AddressUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StakeRegistrationServiceImpl extends CertificateSyncService<StakeRegistration> {

    BatchCertificateDataService batchCertificateDataService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       StakeRegistration certificate, int certificateIdx,
                       Tx tx, Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        StakeRegistrationBuilder<?, ?> stakeRegistrationBuilder =
                org.cardanofoundation.explorer.consumercommon.entity.StakeRegistration.builder();

        String stakeAddressHex = AddressUtil.getRewardAddressString(
                certificate.getStakeCredential(), aggregatedBlock.getNetwork());
        StakeAddress stakeAddress = stakeAddressMap.get(stakeAddressHex);
        stakeRegistrationBuilder.addr(stakeAddress);
        stakeRegistrationBuilder.certIndex(certificateIdx);
        stakeRegistrationBuilder.epochNo(aggregatedBlock.getEpochNo());
        stakeRegistrationBuilder.tx(tx);

        batchCertificateDataService.saveStakeRegistration(stakeRegistrationBuilder.build());
    }
}
