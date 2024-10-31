package org.cardanofoundation.ledgersync.scheduler.service.offchain.drep_registration;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDRepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainDRepRegistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainDRepRegistrationFetchResultDTO;
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
public class DRepRegistrationExtractFetchService extends
        OffChainFetchAbstractService<OffChainDRepRegistration, OffChainFetchError, OffChainDRepRegistrationFetchResultDTO, DRepRegistrationDTO> {

    final SchedulerProperties properties;

    @Override
    public OffChainDRepRegistration extractOffChainData(
            OffChainDRepRegistrationFetchResultDTO offChainFetchResult) {

        OffChainDRepRegistration offChainDrepRegistrationData = new OffChainDRepRegistration();

        OffChainDRepRegistrationId offChainDrepRegistrationId = new OffChainDRepRegistrationId(
                offChainFetchResult.getDrepRegTxHash(), offChainFetchResult.getDrepRegCertIndex());

        offChainDrepRegistrationData.setDrepRegistrationId(offChainDrepRegistrationId);
        offChainDrepRegistrationData.setDrepRegTxHash(offChainFetchResult.getDrepRegTxHash());
        offChainDrepRegistrationData.setDrepRegCertIndex(offChainFetchResult.getDrepRegCertIndex());
        offChainDrepRegistrationData.setContent(offChainFetchResult.getRawData());
        offChainDrepRegistrationData
                .setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid()
                && (offChainFetchResult.getRetryCount() >= properties.getOffChainData().getRetryCount() - 1)) {
            offChainDrepRegistrationData.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainDrepRegistrationData
                    .setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainDrepRegistrationData;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainDRepRegistrationFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainFetchError = new OffChainFetchError();

        OffChainFetchErrorId offChainVoteFetchErrorId = new OffChainFetchErrorId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                TypeVote.DREP_REGISTRATION.getValue());

        offChainFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainFetchError.setType(TypeVote.DREP_REGISTRATION.getValue());
        offChainFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainFetchError;
    }

    @Override
    public OffChainDRepRegistrationFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData,
            DRepRegistrationDTO anchor) {

        OffChainDRepRegistrationFetchResultDTO result = new OffChainDRepRegistrationFetchResultDTO(offChainAnchorData);
        result.setDrepRegCertIndex(anchor.getDrepRegCertIndex());
        result.setDrepRegTxHash(anchor.getDrepRegTxHash());
        return result;
    }
}
