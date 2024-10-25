package org.cardanofoundation.ledgersync.scheduler.service.offchain.gov_action;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorCpId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionCpId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainGovFetchResultDTO;
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
public class GovActionExtractFetchService extends
    OffChainFetchAbstractService<OffChainGovAction, OffChainFetchError, OffChainGovFetchResultDTO, GovAnchorDTO> {

    @Override
    public OffChainGovAction extractOffChainData(OffChainGovFetchResultDTO offChainFetchResult, Integer maxRetry) {
        OffChainGovAction offChainGovActionData = new OffChainGovAction();
        OffChainGovActionCpId offChainGovActionCpId = new OffChainGovActionCpId(
            offChainFetchResult.getGovActionTxHash(), offChainFetchResult.getGovActionIdx());

        offChainGovActionData.setCpId(offChainGovActionCpId);
        offChainGovActionData.setGovActionTxHash(offChainFetchResult.getGovActionTxHash());
        offChainGovActionData.setGovActionIdx(offChainFetchResult.getGovActionIdx());
        offChainGovActionData.setContent(offChainFetchResult.getRawData());
        offChainGovActionData.setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid() && (offChainFetchResult.getRetryCount() >= maxRetry - 1)) {
            offChainGovActionData.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainGovActionData.setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainGovActionData;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainGovFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainVoteFetchError = new OffChainFetchError();
        OffChainFetchErrorCpId offChainVoteFetchErrorId =
            new OffChainFetchErrorCpId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                TypeVote.GOV_ACTION);

        offChainVoteFetchError.setCpId(offChainVoteFetchErrorId);
        offChainVoteFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainVoteFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainVoteFetchError.setTypeVote(TypeVote.GOV_ACTION);
        offChainVoteFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainVoteFetchError;
    }

    @Override
    public OffChainGovFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData, GovAnchorDTO anchor) {
        OffChainGovFetchResultDTO result = new OffChainGovFetchResultDTO(offChainAnchorData);
        result.setGovActionIdx(anchor.getGovActionIdx());
        result.setGovActionTxHash(anchor.getGovActionTxHash());
        return result;
    }
}
