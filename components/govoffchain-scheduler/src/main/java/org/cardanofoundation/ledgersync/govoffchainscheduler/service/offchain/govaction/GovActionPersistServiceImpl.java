package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.govaction;

import java.sql.Timestamp;
import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.governance.GovActionProposalRepo;
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
@Qualifier("govActionPersistServiceImpl")
public class GovActionPersistServiceImpl implements OffChainProcessPersistDataService {

    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final GovActionProposalRepo govActionProposalRepo;
    final GovActionExtractFetchService govActionExtractFetchService;
    final GovActionStoringService govActionStoringService;
    final EraRepo eraRepo;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(offChainDataCheckpointStorage, eraRepo,
                OffChainCheckpointType.GOV_ACTION);
        long currentSlotNo = govActionProposalRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());

        // Limit the amount of query data per job run
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<GovAnchorDTO> govList = govActionProposalRepo.getAnchorInfoBySlotRange(currentCheckpoint.getSlotNo(),
                currentSlotNo);

        govActionExtractFetchService.initOffChainListData();
        govActionExtractFetchService.crawlOffChainAnchors(govList);

        List<OffChainFetchError> offChainFetchErrors = govActionExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainGovAction> offChainDataList = govActionExtractFetchService.getOffChainAnchorsFetch();

        govActionStoringService.insertFetchData(offChainDataList);
        govActionStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching gov action metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
