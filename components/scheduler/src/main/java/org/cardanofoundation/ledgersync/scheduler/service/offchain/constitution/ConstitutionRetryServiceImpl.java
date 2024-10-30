package org.cardanofoundation.ledgersync.scheduler.service.offchain.constitution;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainConstitutionStorage;
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
@Qualifier("constitutionRetryServiceImpl")
public class ConstitutionRetryServiceImpl implements OffChainProcessRetryDataService {

    final ConstitutionExtractFetchService constitutionExtractFetchService;
    final ConstitutionStoringService constitutionStoringService;
    final OffChainConstitutionStorage offChainConstitutionStorage;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        List<ConstitutionAnchorDTO> listOffChainGov = offChainConstitutionStorage.findByInvalid(CheckValid.INVALID);
        constitutionExtractFetchService.initOffChainListData();
        constitutionExtractFetchService.crawlOffChainAnchors(listOffChainGov);

        List<OffChainFetchError> offChainFetchErrors = constitutionExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainConstitution> offChainDataList = constitutionExtractFetchService.getOffChainAnchorsFetch();

        constitutionStoringService.updateFetchData(offChainDataList);
        constitutionStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch Constitution metadata, taken time: {} ms",
            System.currentTimeMillis() - startTime);
    }

}
