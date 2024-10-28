package org.cardanofoundation.ledgersync.scheduler.service.offchain.voting_data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessPersistDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.VotingProcedureRepo;
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

        OffChainDataCheckpoint currentCheckpoint =  getCurrentCheckpoint(OffChainCheckpointType.VOTING);
        long currentSlotNo = votingProcedureRepo.maxSlotNo().orElse(currentCheckpoint.getSlotNo());
        if (currentSlotNo - currentCheckpoint.getSlotNo() > MAX_TIME_QUERY) {
            currentSlotNo = currentCheckpoint.getSlotNo() + MAX_TIME_QUERY;
        }

        List<VotingDataAnchorDTO> votingList = votingProcedureRepo.getAnchorInfoBySlotRange(currentCheckpoint.getSlotNo(),
            currentSlotNo);

        votingDataExtractFetchService.initOffChainListData();
        votingDataExtractFetchService.crawlOffChainAnchors(votingList);

        List<OffChainFetchError> offChainFetchErrors = votingDataExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainVotingData> offChainDataList = votingDataExtractFetchService.getOffChainAnchorsFetch();

        votingDataStoringService.insertFetchData(offChainDataList);
        votingDataStoringService.insertFetchFailData(offChainFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        currentCheckpoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching Voting procedure metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
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
