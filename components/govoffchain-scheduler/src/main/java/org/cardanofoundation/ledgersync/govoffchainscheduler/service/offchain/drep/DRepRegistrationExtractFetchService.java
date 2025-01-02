package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.drep;

import java.util.concurrent.ExecutorService;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDrepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainDRepRegistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.DRepRegistrationDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainDRepRegistrationFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainFetchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class DRepRegistrationExtractFetchService extends
        OffChainFetchService<OffChainDrepRegistration, OffChainFetchError, OffChainDRepRegistrationFetchResultDTO, DRepRegistrationDTO> {

    final GovOffChainSchedulerProperties properties;

    public DRepRegistrationExtractFetchService(GovOffChainSchedulerProperties properties,
            @Qualifier("offChainExecutor") ExecutorService executor) {
        super(executor);
        this.properties = properties;
    }

    @Override
    public OffChainDrepRegistration extractOffChainData(
            OffChainDRepRegistrationFetchResultDTO offChainFetchResult) {

        OffChainDrepRegistration offChainDrepRegistrationData = new OffChainDrepRegistration();

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
                GovOffchainType.DREP_REGISTRATION.getValue());

        offChainFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainFetchError.setType(GovOffchainType.DREP_REGISTRATION.getValue());
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
