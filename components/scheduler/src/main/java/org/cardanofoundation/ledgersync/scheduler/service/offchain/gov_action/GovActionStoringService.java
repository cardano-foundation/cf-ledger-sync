package org.cardanofoundation.ledgersync.scheduler.service.offchain.gov_action;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorCpId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionCpId;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainStoringAbstractService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainFetchErrorStorage;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainGovActionStorage;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class GovActionStoringService extends
        OffChainStoringAbstractService<OffChainGovAction, OffChainFetchError> {

    private final OffChainFetchErrorStorage offChainFetchErrorStorage;
    private final OffChainGovActionStorage offChainGovActionStorage;

    @Override
    public void insertFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getCpId))
                .collect(Collectors.toList());

        Set<OffChainGovActionCpId> offChainGovActionIds = offChainAnchorData.stream()
                .map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        Set<OffChainGovActionCpId> existingOffChainVoteGovActionMapCpId = new HashSet<>(
                offChainGovActionStorage.findByCpIdIn(offChainGovActionIds))
                .stream().map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        List<OffChainGovAction> offChainGovActionDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainVoteGovActionMapCpId.contains(e.getCpId()))
                .collect(Collectors.toList());

        offChainGovActionStorage.saveAll(offChainGovActionDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getCpId))
                .collect(Collectors.toList());

        Set<OffChainGovActionCpId> offChainGovActionIds = offChainAnchorData.stream()
                .map(OffChainGovAction::getCpId)
                .collect(Collectors.toSet());

        Map<OffChainGovActionCpId, OffChainGovAction> mapEntityByCpId = offChainAnchorData.stream()
                .collect(Collectors.toMap(OffChainGovAction::getCpId, Function.identity()));

        Set<OffChainGovAction> offChainGovActionDataToSave = new HashSet<>(
                offChainGovActionStorage.findByCpIdIn(offChainGovActionIds));

        offChainGovActionDataToSave.forEach(e -> {
            OffChainGovAction oc = mapEntityByCpId.get(e.getCpId());
            if (oc != null) {
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

        offChainFetchErrorData = offChainFetchErrorData.stream()
                .filter(distinctByKey(OffChainFetchError::getCpId))
                .collect(Collectors.toList());

        Set<OffChainFetchErrorCpId> offChainFetchErrorCpId = offChainFetchErrorData.stream()
                .map(OffChainFetchError::getCpId)
                .collect(Collectors.toSet());

        Map<OffChainFetchErrorCpId, OffChainFetchError> existingOffChainVoteFetchErrorMap = offChainFetchErrorStorage
                .findByCpIdIn(offChainFetchErrorCpId).stream()
                .collect(Collectors.toMap(OffChainFetchError::getCpId, Function.identity()));

        offChainFetchErrorData.forEach(
                e -> {
                    OffChainFetchError existingOffChainVoteFetchError = existingOffChainVoteFetchErrorMap
                            .get(e.getCpId());
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
}
