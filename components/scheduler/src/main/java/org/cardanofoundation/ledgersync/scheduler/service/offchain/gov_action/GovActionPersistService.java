package org.cardanofoundation.ledgersync.scheduler.service.offchain.gov_action;

import java.sql.Timestamp;
import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.ProcessService;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.GovActionProposalRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDataCheckpointStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
@Qualifier("govActionPersistService")
public class GovActionPersistService implements ProcessService {

    @Value("${ledger-sync.scheduler.off-chain-data.retry-count}")
    private Integer maxRetry;
    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final GovActionProposalRepo govActionProposalRepo;
    final GovActionExtractFetchService offChainGovActionStoreService;
    final GovActionStoringService govActionStoringService;

    @Override
    public void process(long startTime, OffChainDataCheckpoint currentCheckpoint) {
        long currentSlotNo = govActionProposalRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<GovAnchorDTO> govList = govActionProposalRepo.getAnchorInfoBySlotRange(currentCheckpoint.getSlotNo(),
            currentSlotNo);

        offChainGovActionStoreService.initOffChainListData();
        offChainGovActionStoreService.crawlOffChainAnchors(govList);

        List<OffChainFetchError> offChainVoteFetchErrors = offChainGovActionStoreService.getOffChainAnchorsFetchError();
        List<OffChainGovAction> offChainVoteGovActionDataList = offChainGovActionStoreService.getOffChainAnchorsFetch(
            maxRetry);

        govActionStoringService.insertFetchData(offChainVoteGovActionDataList);
        govActionStoringService.insertFetchFailData(offChainVoteFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        currentCheckpoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching gov action metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
