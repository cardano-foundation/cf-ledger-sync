package org.cardanofoundation.ledgersync.scheduler.service.impl;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainGovActionStoreService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainGovActionStorage;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class OffChainRetryDataErrorServiceImpl implements OffChainRetryDataErrorService {

    static final Integer MAX_RETRY = 10;
    final OffChainGovActionStoreService offChainGovActionStoreService;
    final OffChainGovActionStorage offChainGovActionStorage;

    @Override
    public void retryOffChainErrorData() {
        long startTime = System.currentTimeMillis();
        log.info("Start retry error offchain data");
        retryOffChainGovErrorData(startTime);

        log.info("End retry error offchain data time taken: {} ms", System.currentTimeMillis() - startTime);
    }

    private void retryOffChainGovErrorData(long startTime) {
        List<GovAnchorDTO> listOffChainGov = offChainGovActionStorage.findByInvalid(CheckValid.INVALID);
        offChainGovActionStoreService.initOffChainListData();
        offChainGovActionStoreService.crawlOffChainAnchors(listOffChainGov);

        List<OffChainFetchError> offChainVoteFetchErrors = offChainGovActionStoreService.getOffChainAnchorsFetchError();
        List<OffChainGovAction> offChainVoteGovActionDataList = offChainGovActionStoreService.getOffChainAnchorsFetch(
            MAX_RETRY);

        offChainGovActionStoreService.updateFetchData(offChainVoteGovActionDataList);
        offChainGovActionStoreService.insertFetchFailData(offChainVoteFetchErrors);

        log.info("End retrying to fetch gov-action metadata, taken time: {} ms",
            System.currentTimeMillis() - startTime);
    }

}
