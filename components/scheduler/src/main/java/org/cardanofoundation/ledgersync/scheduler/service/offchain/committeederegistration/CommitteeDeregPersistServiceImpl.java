package org.cardanofoundation.ledgersync.scheduler.service.offchain.committeederegistration;

import java.sql.Timestamp;
import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.CommitteeDeregistrationRepo;
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
@Qualifier("committeeDeregPersistServiceImpl")
public class CommitteeDeregPersistServiceImpl implements OffChainProcessPersistDataService {

    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final CommitteeDeregistrationRepo committeeDeregistrationRepo;
    final CommitteeDeregExtractFetchService committeeDeregExtractFetchService;
    final CommitteeDeregStoringService committeeDeregStoringService;
    final EraRepo eraRepo;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(offChainDataCheckpointStorage, eraRepo,
                OffChainCheckpointType.COMMITTEE_DEREGISTRATION);
        long currentSlotNo = committeeDeregistrationRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<CommitteeDeregistrationDTO> committeeDeregList = committeeDeregistrationRepo.getAnchorInfoBySlotRange(
                currentCheckpoint.getSlotNo(), currentSlotNo);

        committeeDeregExtractFetchService.initOffChainListData();
        committeeDeregExtractFetchService.crawlOffChainAnchors(committeeDeregList);

        List<OffChainFetchError> offChainFetchErrors = committeeDeregExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainCommitteeDeregistration> offChainDataList = committeeDeregExtractFetchService
                .getOffChainAnchorsFetch();

        committeeDeregStoringService.insertFetchData(offChainDataList);
        committeeDeregStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        currentCheckpoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching Committee Deregistration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
