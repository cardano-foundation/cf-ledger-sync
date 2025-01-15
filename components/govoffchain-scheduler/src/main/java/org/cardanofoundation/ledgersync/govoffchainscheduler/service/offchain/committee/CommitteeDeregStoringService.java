package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.committee;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainCommitteeDeregistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainCommitteeDeregistrationId;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainStoringService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainCommitteeDeregStorage;
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
public class CommitteeDeregStoringService extends
        OffChainStoringService<OffChainCommitteeDeregistration, OffChainFetchError> {

    final GovOffChainSchedulerProperties properties;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainCommitteeDeregStorage offChainCommitteeDeregStorage;

    @Override
    public void insertFetchData(Collection<OffChainCommitteeDeregistration> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainCommitteeDeregistration::getCommitteeDeregistrationId))
                .collect(Collectors.toList());

        Set<OffChainCommitteeDeregistrationId> offChainCommitteeDeregIds = offChainAnchorData.stream()
                .map(OffChainCommitteeDeregistration::getCommitteeDeregistrationId)
                .collect(Collectors.toSet());

        Set<OffChainCommitteeDeregistrationId> existingOffChainCommitteeDeregIds = new HashSet<>(
                offChainCommitteeDeregStorage.findByCommitteeDeregistrationIdIn(offChainCommitteeDeregIds))
                .stream().map(OffChainCommitteeDeregistration::getCommitteeDeregistrationId)
                .collect(Collectors.toSet());

        List<OffChainCommitteeDeregistration> offChainDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainCommitteeDeregIds.contains(e.getCommitteeDeregistrationId()))
                .collect(Collectors.toList());

        offChainCommitteeDeregStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainCommitteeDeregistration> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainCommitteeDeregistration::getCommitteeDeregistrationId))
                .collect(Collectors.toList());

        Set<OffChainCommitteeDeregistrationId> offChainCommitteeDeregIds = offChainAnchorData.stream()
                .map(OffChainCommitteeDeregistration::getCommitteeDeregistrationId)
                .collect(Collectors.toSet());

        Map<OffChainCommitteeDeregistrationId, OffChainCommitteeDeregistration> mapEntityById = offChainAnchorData
                .stream().collect(Collectors.toMap(
                        OffChainCommitteeDeregistration::getCommitteeDeregistrationId,
                        Function.identity()));

        Set<OffChainCommitteeDeregistration> offChainDataToSave = new HashSet<>(
                offChainCommitteeDeregStorage.findByCommitteeDeregistrationIdIn(offChainCommitteeDeregIds));

        offChainDataToSave.forEach(e -> {
            OffChainCommitteeDeregistration oc = mapEntityById.get(e.getCommitteeDeregistrationId());
            if (oc != null) {
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainCommitteeDeregStorage.saveAll(offChainDataToSave);
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
