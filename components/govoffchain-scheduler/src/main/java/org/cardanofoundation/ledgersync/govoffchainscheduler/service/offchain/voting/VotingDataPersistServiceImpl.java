package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.voting;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.VotingDataAnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.governance.VotingProcedureRepo;
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
@Qualifier("votingDataPersistServiceImpl")
public class VotingDataPersistServiceImpl implements OffChainProcessPersistDataService {

    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final VotingProcedureRepo votingProcedureRepo;
    final VotingDataExtractFetchService votingDataExtractFetchService;
    final VotingDataStoringService votingDataStoringService;
    final EraRepo eraRepo;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(offChainDataCheckpointStorage, eraRepo,
                OffChainCheckpointType.VOTING);
        long currentSlotNo = votingProcedureRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());

        // Limit the amount of query data per job run
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<VotingDataAnchorDTO> votingList = votingProcedureRepo.getAnchorInfoBySlotRange(
                currentCheckpoint.getSlotNo(),
                currentSlotNo);

        votingDataExtractFetchService.initOffChainListData();
        votingDataExtractFetchService.crawlOffChainAnchors(votingList);

        List<OffChainFetchError> offChainFetchErrors = votingDataExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainVotingData> offChainDataList = votingDataExtractFetchService.getOffChainAnchorsFetch();

        votingDataStoringService.insertFetchData(offChainDataList);
        votingDataStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching Voting procedure metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

}
