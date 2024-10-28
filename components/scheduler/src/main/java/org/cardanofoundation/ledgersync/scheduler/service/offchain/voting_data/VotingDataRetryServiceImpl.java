package org.cardanofoundation.ledgersync.scheduler.service.offchain.voting_data;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainVotingDataStorage;
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
@Qualifier("votingDataRetryServiceImpl")
public class VotingDataRetryServiceImpl implements OffChainProcessRetryDataService {

    final VotingDataExtractFetchService votingDataExtractFetchService;
    final VotingDataStoringService votingDataStoringService;
    final OffChainVotingDataStorage offChainVotingDataStorage;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        List<VotingDataAnchorDTO> listOffChainGov = offChainVotingDataStorage.findByInvalid(CheckValid.INVALID);
        votingDataExtractFetchService.initOffChainListData();
        votingDataExtractFetchService.crawlOffChainAnchors(listOffChainGov);

        List<OffChainFetchError> offChainFetchErrors = votingDataExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainVotingData> offChainGovActionDataList = votingDataExtractFetchService.getOffChainAnchorsFetch();

        votingDataStoringService.updateFetchData(offChainGovActionDataList);
        votingDataStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch Voting procedure metadata, taken time: {} ms",
            System.currentTimeMillis() - startTime);
    }

}
