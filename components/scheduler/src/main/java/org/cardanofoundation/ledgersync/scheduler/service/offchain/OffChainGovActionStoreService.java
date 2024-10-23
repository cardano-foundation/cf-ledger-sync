package org.cardanofoundation.ledgersync.scheduler.service.offchain;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.GovActionProposalEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.GovActionProposalId;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.GovActionProposalAnchorCpId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorCpId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionCpId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.GovAnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainGovFetchResultDTO;
import org.cardanofoundation.ledgersync.scheduler.storage.governance.GovActionProposalRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainFetchErrorStorage;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainGovActionStorage;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class OffChainGovActionStoreService extends
    OffChainStoringService<OffChainGovAction, OffChainFetchError, OffChainGovFetchResultDTO, GovAnchorDTO> {

    private final OffChainFetchErrorStorage offChainFetchErrorStorage;
    private final OffChainGovActionStorage offChainGovActionStorage;
    private final GovActionProposalRepo govActionProposalRepo;

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
    public void insertFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData =
            offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getCpId))
                .collect(Collectors.toList());

        Set<OffChainGovActionCpId> offChainGovActionIds =
            offChainAnchorData.stream()
                .map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        Set<OffChainGovActionCpId> existingOffChainVoteGovActionMapCpId =
            new HashSet<>(offChainGovActionStorage.findByCpIdIn(offChainGovActionIds))
                .stream().map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        List<OffChainGovAction> offChainGovActionDataToSave =
            offChainAnchorData.stream()
                .filter(e -> !existingOffChainVoteGovActionMapCpId.contains(e.getCpId()))
                .collect(Collectors.toList());

        offChainGovActionStorage.saveAll(offChainGovActionDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData =
            offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getCpId))
                .collect(Collectors.toList());

        Set<OffChainGovActionCpId> offChainGovActionIds =
            offChainAnchorData.stream()
                .map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        Map<OffChainGovActionCpId, OffChainGovAction> mapEntityByCpId = offChainAnchorData.stream()
            .collect(Collectors.toMap(OffChainGovAction::getCpId, Function.identity()));

        Set<OffChainGovAction> offChainGovActionDataToSave =
            new HashSet<>(offChainGovActionStorage.findByCpIdIn(offChainGovActionIds));

        offChainGovActionDataToSave.forEach(e -> {
            OffChainGovAction oc = mapEntityByCpId.get(e.getCpId());
            if (oc != null){
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainGovActionStorage.saveAll(offChainGovActionDataToSave);
    }

    @Override
    public void insertFetchFailData(Collection<OffChainFetchError> offChainFetchErrorData) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        offChainFetchErrorData =
            offChainFetchErrorData.stream()
                .filter(distinctByKey(OffChainFetchError::getCpId))
                .collect(Collectors.toList());

        Set<OffChainFetchErrorCpId> offChainFetchErrorCpId =
            offChainFetchErrorData.stream()
                .map(OffChainFetchError::getCpId)
                .collect(Collectors.toSet());

        Map<OffChainFetchErrorCpId, OffChainFetchError> existingOffChainVoteFetchErrorMap =
            offChainFetchErrorStorage.findByCpIdIn(offChainFetchErrorCpId).stream()
                .collect(Collectors.toMap(OffChainFetchError::getCpId, Function.identity()));

        offChainFetchErrorData.forEach(
            e -> {
                OffChainFetchError existingOffChainVoteFetchError =
                    existingOffChainVoteFetchErrorMap.get(e.getCpId());
                if (existingOffChainVoteFetchError != null) {
                    e.setId(existingOffChainVoteFetchError.getId());
                    e.setRetryCount(
                        existingOffChainVoteFetchError.getRetryCount() + 1);
                    e.setFetchTime(currentTime);
                } else {
                    e.setRetryCount(1);
                    e.setFetchTime(currentTime);
                }
            });

        offChainFetchErrorStorage.saveAll(offChainFetchErrorData);
    }

    @Override
    public OffChainGovFetchResultDTO addIdKey(OffChainFetchResultDTO offChainAnchorData, GovAnchorDTO anchor) {
        OffChainGovFetchResultDTO result = new OffChainGovFetchResultDTO(offChainAnchorData);
        result.setGovActionIdx(anchor.getGovActionIdx());
        result.setGovActionTxHash(anchor.getGovActionTxHash());
        return result;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
