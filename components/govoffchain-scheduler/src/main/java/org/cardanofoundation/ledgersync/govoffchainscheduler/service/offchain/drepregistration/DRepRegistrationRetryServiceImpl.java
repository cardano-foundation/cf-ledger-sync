package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.drepregistration;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDrepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainDRepRegistrationStorage;
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
                .findByInvalid(CheckValid.INVALID, GovOffchainType.DREP_REGISTRATION.getValue());

        dRepRegistrationExtractFetchService.initOffChainListData();
        dRepRegistrationExtractFetchService.crawlOffChainAnchors(listOffChainDRepRegistration);

        List<OffChainFetchError> offChainFetchErrors = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetchError();
        List<OffChainDrepRegistration> offChainDataList = dRepRegistrationExtractFetchService
                .getOffChainAnchorsFetch();

        dRepRegistrationStoringService.updateFetchData(offChainDataList);
        dRepRegistrationStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch DRep Registration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
