package org.cardanofoundation.ledgersync.scheduler.service.offchain.voting_data;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.VotingDataAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainVotingFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainFetchAbstractService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class VotingDataExtractFetchService extends
        OffChainFetchAbstractService<OffChainVotingData, OffChainFetchError, OffChainVotingFetchResultDTO, VotingDataAnchorDTO> {

    @Value("${ledger-sync.scheduler.off-chain-data.retry-count}")
    private Integer maxRetry;

    @Override
    public OffChainVotingData extractOffChainData(OffChainVotingFetchResultDTO offChainFetchResult) {
        OffChainVotingData offChainVotingData = new OffChainVotingData();

        offChainVotingData.setVotingProcedureId(offChainFetchResult.getVotingProcedureId());
        offChainVotingData.setContent(offChainFetchResult.getRawData());
        offChainVotingData.setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid() && (offChainFetchResult.getRetryCount() >= maxRetry - 1)) {
            offChainVotingData.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainVotingData.setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainVotingData;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainVotingFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainVoteFetchError = new OffChainFetchError();
        OffChainFetchErrorId offChainVoteFetchErrorId = new OffChainFetchErrorId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                TypeVote.VOTING.getValue());

        offChainVoteFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainVoteFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainVoteFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainVoteFetchError.setType(TypeVote.VOTING.getValue());
        offChainVoteFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainVoteFetchError;
    }

    @Override
    public OffChainVotingFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData, VotingDataAnchorDTO anchor) {
        OffChainVotingFetchResultDTO result = new OffChainVotingFetchResultDTO(offChainAnchorData);
        result.setVotingProcedureId(anchor.getVotingProcedureId());
        return result;
    }
}
