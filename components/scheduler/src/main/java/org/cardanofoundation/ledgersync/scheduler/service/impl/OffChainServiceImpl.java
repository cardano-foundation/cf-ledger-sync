package org.cardanofoundation.ledgersync.scheduler.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainDataStoringService;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainGovActionStoreService;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.CommitteeDeregistrationRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.ConstitutionRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.DrepRegisRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.GovActionProposalRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDataCheckpointStorage;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainVotingDataStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class OffChainServiceImpl implements OffChainDataStoringService {

    @Value("${ledger-sync.scheduler.off-chain-data.retry-count}")
    private Integer MAX_RETRY;
    static final long MAX_TIME_QUERY = 432000L;
    final OffChainDataCheckpointStorage offChainDataCheckpointStorage;
    final OffChainVotingDataStorage offChainVotingDataStorage;
    final GovActionProposalRepo govActionProposalRepo;
    final OffChainGovActionStoreService offChainGovActionStoreService;
    final CommitteeDeregistrationRepo committeeDeregistrationRepo;
    final ConstitutionRepo constitutionRepo;
    final DrepRegisRepo drepRegisStorage;
    final EraRepo eraRepo;

    @Override
    public void validateAndPersistData() {
        long startTime = System.currentTimeMillis();
        log.info("Start validating off-chain data");

        processGovAction(startTime);
        processVotingData(startTime);
        processDRepRegistrationData(startTime);
        processConstitutionData(startTime);
        processCommitteeDeregData(startTime);

        log.info("End validating off-chain data, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

    private void processGovAction(long startTime) {
        OffChainDataCheckpoint currentCheckpoint = getCurrentCheckpoint(OffChainCheckpointType.GOV_ACTION);

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
            MAX_RETRY);

        offChainGovActionStoreService.insertFetchData(offChainVoteGovActionDataList);
        offChainGovActionStoreService.insertFetchFailData(offChainVoteFetchErrors);

        currentCheckpoint.setSlotNo(currentSlotNo);
        currentCheckpoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        offChainDataCheckpointStorage.save(currentCheckpoint);

        log.info("End fetching gov action metadata, taken time: {} ms", System.currentTimeMillis() - startTime);
    }

    private void processVotingData(long startTime) {
        // Todo
    }

    private void processDRepRegistrationData(long startTime) {
        // Todo
    }

    private void processConstitutionData(long startTime) {
        // Todo
    }

    private void processCommitteeDeregData(long startTime) {
        // Todo
    }

    private OffChainDataCheckpoint getCurrentCheckpoint(OffChainCheckpointType cpType) {
        Optional<OffChainDataCheckpoint> checkpoint = offChainDataCheckpointStorage.findFirstByType(cpType);
        if (checkpoint.isEmpty()) {
            Long starSlotAtEra = eraRepo.getStartSlotByEra(Era.CONWAY.getValue());
            return OffChainDataCheckpoint.builder()
                .slotNo(starSlotAtEra)
                .type(cpType)
                .build();
        }
        return checkpoint.get();
    }

}
