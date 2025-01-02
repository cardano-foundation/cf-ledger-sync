package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.voting;

import java.util.concurrent.ExecutorService;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.VotingDataAnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainVotingFetchResultDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainFetchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VotingDataExtractFetchService extends
        OffChainFetchService<OffChainVotingData, OffChainFetchError, OffChainVotingFetchResultDTO, VotingDataAnchorDTO> {

    final GovOffChainSchedulerProperties properties;

    public VotingDataExtractFetchService(GovOffChainSchedulerProperties properties,
            @Qualifier("offChainExecutor") ExecutorService executor) {
        super(executor);
        this.properties = properties;
    }

    @Override
    public OffChainVotingData extractOffChainData(OffChainVotingFetchResultDTO offChainFetchResult) {
        OffChainVotingData offChainVotingData = new OffChainVotingData();

        offChainVotingData.setVotingProcedureId(offChainFetchResult.getVotingProcedureId());
        offChainVotingData.setContent(offChainFetchResult.getRawData());
        offChainVotingData.setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid()
                && (offChainFetchResult.getRetryCount() >= properties.getOffChainData().getRetryCount() - 1)) {
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
                GovOffchainType.VOTING.getValue());

        offChainVoteFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainVoteFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainVoteFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainVoteFetchError.setType(GovOffchainType.VOTING.getValue());
        offChainVoteFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainVoteFetchError;
    }

    @Override
    public OffChainVotingFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData,
            VotingDataAnchorDTO anchor) {
        OffChainVotingFetchResultDTO result = new OffChainVotingFetchResultDTO(offChainAnchorData);
        result.setVotingProcedureId(anchor.getVotingProcedureId());
        return result;
    }
}
