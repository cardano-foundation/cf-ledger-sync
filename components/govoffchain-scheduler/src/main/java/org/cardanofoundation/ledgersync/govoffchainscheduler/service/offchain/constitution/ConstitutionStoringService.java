package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.constitution;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainConstitution;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.govoffchainscheduler.GovOffChainSchedulerProperties;
import org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain.OffChainStoringService;
import org.cardanofoundation.ledgersync.govoffchainscheduler.storage.offchain.OffChainConstitutionStorage;
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
public class ConstitutionStoringService extends
        OffChainStoringService<OffChainConstitution, OffChainFetchError> {

    final GovOffChainSchedulerProperties properties;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainConstitutionStorage offChainConstitutionStorage;

    @Override
    public void insertFetchData(Collection<OffChainConstitution> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainConstitution::getConstitutionActiveEpoch))
                .collect(Collectors.toList());

        Set<Integer> constitutionActiveEpochs = offChainAnchorData.stream()
                .map(OffChainConstitution::getConstitutionActiveEpoch)
                .collect(Collectors.toSet());

        Set<Integer> existingOffChainConstitutionActiveEpochs = new HashSet<>(
                offChainConstitutionStorage.findByConstitutionActiveEpochIn(constitutionActiveEpochs))
                .stream().map(OffChainConstitution::getConstitutionActiveEpoch)
                .collect(Collectors.toSet());

        List<OffChainConstitution> offChainDataToSave = offChainAnchorData.stream()
                .filter(e -> !existingOffChainConstitutionActiveEpochs.contains(e.getConstitutionActiveEpoch()))
                .collect(Collectors.toList());

        offChainConstitutionStorage.saveAll(offChainDataToSave);
    }

    @Override
    public void updateFetchData(Collection<OffChainConstitution> offChainAnchorData) {

        offChainAnchorData = offChainAnchorData.stream()
                .filter(distinctByKey(OffChainConstitution::getConstitutionActiveEpoch))
                .collect(Collectors.toList());

        Set<Integer> constitutionActiveEpochs = offChainAnchorData.stream()
                .map(OffChainConstitution::getConstitutionActiveEpoch)
                .collect(Collectors.toSet());

        Map<Integer, OffChainConstitution> mapEntityByEpoch = offChainAnchorData.stream()
                .collect(Collectors.toMap(OffChainConstitution::getConstitutionActiveEpoch, Function.identity()));

        Set<OffChainConstitution> offChainDataToSave = new HashSet<>(
                offChainConstitutionStorage.findByConstitutionActiveEpochIn(constitutionActiveEpochs));

        offChainDataToSave.forEach(e -> {
            OffChainConstitution oc = mapEntityByEpoch.get(e.getConstitutionActiveEpoch());
            if (oc != null) {
                e.setContent(oc.getContent());
                e.setCheckValid(oc.getCheckValid());
                e.setValidAtSlot(oc.getValidAtSlot());
            }
        });

        offChainConstitutionStorage.saveAll(offChainDataToSave);
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
