package org.cardanofoundation.ledgersync.service.impl;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxMetadataHash;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.repository.TxMetaDataHashRepository;
import org.cardanofoundation.ledgersync.service.TxMetaDataHashService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxMetaDataHashServiceImpl implements TxMetaDataHashService {

    final TxMetaDataHashRepository txMetaDataHashRepository;

    @Override
    public Map<String, TxMetadataHash> handleMetaDataHash(Collection<AggregatedTx> aggregatedTxes) {

        Map<String, TxMetadataHash> existsHash = Lists.partition(aggregatedTxes.stream()
                        .filter(aggregatedTx -> !ObjectUtils.isEmpty(aggregatedTx.getAuxiliaryDataHash()))
                        .map(AggregatedTx::getAuxiliaryDataHash)
                        .collect(Collectors.toSet())
                        .stream().toList(), ConsumerConstant.TX_OUT_BATCH_QUERY_SIZE)
                .stream()
                .map(txMetaDataHashRepository::findAllByHashIn)
                .flatMap(List::stream)
                .collect(Collectors.toMap(TxMetadataHash::getHash, Function.identity(),
                        (old, change) -> old));

        final Map<String, TxMetadataHash> txMetadataHashMap = aggregatedTxes.stream()
                .filter(aggregatedTx -> !ObjectUtils.isEmpty(aggregatedTx.getAuxiliaryDataHash()))
                .filter(aggregatedTx -> !existsHash.containsKey(aggregatedTx.getAuxiliaryDataHash()))
                .map(aggTx ->
                        TxMetadataHash.builder()
                                .hash(aggTx.getAuxiliaryDataHash())
                                .build())
                .map(TxMetadataHash.class::cast)
                .collect(Collectors.toMap(
                        TxMetadataHash::getHash,
                        Function.identity(), (o1, o2) -> o1));

        if (!ObjectUtils.isEmpty(txMetadataHashMap)) {
            txMetaDataHashRepository.saveAll(txMetadataHashMap.values());
        }

        txMetadataHashMap.putAll(existsHash);

        return txMetadataHashMap;
    }
}
