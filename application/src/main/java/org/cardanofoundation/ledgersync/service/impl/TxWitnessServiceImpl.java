package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.byron.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxWitness;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.TxWitnessRepository;
import org.cardanofoundation.ledgersync.service.TxWitnessService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TxWitnessServiceImpl implements TxWitnessService {

    private final TxWitnessRepository txWitnessRepository;

    @Override
    public void handleTxWitness(Collection<AggregatedTx> aggregatedTxs, Map<String, Tx> txMap) {
        List<TxWitness> txWitnesses = new ArrayList<>();

        aggregatedTxs.forEach(aggregatedTx -> {
            var tx = txMap.get(aggregatedTx.getHash());
            processAggregatedTxWitnesses(aggregatedTx, tx, txWitnesses);
            processByronTxWitnesses(aggregatedTx, tx, txWitnesses);
        });

        Map<Pair<String, String>, TxWitness> txWitnessMap = new HashMap<>();
        txWitnesses.forEach(txWitness -> txWitnessMap.put(Pair.of(txWitness.getKey(), txWitness.getSignature()), txWitness));

        Map<Pair<String, String>, List<Integer>> txWitnessIndexMap = new HashMap<>();
        for (int i = 0; i < txWitnesses.size(); i++) {
            TxWitness txWitness = txWitnesses.get(i);
            Pair<String, String> txWitnessesPair = Pair.of(txWitness.getKey(), txWitness.getSignature());

            if (txWitnessIndexMap.containsKey(txWitnessesPair)) {
                List<Integer> indexList = txWitnessIndexMap.get(txWitnessesPair);
                indexList.add(i);
                txWitnessIndexMap.put(txWitnessesPair, indexList);
            } else {
                txWitnessIndexMap.put(txWitnessesPair, new ArrayList<>(List.of(i)));
            }
        }

        txWitnessIndexMap.forEach((key, value) -> {
            TxWitness txWitness = txWitnessMap.get(Pair.of(key.getFirst(), key.getSecond()));
            txWitness.setIndexArrSize(value.size());
            txWitness.setIndexArr(value.toArray(Integer[]::new));
        });

        txWitnessRepository.saveAll(txWitnesses);
    }

    private void processAggregatedTxWitnesses(AggregatedTx aggregatedTx, Tx tx, List<TxWitness> txWitnesses) {
        if (aggregatedTx.getWitnesses() == null) {
            return;
        }
        var vkWitnesses = aggregatedTx.getWitnesses().getVkeyWitnesses();

        vkWitnesses.forEach(vkeyWitness -> {
            TxWitness txWitness = new TxWitness();
            txWitness.setTx(tx);
            txWitness.setKey(vkeyWitness.getKey());
            txWitness.setSignature(vkeyWitness.getSignature());
            txWitnesses.add(txWitness);
        });
    }

    private void processByronTxWitnesses(AggregatedTx aggregatedTx, Tx tx, List<TxWitness> txWitnesses) {
        var byronTxWitnesses = aggregatedTx.getByronTxWitnesses();

        if (byronTxWitnesses == null) {
            return;
        }

        for (ByronTxWitnesses byronTxWitness : byronTxWitnesses) {
            TxWitness txWitness = mapByronTxWitnessesToTxWitnesses(byronTxWitness);
            txWitness.setTx(tx);
            txWitnesses.add(txWitness);
        }
    }

    private TxWitness mapByronTxWitnessesToTxWitnesses(ByronTxWitnesses byronTxWitnesses) {
        String publicKey;
        String signature;
        String type = byronTxWitnesses.getType();
        switch (type) {
            case ByronPkWitness.TYPE:
                publicKey = ((ByronPkWitness) byronTxWitnesses).getPublicKey();
                signature = ((ByronPkWitness) byronTxWitnesses).getSignature();
                break;
            case ByronRedeemWitness.TYPE:
                publicKey = ((ByronRedeemWitness) byronTxWitnesses).getRedeemPublicKey();
                signature = ((ByronRedeemWitness) byronTxWitnesses).getRedeemSignature();
                break;
            case ByronScriptWitness.TYPE:
                publicKey = ((ByronScriptWitness) byronTxWitnesses).getValidator().getScript();
                signature = ((ByronScriptWitness) byronTxWitnesses).getRedeemer().getScript();
                break;
            case ByronUnknownWitness.TYPE:
                publicKey = ((ByronUnknownWitness) byronTxWitnesses).getData();
                signature = ((ByronUnknownWitness) byronTxWitnesses).getData();
                break;
            default:
                throw new IllegalArgumentException("Unknown byron witness type: " + type);
        }

        return TxWitness.builder()
                .key(publicKey)
                .type(type)
                .signature(signature)
                .build();
    }
}
