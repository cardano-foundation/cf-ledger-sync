package org.cardanofoundation.ledgersync.scheduler.service.offchain.committeederegistration;

import java.util.concurrent.ExecutorService;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainCommitteeDeregistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainCommitteeDeregFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainFetchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CommitteeDeregExtractFetchService extends
        OffChainFetchService<OffChainCommitteeDeregistration, OffChainFetchError, OffChainCommitteeDeregFetchResultDTO, CommitteeDeregistrationDTO> {

    final SchedulerProperties properties;

    public CommitteeDeregExtractFetchService(SchedulerProperties properties,
            @Qualifier("offChainExecutor") ExecutorService executor) {
        super(executor);
        this.properties = properties;
    }

    @Override
    public OffChainCommitteeDeregistration extractOffChainData(
            OffChainCommitteeDeregFetchResultDTO offChainFetchResult) {

        OffChainCommitteeDeregistration offChainCommitteeDeregData = new OffChainCommitteeDeregistration();

        OffChainCommitteeDeregistrationId offChainCommitteeDeregId = new OffChainCommitteeDeregistrationId(
                offChainFetchResult.getCommitteeDeregTxHash(), offChainFetchResult.getCommitteeDeregCertIndex());

        offChainCommitteeDeregData.setCommitteeDeregistrationId(offChainCommitteeDeregId);
        offChainCommitteeDeregData.setCommitteeDeregTxHash(offChainFetchResult.getCommitteeDeregTxHash());
        offChainCommitteeDeregData.setCommitteeDeregCertIndex(offChainFetchResult.getCommitteeDeregCertIndex());
        offChainCommitteeDeregData.setContent(offChainFetchResult.getRawData());
        offChainCommitteeDeregData
                .setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid()
                && (offChainFetchResult.getRetryCount() >= properties.getOffChainData().getRetryCount() - 1)) {
            offChainCommitteeDeregData.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainCommitteeDeregData
                    .setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainCommitteeDeregData;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainCommitteeDeregFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainFetchError = new OffChainFetchError();
    
        OffChainFetchErrorId offChainVoteFetchErrorId = new OffChainFetchErrorId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                GovOffchainType.COMMITTEE_DEREGISTRATION.getValue());

        offChainFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainFetchError.setType(GovOffchainType.COMMITTEE_DEREGISTRATION.getValue());
        offChainFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainFetchError;
    }

    @Override
    public OffChainCommitteeDeregFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData,
            CommitteeDeregistrationDTO anchor) {

        OffChainCommitteeDeregFetchResultDTO result = new OffChainCommitteeDeregFetchResultDTO(offChainAnchorData);
        result.setCommitteeDeregCertIndex(anchor.getCommitteeDeregCertIndex());
        result.setCommitteeDeregTxHash(anchor.getCommitteeDeregTxHash());
        return result;
    }
}
