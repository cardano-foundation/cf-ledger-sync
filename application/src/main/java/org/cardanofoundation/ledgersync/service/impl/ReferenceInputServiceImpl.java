package org.cardanofoundation.ledgersync.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.ReferenceTxIn;
import org.cardanofoundation.ledgersync.consumercommon.entity.ReferenceTxIn.ReferenceTxInBuilder;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.repository.ReferenceInputRepository;
import org.cardanofoundation.ledgersync.service.ReferenceInputService;
import org.cardanofoundation.ledgersync.service.TxOutService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ReferenceInputServiceImpl implements ReferenceInputService {

    ReferenceInputRepository referenceInputRepository;
    TxOutService txOutService;

    @Override
    public List<ReferenceTxIn> handleReferenceInputs(
            Map<String, Set<AggregatedTxIn>> referenceTxInMap, Map<String, Tx> txMap,
            Map<Pair<String, Short>, TxOut> newTxOutMap) {
        Set<AggregatedTxIn> allReferenceTxIns = referenceTxInMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Pair<String, Short>, TxOut> txOutMap = txOutService
                .getTxOutCanUseByAggregatedTxIns(allReferenceTxIns)
                .stream()
                .collect(Collectors.toMap(this::getTxOutKey, Function.identity()));
        return referenceInputRepository.saveAll(
                referenceTxInMap.entrySet().stream().flatMap(txHashReferenceTxInsEntry -> {
                    Set<AggregatedTxIn> referenceTxIns = txHashReferenceTxInsEntry.getValue();
                    String txHash = txHashReferenceTxInsEntry.getKey();
                    Tx tx = txMap.get(txHash);
                    return referenceTxIns.stream()
                            .map(referInput -> handleReferenceInput(tx, referInput, txOutMap, newTxOutMap));
                }).toList()
        );
    }

    private Pair<String, Short> getTxOutKey(TxOut txOut) {
        return Pair.of(txOut.getTx().getHash(), txOut.getIndex());
    }

    public ReferenceTxIn handleReferenceInput(Tx tx, AggregatedTxIn referenceInput,
                                              Map<Pair<String, Short>, TxOut> txOutMap,
                                              Map<Pair<String, Short>, TxOut> newTxOutMap) {
        ReferenceTxInBuilder<?, ?> referenceTxInBuilder = ReferenceTxIn.builder();
        referenceTxInBuilder.txIn(tx);
        Pair<String, Short> txOutKey =
                Pair.of(referenceInput.getTxId(), (short) referenceInput.getIndex());
        TxOut txOut = txOutMap.get(txOutKey);
        if (Objects.isNull(txOut)) {
            txOut = newTxOutMap.get(txOutKey);
        }

        referenceTxInBuilder.txOut(txOut.getTx());
        referenceTxInBuilder.txOutIndex(txOut.getIndex());
        return referenceTxInBuilder.build();
    }
}
