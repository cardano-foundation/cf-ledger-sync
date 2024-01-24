package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.BootstrapWitness;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxBootstrapWitnesses;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.TxBootstrapWitnessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TxBootstrapWitnessServiceImplTest {

  @Mock
  TxBootstrapWitnessRepository txBootstrapWitnessRepository;

  @InjectMocks
  TxBootstrapWitnessServiceImpl txBootstrapWitnessServiceImpl;

  @Captor
  ArgumentCaptor<List<TxBootstrapWitnesses>> txBootstrapWitnessesCaptor;

  @Test
  void handleBootstrapWitnesses() {
    Collection<AggregatedTx> aggregatedTxs = new ArrayList<>();
    Map<String, Tx> txMap = new HashMap<>();
    List<BootstrapWitness> bootstrapWitnesses = new ArrayList<>();

    bootstrapWitnesses.add(BootstrapWitness.builder()
                               .publicKey("publicKey1").signature("signature1")
                               .chainCode("chaincode1").attributes("attribute1").build());

    bootstrapWitnesses.add(BootstrapWitness.builder()
                               .publicKey("publicKey2").signature("signature2")
                               .chainCode("chaincode2").attributes("attribute2").build());
    AggregatedTx aggregatedTx = AggregatedTx
        .builder()
        .hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
        .witnesses(Witnesses.builder().bootstrapWitnesses(bootstrapWitnesses).build())
        .build();

    aggregatedTxs.add(aggregatedTx);
    txMap.put("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
              Tx.builder().hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                  .build());

    txBootstrapWitnessServiceImpl.handleBootstrapWitnesses(aggregatedTxs, txMap);
    verify(txBootstrapWitnessRepository, times(1))
        .saveAll(txBootstrapWitnessesCaptor.capture());

    List<TxBootstrapWitnesses> txBootstrapWitnesses = txBootstrapWitnessesCaptor.getValue();
    assertEquals(2, txBootstrapWitnesses.size());
    assertEquals("publicKey1", txBootstrapWitnesses.get(0).getPublicKey());
    assertEquals("signature1", txBootstrapWitnesses.get(0).getSignature());
    assertEquals("publicKey2", txBootstrapWitnesses.get(1).getPublicKey());
    assertEquals("signature2", txBootstrapWitnesses.get(1).getSignature());
  }
}