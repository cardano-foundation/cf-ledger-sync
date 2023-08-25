package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;


import com.bloxbean.cardano.yaci.core.model.certs.StakeDeregistration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.StakeDeregistration.StakeDeregistrationBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CertificateSyncService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.AddressUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StakeDeregistrationServiceImpl extends CertificateSyncService<StakeDeregistration> {

    BatchCertificateDataService batchCertificateDataService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       StakeDeregistration certificate, int certificateIdx,
                       Tx tx, Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        StakeDeregistrationBuilder<?, ?> stakeDeregistrationBuilder =
                org.cardanofoundation.explorer.consumercommon.entity.StakeDeregistration.builder();

        String stakeAddressHex = AddressUtil.getRewardAddressString(
                certificate.getStakeCredential(), aggregatedBlock.getNetwork());
        Optional.ofNullable(stakeAddressMap.get(stakeAddressHex))
                .ifPresentOrElse(stakeDeregistrationBuilder::addr, () -> {
                    log.error("Stake address with address hex {} not found", stakeAddressHex);
                    log.error(
                            "Block number: {}, block hash: {}",
                            aggregatedBlock.getBlockNo(),
                            aggregatedBlock.getHash());
                    throw new IllegalStateException();
                });

        stakeDeregistrationBuilder.certIndex(certificateIdx);

        stakeDeregistrationBuilder.epochNo(aggregatedBlock.getEpochNo());
        stakeDeregistrationBuilder.tx(tx);
        stakeDeregistrationBuilder.redeemer(redeemer);

        batchCertificateDataService.saveStakeDeregistration(stakeDeregistrationBuilder.build());
    }
}
