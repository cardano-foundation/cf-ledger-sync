package org.cardanofoundation.ledgersync.scheduler.service.impl;

import java.util.Optional;

import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainPersistService;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.ProcessService;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDataCheckpointStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class OffChainPersistServiceImpl implements OffChainPersistService {

    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;

    final ProcessService govActionPersistService;
    final EraRepo eraRepo;

    public OffChainPersistServiceImpl(
        OffChainDataCheckpointStorage offChainDataCheckpointStorage,
        @Qualifier("govActionPersistService") ProcessService govActionPersistService,
        EraRepo eraRepo
    ) {
        this.offChainDataCheckpointStorage = offChainDataCheckpointStorage;
        this.govActionPersistService = govActionPersistService;
        this.eraRepo = eraRepo;
    }

    @Override
    public void validateAndPersistData() {
        long startTime = System.currentTimeMillis();
        log.info("Start validating off-chain data");

        govActionPersistService.process(startTime, getCurrentCheckpoint(OffChainCheckpointType.GOV_ACTION));
//        processVotingData(startTime);
//        processDRepRegistrationData(startTime);
//        processConstitutionData(startTime);
//        processCommitteeDeregData(startTime);

        log.info("End validating off-chain data, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

    private OffChainDataCheckpoint getCurrentCheckpoint(OffChainCheckpointType cpType) {
        Optional<OffChainDataCheckpoint> checkpoint = offChainDataCheckpointStorage.findFirstByType(cpType);
        if (checkpoint.isEmpty()) {
            Long starSlotAtEra = eraRepo.getStartSlotByEra(Era.CONWAY.getValue());
            return OffChainDataCheckpoint.builder().slotNo(starSlotAtEra).type(cpType).build();
        }
        return checkpoint.get();
    }

}
