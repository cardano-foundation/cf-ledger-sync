package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxBootstrapWitnesses;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxBootstrapWitnessRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TxBootstrapWitnessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TxBootstrapWitnessServiceImpl implements TxBootstrapWitnessService {

    TxBootstrapWitnessRepository txBootstrapWitnessRepository;

    @Override
    public void handleBootstrapWitnesses(Collection<AggregatedTx> aggregatedTxs,
                                         Map<String, Tx> txMap) {
        List<TxBootstrapWitnesses> txBootstrapWitnesses = new ArrayList<>();

        aggregatedTxs.forEach(aggregatedTx -> {
            var txWitnesses = aggregatedTx.getWitnesses();
            if (!Objects.isNull(txWitnesses)) {
                var bootstrapWitnesses = aggregatedTx.getWitnesses().getBootstrapWitnesses();
                if (!CollectionUtils.isEmpty(bootstrapWitnesses)) {
                    txBootstrapWitnesses.addAll(bootstrapWitnesses.stream()
                            .map(bootstrapWitness -> TxBootstrapWitnesses.builder()
                                    .tx(txMap.get(aggregatedTx.getHash()))
                                    .signature(bootstrapWitness.getSignature())
                                    .publicKey(bootstrapWitness.getPublicKey())
                                    .chainCode(bootstrapWitness.getChainCode())
                                    .attributes(bootstrapWitness.getAttributes())
                                    .build())
                            .toList());
                }
            }
        });

        txBootstrapWitnessRepository.saveAll(txBootstrapWitnesses);
    }
}
