package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

/**
 * ; Valid blocks must also satisfy the following two constraints: ; 1) the length of
 * transaction_bodies and transaction_witness_sets ;    must be the same ; 2) every
 * transaction_index must be strictly smaller than the ;  length of transaction_bodies
 */

import com.bloxbean.cardano.client.plutus.spec.PlutusData;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.explorer.consumercommon.entity.Datum;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.DatumRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.DatumService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.DatumFormatUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.util.DatumUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant.BATCH_QUERY_SIZE;
import static org.cardanofoundation.ledgersync.explorerconsumer.util.DatumUtil.toDatumHash;

@Service
@RequiredArgsConstructor
public class DatumServiceImpl implements DatumService {

    private final DatumRepository datumRepository;

    @Override
    public Map<String, Datum> handleDatum(Collection<AggregatedTx> aggregatedTxs,
                                          Map<String, Tx> txMap) {
        Map<String, Datum> datumMap = new ConcurrentHashMap<>();
        Set<String> datumHashes = getDatumHashes(aggregatedTxs);
        Map<String, Datum> existingDatumHashesFromDb = getExistingDatumHashByHashIn(datumHashes);

        aggregatedTxs.forEach(aggregatedTx -> {
            var transactionWitness = aggregatedTx.getWitnesses();
            var tx = txMap.get(aggregatedTx.getHash());

            datumMap.putAll(
                    handleTransactionOutput(aggregatedTx, tx, datumMap, existingDatumHashesFromDb));

            if (Objects.nonNull(transactionWitness)) {
                datumMap.putAll(
                        handleTransactionWitness(transactionWitness, tx, datumMap, existingDatumHashesFromDb));
            }
        });

        datumRepository.saveAll(datumMap.values()
                .stream()
                .sorted(Comparator.comparing(datum -> datum.getTx().getId()))
                .toList());

        // add exist datum to map
        datumMap.putAll(existingDatumHashesFromDb);

        return datumMap;
    }

    private Set<String> getDatumHashes(Collection<AggregatedTx> aggregatedTxs) {
        Set<String> datumHashes = new HashSet<>();

        //TODO -- yaci
        aggregatedTxs.forEach(aggregatedTx -> {
            Witnesses txWitnesses = aggregatedTx.getWitnesses();
            if (Objects.nonNull(txWitnesses) && !CollectionUtils.isEmpty(txWitnesses.getDatums())) {
                datumHashes.addAll(txWitnesses
                        .getDatums()
                        .stream()
                        .map(datum -> toDatumHash(datum))  //TODO refactor
                        .collect(Collectors.toSet()));
            }

            aggregatedTx.getTxOutputs().forEach(transactionOutput -> {
                if (Objects.nonNull(transactionOutput.getInlineDatum())) {
                    datumHashes.add(DatumUtil.toDatumHash(transactionOutput.getInlineDatum()));
                }
            });
        });

        return datumHashes;
    }

    private Map<String, Datum> handleTransactionWitness(
            Witnesses transactionWitness, Tx tx,
            Map<String, Datum> existingDatum,
            Map<String, Datum> existingDatumHashesFromDb) {

        Map<String, Datum> mDatumNeedSave = new HashMap<>();
        transactionWitness.getDatums().forEach(datum -> {
            String datumHash = null;
            try {
                datumHash = PlutusData.deserialize(HexUtil.decodeHexString(datum.getCbor())).getDatumHash();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to deserialize datum", e);
            }

            boolean datumExist = existingDatumHashesFromDb.containsKey(datumHash)
                    || existingDatum.containsKey(datumHash);

            if (!datumExist) {
                mDatumNeedSave.put(datumHash,
                        Datum.builder().
                                hash(datumHash).
                                value(JsonUtil.getPrettyJson(datum.getJson())).
                                tx(tx).
                                bytes(HexUtil.decodeHexString(datum.getCbor())).
                                build());
            }
        });
        return mDatumNeedSave;
    }


    private Map<String, Datum> handleTransactionOutput(
            AggregatedTx aggregatedTx, Tx tx, Map<String, Datum> existingDatum,
            Map<String, Datum> existingDatumHashesFromDb) {

        Map<String, Datum> datumInlineNeedSave = new HashMap<>();
        //TODO -- yaci
        aggregatedTx.getTxOutputs()
                .forEach(transactionOutput -> {
                    var inlineDatum = transactionOutput.getInlineDatum();
                    if (Objects.nonNull(inlineDatum) &&
                            !existingDatumHashesFromDb.containsKey(toDatumHash(inlineDatum)) &&
                            !existingDatum.containsKey(toDatumHash(inlineDatum))) {
                        datumInlineNeedSave.put(toDatumHash(inlineDatum),
                                Datum.builder().
                                        hash(toDatumHash(inlineDatum)).
                                        bytes(HexUtil.decodeHexString(inlineDatum.getCbor())).
                                        tx(tx).
                                        value(JsonUtil.getPrettyJson(inlineDatum.getJson())).
                                        build());
                    }
                });
        return datumInlineNeedSave;
    }

    private Map<String, Datum> getExistingDatumHashByHashIn(Collection<String> datumHashes) {
        Map<String, Datum> existingDatumHashes = new ConcurrentHashMap<>();
        var queryBatches = Lists.partition(new ArrayList<>(datumHashes), BATCH_QUERY_SIZE);

        queryBatches.parallelStream().forEach(datumHashBatch ->
                datumRepository.getExistHashByHashIn(new HashSet<>(datumHashBatch))
                        .parallelStream()
                        .forEach(
                                existingDatum -> existingDatumHashes.put(existingDatum.getHash(), existingDatum)));

        return existingDatumHashes;
    }

}
