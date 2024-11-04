package org.cardanofoundation.ledgersync.scheduler.service.offchain.govaction;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainGovActionStorage;
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
@Qualifier("govActionRetryServiceImpl")
public class GovActionRetryServiceImpl implements OffChainProcessRetryDataService {

    final GovActionExtractFetchService govActionExtractFetchService;
    final GovActionStoringService govActionStoringService;
    final OffChainGovActionStorage offChainGovActionStorage;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        List<GovAnchorDTO> listOffChainGov = offChainGovActionStorage.findByInvalid(CheckValid.INVALID);
        govActionExtractFetchService.initOffChainListData();
        govActionExtractFetchService.crawlOffChainAnchors(listOffChainGov);

        List<OffChainFetchError> offChainFetchErrors = govActionExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainGovAction> offChainDataList = govActionExtractFetchService.getOffChainAnchorsFetch();

        govActionStoringService.updateFetchData(offChainDataList);
        govActionStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch gov-action metadata, taken time: {} ms",
            System.currentTimeMillis() - startTime);
    }

}
