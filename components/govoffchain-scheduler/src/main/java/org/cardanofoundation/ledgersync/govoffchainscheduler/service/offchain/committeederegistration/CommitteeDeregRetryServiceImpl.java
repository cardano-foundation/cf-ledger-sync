package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.committeederegistration;

import java.util.List;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainProcessRetryDataService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainCommitteeDeregStorage;
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
@Qualifier("committeeDeregRetryServiceImpl")
public class CommitteeDeregRetryServiceImpl implements OffChainProcessRetryDataService {

    final CommitteeDeregExtractFetchService committeeDeregExtractFetchService;
    final CommitteeDeregStoringService committeeDeregStoringService;
    final OffChainCommitteeDeregStorage offChainCommitteeDeregStorage;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();

        List<CommitteeDeregistrationDTO> listOffChainCommitteeDereg = offChainCommitteeDeregStorage
                .findByInvalid(CheckValid.INVALID, GovOffchainType.COMMITTEE_DEREGISTRATION.getValue());

        committeeDeregExtractFetchService.initOffChainListData();
        committeeDeregExtractFetchService.crawlOffChainAnchors(listOffChainCommitteeDereg);

        List<OffChainFetchError> offChainFetchErrors = committeeDeregExtractFetchService.getOffChainAnchorsFetchError();
        List<OffChainCommitteeDeregistration> offChainDataList = committeeDeregExtractFetchService
                .getOffChainAnchorsFetch();

        committeeDeregStoringService.updateFetchData(offChainDataList);
        committeeDeregStoringService.insertFetchFailData(offChainFetchErrors);

        log.info("End retrying to fetch Committee Deregistration metadata, taken time: {} ms",
                System.currentTimeMillis() - startTime);
    }

}
