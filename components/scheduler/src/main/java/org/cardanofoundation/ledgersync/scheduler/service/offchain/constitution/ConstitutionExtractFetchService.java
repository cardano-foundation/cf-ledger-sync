package org.cardanofoundation.ledgersync.scheduler.service.offchain.constitution;

import java.util.concurrent.ExecutorService;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.GovOffchainType;
import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.ConstitutionAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainConstitutionFetchResultDTO;
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
public class ConstitutionExtractFetchService extends
        OffChainFetchService<OffChainConstitution, OffChainFetchError, OffChainConstitutionFetchResultDTO, ConstitutionAnchorDTO> {

    final SchedulerProperties properties;

    public ConstitutionExtractFetchService(SchedulerProperties properties,
            @Qualifier("offChainExecutor") ExecutorService executor) {
        super(executor);
        this.properties = properties;
    }

    @Override
    public OffChainConstitution extractOffChainData(OffChainConstitutionFetchResultDTO offChainFetchResult) {
        OffChainConstitution offChainConstitution = new OffChainConstitution();

        offChainConstitution.setConstitutionActiveEpoch(offChainFetchResult.getConstitutionActiveEpoch());
        offChainConstitution.setContent(offChainFetchResult.getRawData());
        offChainConstitution.setValidAtSlot(offChainFetchResult.isValid() ? offChainFetchResult.getSlotNo() : null);

        if (!offChainFetchResult.isValid()
                && (offChainFetchResult.getRetryCount() >= properties.getOffChainData().getRetryCount() - 1)) {
            offChainConstitution.setCheckValid(CheckValid.EXPIRED);
        } else {
            offChainConstitution.setCheckValid(offChainFetchResult.isValid() ? CheckValid.VALID : CheckValid.INVALID);
        }

        return offChainConstitution;
    }

    @Override
    public OffChainFetchError extractFetchError(OffChainConstitutionFetchResultDTO offChainAnchorData) {
        OffChainFetchError offChainVoteFetchError = new OffChainFetchError();
        OffChainFetchErrorId offChainVoteFetchErrorId = new OffChainFetchErrorId(
                offChainAnchorData.getAnchorUrl(),
                offChainAnchorData.getAnchorHash(),
                GovOffchainType.CONSTITUTION.getValue());

        offChainVoteFetchError.setOffChainFetchErrorId(offChainVoteFetchErrorId);
        offChainVoteFetchError.setAnchorUrl(offChainAnchorData.getAnchorUrl());
        offChainVoteFetchError.setAnchorHash(offChainAnchorData.getAnchorHash());
        offChainVoteFetchError.setType(GovOffchainType.CONSTITUTION.getValue());
        offChainVoteFetchError.setFetchError(offChainAnchorData.getFetchFailError());
        return offChainVoteFetchError;
    }

    @Override
    public OffChainConstitutionFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData,
            ConstitutionAnchorDTO anchor) {
        OffChainConstitutionFetchResultDTO result = new OffChainConstitutionFetchResultDTO(offChainAnchorData);
        result.setConstitutionActiveEpoch(anchor.getConstitutionActiveEpoch());
        return result;
    }
}
