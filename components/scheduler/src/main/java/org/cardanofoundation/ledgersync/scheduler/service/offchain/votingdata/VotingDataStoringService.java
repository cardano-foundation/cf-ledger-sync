package org.cardanofoundation.ledgersync.scheduler.service.offchain.votingdata;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainVotingData;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.scheduler.SchedulerProperties;
import org.cardanofoundation.ledgersync.scheduler.service.offchain.OffChainStoringAbstractService;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainFetchErrorStorage;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainVotingDataStorage;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class VotingDataStoringService extends
        OffChainStoringAbstractService<OffChainVotingData, OffChainFetchError> {

    final SchedulerProperties properties;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainVotingDataStorage offChainVotingDataStorage;

    @Override
    public void insertFetchData(Collection<OffChainVotingData> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainVotingData::getVotingProcedureId))
                .collect(Collectors.toList());

        Set<UUID> votingProcedureIds = offChainAnchorData.stream()
                .map(OffChainVotingData::getVotingProcedureId)
                .collect(Collectors.toSet());

        Set<UUID> existingOffChainVotingId = new HashSet<>(
                offChainVotingDataStorage.findByVotingProcedureIdIn(votingProcedureIds))
                .stream().map(OffChainVotingData::getVotingProcedureId)
                .collect(Collectors.toSet());

        List<OffChainVotingData> offChainDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainVotingId.contains(e.getVotingProcedureId()))
                .collect(Collectors.toList());

        offChainVotingDataStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainVotingData> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainVotingData::getVotingProcedureId))
                .collect(Collectors.toList());

        Set<UUID> votingProcedureIds = offChainAnchorData.stream()
                .map(OffChainVotingData::getVotingProcedureId)
                .collect(Collectors.toSet());

        Map<UUID, OffChainVotingData> mapEntityById = offChainAnchorData.stream()
                .collect(Collectors.toMap(OffChainVotingData::getVotingProcedureId, Function.identity()));

        Set<OffChainVotingData> offChainDataToSave = new HashSet<>(
                offChainVotingDataStorage.findByVotingProcedureIdIn(votingProcedureIds));

        offChainDataToSave.forEach(e -> {
            OffChainVotingData oc = mapEntityById.get(e.getVotingProcedureId());
            if (oc != null) {
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainVotingDataStorage.saveAll(offChainDataToSave);
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
