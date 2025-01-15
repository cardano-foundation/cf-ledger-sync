package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.govaction;

import java.util.concurrent.ExecutorService;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainGovFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainFetchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class GovActionExtractFetchService extends
        OffChainFetchService<OffChainGovAction, OffChainFetchError, OffChainGovFetchResultDTO, GovAnchorDTO> {

    final GovOffChainSchedulerProperties properties;

    public GovActionExtractFetchService(GovOffChainSchedulerProperties properties,
            @Qualifier("offChainExecutor") ExecutorService executor) {
        super(executor);
        this.properties = properties;
    }

    @Override
    public OffChainGovAction extractOffChainData(OffChainGovFetchResultDTO offChainFetchResult) {
        OffChainGovAction offChainGovActionData = new OffChainGovAction();
        OffChainGovActionId offChainGovActionId = new OffChainGovActionId(
                offChainFetchResult.getGovActionTxHash(), offChainFetchResult.getGovActionIdx());

        offChainGovActionData.setGovActionId(offChainGovActionId);
        offChainGovActionData.setGovActionTxHash(offChainFetchResult.getGovActionTxHash());
        offChainGovActionData.setGovActionIdx(offChainFetchResult.getGovActionIdx());
        offChainGovActionData.setContent(offChainFetchResult.getRawData());
        offChainGovActionData.setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid()
                && (offChainFetchResult.getRetryCount() >= properties.getOffChainData().getRetryCount() - 1)) {
            offChainGovActionData.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainGovActionData.setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainGovActionData;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainGovFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainFetchError = new OffChainFetchError();
        OffChainFetchErrorId offChainVoteFetchErrorId = new OffChainFetchErrorId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                GovOffchainType.GOV_ACTION.getValue());

        offChainFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainFetchError.setType(GovOffchainType.GOV_ACTION.getValue());
        offChainFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainFetchError;
    }

    @Override
    public OffChainGovFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData, GovAnchorDTO anchor) {
        OffChainGovFetchResultDTO result = new OffChainGovFetchResultDTO(offChainAnchorData);
        result.setGovActionIdx(anchor.getGovActionIdx());
        result.setGovActionTxHash(anchor.getGovActionTxHash());
        return result;
    }
}
