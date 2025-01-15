package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.drep;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDrepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.governance.DRepRegistrationRepo;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainDataCheckpointStorage;
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

        // Limit the amount of query data per job run
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<DRepRegistrationDTO> dRepRegistrationList = dRepRegistrationRepo.getAnchorInfoBySlotRange(
                currentCheckpoint.getSlotNo(), currentSlotNo);

        dRepRegistrationExtractFetchService.initOffChainListData();
        dRepRegistrationExtractFetchService.crawlOffChainAnchors(dRepRegistrationList);

        List<OffChainFetchError> offChainFetchErrors = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetchError();
        List<OffChainDrepRegistration> offChainDataList = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetch();

        dRepRegistrationStoringService.insertFetchData(offChainDataList);
        dRepRegistrationStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching DRep Registration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
