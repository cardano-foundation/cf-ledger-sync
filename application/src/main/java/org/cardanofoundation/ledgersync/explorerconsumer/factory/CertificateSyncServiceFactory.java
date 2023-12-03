package org.cardanofoundation.ledgersync.explorerconsumer.factory;

import com.bloxbean.cardano.yaci.core.model.certs.*;
import jakarta.annotation.PostConstruct;
import org.cardanofoundation.explorer.consumercommon.entity.Delegation;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CertificateSyncService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CertificateSyncServiceFactory extends
        AbstractServiceFactory<CertificateSyncService<? extends Certificate>, CertificateSyncService> {// NOSONAR


    public CertificateSyncServiceFactory(
            List<CertificateSyncService<? extends Certificate>> certificateSyncServices) {
        super(certificateSyncServices);
    }

    @PostConstruct
    void init() {
        serviceMap = services.stream()
                .collect(
                        Collectors.toMap(
                                CertificateSyncService::supports,
                                Function.identity()));

    }

    @SuppressWarnings("unchecked")
    public void handle(AggregatedBlock aggregatedBlock,
                       Certificate certificate, int certificateIdx, Tx tx, Redeemer redeemer,
                       Map<String, StakeAddress> stakeAddressMap) {
        if (serviceMap.get(certificate.getClass()) == null) {
            return;
        }
        serviceMap.get(certificate.getClass()).handle(
                aggregatedBlock, certificate, certificateIdx, tx,
                redeemer, stakeAddressMap);
    }
}

