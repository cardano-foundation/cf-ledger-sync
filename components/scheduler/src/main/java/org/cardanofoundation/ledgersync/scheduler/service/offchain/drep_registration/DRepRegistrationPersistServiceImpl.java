package org.cardanofoundation.ledgersync.scheduler.service.offchain.drep_registration;

import java.sql.Timestamp;
import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDRepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.DRepRegistrationRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDataCheckpointStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
@Qualifier("dRepRegistrationPersistServiceImpl")
public class DRepRegistrationPersistServiceImpl implements OffChainProcessPersistDataService {

    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final DRepRegistrationRepo dRepRegistrationRepo;
    final DRepRegistrationExtractFetchService dRepRegistrationExtractFetchService;
    final DRepRegistrationStoringService dRepRegistrationStoringService;
    final EraRepo eraRepo;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(offChainDataCheckpointStorage, eraRepo,
                OffChainCheckpointType.DREP_REGISTRATION);

        long currentSlotNo = dRepRegistrationRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<DRepRegistrationDTO> dRepRegistrationList = dRepRegistrationRepo.getAnchorInfoBySlotRange(
                currentCheckpoint.getSlotNo(), currentSlotNo);

        dRepRegistrationExtractFetchService.initOffChainListData();
        dRepRegistrationExtractFetchService.crawlOffChainAnchors(dRepRegistrationList);

        List<OffChainFetchError> offChainFetchErrors = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetchError();
        List<OffChainDRepRegistration> offChainDataList = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetch();

        dRepRegistrationStoringService.insertFetchData(offChainDataList);
        dRepRegistrationStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        currentCheckpoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching DRep Registration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
