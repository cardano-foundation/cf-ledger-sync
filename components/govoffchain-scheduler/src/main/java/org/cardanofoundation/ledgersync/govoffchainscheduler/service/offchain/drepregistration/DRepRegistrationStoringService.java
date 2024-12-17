package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.drepregistration;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDrepRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainDRepRegistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainStoringService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainDRepRegistrationStorage;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainFetchErrorStorage;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class DRepRegistrationStoringService extends
        OffChainStoringService<OffChainDrepRegistration, OffChainFetchError> {

    final GovOffChainSchedulerProperties properties;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainDRepRegistrationStorage offChainDRepRegistrationStorage;

    @Override
    public void insertFetchData(Collection<OffChainDrepRegistration> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainDrepRegistration::getDrepRegistrationId))
                .collect(Collectors.toList());

        Set<OffChainDRepRegistrationId> offChainDRepRegistrationIds = offChainAnchorData.stream()
                .map(OffChainDrepRegistration::getDrepRegistrationId)
                .collect(Collectors.toSet());

        Set<OffChainDRepRegistrationId> existingOffChainDRepRegistrationIds = new HashSet<>(
                offChainDRepRegistrationStorage.findByDrepRegistrationIdIn(offChainDRepRegistrationIds))
                .stream().map(OffChainDrepRegistration::getDrepRegistrationId)
                .collect(Collectors.toSet());

        List<OffChainDrepRegistration> offChainDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainDRepRegistrationIds.contains(e.getDrepRegistrationId()))
                .collect(Collectors.toList());

        offChainDRepRegistrationStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainDrepRegistration> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainDrepRegistration::getDrepRegistrationId))
                .collect(Collectors.toList());

        Set<OffChainDRepRegistrationId> offChainDRepRegistrationIds = offChainAnchorData.stream()
                .map(OffChainDrepRegistration::getDrepRegistrationId)
                .collect(Collectors.toSet());

        Map<OffChainDRepRegistrationId, OffChainDrepRegistration> mapEntityById = offChainAnchorData
                .stream().collect(Collectors.toMap(
                        OffChainDrepRegistration::getDrepRegistrationId,
                        Function.identity()));

        Set<OffChainDrepRegistration> offChainDataToSave = new HashSet<>(
                offChainDRepRegistrationStorage.findByDrepRegistrationIdIn(offChainDRepRegistrationIds));

        offChainDataToSave.forEach(e -> {
            OffChainDrepRegistration oc = mapEntityById.get(e.getDrepRegistrationId());
            if (oc != null) {
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainDRepRegistrationStorage.saveAll(offChainDataToSave);
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
