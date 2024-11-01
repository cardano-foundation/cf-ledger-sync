package org.cardanofoundation.ledgersync.scheduler.service.offchain.drep_registration;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDRepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDRepRegistrationStorage;
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
@Qualifier("dRepRegistrationRetryServiceImpl")
public class DRepRegistrationRetryServiceImpl implements OffChainProcessRetryDataService {

    final DRepRegistrationExtractFetchService dRepRegistrationExtractFetchService;
    final DRepRegistrationStoringService dRepRegistrationStoringService;
    final OffChainDRepRegistrationStorage offChainDRepRegistrationStorage;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        List<DRepRegistrationDTO> listOffChainDRepRegistration = offChainDRepRegistrationStorage
                .findByInvalid(CheckValid.INVALID);

        dRepRegistrationExtractFetchService.initOffChainListData();
        dRepRegistrationExtractFetchService.crawlOffChainAnchors(listOffChainDRepRegistration);

        List<OffChainFetchError> offChainFetchErrors = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetchError();
        List<OffChainDRepRegistration> offChainDataList = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetch();

        dRepRegistrationStoringService.updateFetchData(offChainDataList);
        dRepRegistrationStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch DRep Registration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
