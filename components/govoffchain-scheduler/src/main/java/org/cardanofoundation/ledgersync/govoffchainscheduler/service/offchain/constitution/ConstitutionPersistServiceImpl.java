package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.constitution;

import java.sql.Timestamp;
import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.ConstitutionAnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.governance.ConstitutionRepo;
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
@Qualifier("constitutionPersistServiceImpl")
public class ConstitutionPersistServiceImpl implements OffChainProcessPersistDataService {

    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final ConstitutionRepo constitutionRepo;
    final ConstitutionExtractFetchService constitutionExtractFetchService;
    final ConstitutionStoringService constitutionStoringService;
    final EraRepo eraRepo;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(offChainDataCheckpointStorage, eraRepo,
                OffChainCheckpointType.CONSTITUTION);
        long currentSlotNo = constitutionRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());

        // Limit the amount of query data per job run
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<ConstitutionAnchorDTO> constitutionList = constitutionRepo.getAnchorInfoBySlotRange(
                currentCheckpoint.getSlotNo(),
                currentSlotNo);

        constitutionExtractFetchService.initOffChainListData();
        constitutionExtractFetchService.crawlOffChainAnchors(constitutionList);

        List<OffChainFetchError> offChainFetchErrors = constitutionExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainConstitution> offChainDataList = constitutionExtractFetchService.getOffChainAnchorsFetch();

        constitutionStoringService.insertFetchData(offChainDataList);
        constitutionStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching Constitution metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
