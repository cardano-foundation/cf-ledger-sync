package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.govaction;

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
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionId;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainStoringService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainFetchErrorStorage;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainGovActionStorage;
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
        OffChainStoringService<OffChainGovAction, OffChainFetchError> {

    final GovOffChainSchedulerProperties properties;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainGovActionStorage offChainGovActionStorage;

    @Override
    public void insertFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getGovActionId))
                .collect(Collectors.toList());

        Set<OffChainGovActionId> offChainGovActionIds = offChainAnchorData.stream()
                .map(OffChainGovAction::getGovActionId)
                .collect(Collectors.toSet());

        Set<OffChainGovActionId> existingOffChainVoteGovActionIds = new HashSet<>(
                offChainGovActionStorage.findByGovActionIdIn(offChainGovActionIds))
                .stream().map(OffChainGovAction::getGovActionId)
                .collect(Collectors.toSet());

        List<OffChainGovAction> offChainDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainVoteGovActionIds.contains(e.getGovActionId()))
                .collect(Collectors.toList());

        offChainGovActionStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainGovAction> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainGovAction::getGovActionId))
                .collect(Collectors.toList());

        Set<OffChainGovActionId> offChainGovActionIds = offChainAnchorData.stream()
                .map(OffChainGovAction::getGovActionId)
                .collect(Collectors.toSet());

        Map<OffChainGovActionId, OffChainGovAction> mapEntityById = offChainAnchorData.stream()
                .collect(Collectors.toMap(OffChainGovAction::getGovActionId, Function.identity()));

        Set<OffChainGovAction> offChainDataToSave = new HashSet<>(
                offChainGovActionStorage.findByGovActionIdIn(offChainGovActionIds));

        offChainDataToSave.forEach(e -> {
            OffChainGovAction oc = mapEntityById.get(e.getGovActionId());
            if (oc != null) {
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainGovActionStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void insertFetchFailData(Collection<OffChainFetchError> offChainFetchErrorData) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        offChainFetchErrorData = offChainFetchErrorData.stream()
                .filter(distinctByKey(OffChainFetchError::getOffChainFetchErrorId))
                .collect(Collectors.toList());

        Set<OffChainFetchErrorId> offChainFetchErrorId = offChainFetchErrorData.stream()
                .map(OffChainFetchError::getOffChainFetchErrorId)
                .collect(Collectors.toSet());

        Map<OffChainFetchErrorId, OffChainFetchError> existingOffChainVoteFetchErrorMap = offChainFetchErrorStorage
                .findByOffChainFetchErrorIdIn(offChainFetchErrorId).stream()
                .collect(Collectors.toMap(OffChainFetchError::getOffChainFetchErrorId, Function.identity()));

        offChainFetchErrorData.forEach(
                e -> {
                    OffChainFetchError existingOffChainVoteFetchError = existingOffChainVoteFetchErrorMap
                            .get(e.getOffChainFetchErrorId());
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

        List<OffChainFetchError> filterDataNotExpired = offChainFetchErrorData.stream()
            .filter(e -> e.getRetryCount() <= properties.getOffChainData().getRetryCount())
            .collect(Collectors.toList());

        offChainFetchErrorStorage.saveAll(filterDataNotExpired);
    }
}
