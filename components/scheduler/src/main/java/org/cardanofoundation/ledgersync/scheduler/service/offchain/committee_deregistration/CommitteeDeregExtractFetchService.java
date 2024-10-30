package org.cardanofoundation.ledgersync.scheduler.service.offchain.committee_deregistration;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainCommitteeDeregistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.CommitteeDeregistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainCommitteeDeregFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainFetchAbstractService;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class CommitteeDeregExtractFetchService extends
        OffChainFetchAbstractService<OffChainCommitteeDeregistration, OffChainFetchError, OffChainCommitteeDeregFetchResultDTO, CommitteeDeregistrationDTO> {

    final SchedulerProperties properties;

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
                TypeVote.COMMITTEE_DEREGISTRATION.getValue());

        offChainFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainFetchError.setType(TypeVote.COMMITTEE_DEREGISTRATION.getValue());
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
