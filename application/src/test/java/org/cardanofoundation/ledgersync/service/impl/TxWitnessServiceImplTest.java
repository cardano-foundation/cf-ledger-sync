package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.VkeyWitness;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxWitness;
import org.cardanofoundation.ledgersync.repository.TxWitnessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TxWitnessServiceImplTest {

    @Mock
    TxWitnessRepository txWitnessRepository;

    @InjectMocks
    TxWitnessServiceImpl txWitnessServiceImpl;

    @Captor
    ArgumentCaptor<List<TxWitness>> txWitnessesCaptor;

    @Test
    void handleTxWitness_whenSamePairKeyAndSignatureInEachTransaction() {
        Map<String, Tx> txMap = new HashMap<>();

        List<VkeyWitness> vkWitnesses1 = new ArrayList<>();
        vkWitnesses1.add(
                new VkeyWitness("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));
        vkWitnesses1.add(
                new VkeyWitness("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));
        vkWitnesses1.add(
                new VkeyWitness("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));

        List<VkeyWitness> vkWitnesses2 = new ArrayList<>();
        vkWitnesses2.add(
                new VkeyWitness("aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig2"));
        vkWitnesses2.add(
                new VkeyWitness("aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig2"));

        AggregatedTx aggregatedTx1 = AggregatedTx
                .builder()
                .hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses1).build())
                .build();

        AggregatedTx aggregatedTx2 = AggregatedTx
                .builder()
                .hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses2).build())
                .build();

        Collection<AggregatedTx> aggregatedTxs = new ArrayList<>(List.of(aggregatedTx1, aggregatedTx2));

        txMap.put("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());
        txMap.put("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());

        txWitnessServiceImpl.handleTxWitness(aggregatedTxs, txMap);

        verify(txWitnessRepository, times(1))
                .saveAll(txWitnessesCaptor.capture());

        List<TxWitness> txWitnesses = txWitnessesCaptor.getValue();

        assertThat(txWitnesses).hasSize(2);
        assertThat(txWitnesses.stream().map(TxWitness::getKey))
                .contains("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228", "aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228");

        for (var txWitness : txWitnesses) {
            if (txWitness.getKey().equals("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228")) {
                assertEquals("sig1", txWitness.getSignature());
                assertEquals(3, txWitness.getIndexArrSize());
                assertThat(txWitness.getIndexArr()).contains(0, 1, 2);
            } else {
                assertEquals("sig2", txWitness.getSignature());
                assertEquals(2, txWitness.getIndexArrSize());
                assertThat(txWitness.getIndexArr()).contains(0, 1);
            }
        }
    }

    @Test
    void handleTxWitness_whenDiffPairKeyAndSignatureInEachTransaction() {
        Map<String, Tx> txMap = new HashMap<>();

        List<VkeyWitness> vkWitnesses1 = new ArrayList<>();
        vkWitnesses1.add(
                new VkeyWitness("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));
        vkWitnesses1.add(
                new VkeyWitness("bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663",
                        "sig2"));

        List<VkeyWitness> vkWitnesses2 = new ArrayList<>();
        vkWitnesses2.add(
                new VkeyWitness("aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));

        vkWitnesses2.add(
                new VkeyWitness("bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig2"));

        AggregatedTx aggregatedTx1 = AggregatedTx
                .builder()
                .hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses1).build())
                .build();

        AggregatedTx aggregatedTx2 = AggregatedTx
                .builder()
                .hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses2).build())
                .build();

        Collection<AggregatedTx> aggregatedTxs = new ArrayList<>(List.of(aggregatedTx1, aggregatedTx2));

        txMap.put("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());
        txMap.put("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());

        txWitnessServiceImpl.handleTxWitness(aggregatedTxs, txMap);

        verify(txWitnessRepository, times(1))
                .saveAll(txWitnessesCaptor.capture());

        List<TxWitness> txWitnesses = txWitnessesCaptor.getValue();

        assertEquals(4, txWitnesses.size());
        assertThat(txWitnesses.stream().map(TxWitness::getKey)).contains(
                "91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                "bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663",
                "bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                "aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228");

        assertThat(txWitnesses.stream().map(txWitness -> txWitness.getTx().getHash())).contains(
                "b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                "bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1");

        for (var txWitness : txWitnesses) {
            if (txWitness.getTx().getHash().equals("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")) {
                if (txWitness.getKey().equals("bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663")) {
                    assertEquals("sig2", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(1);
                    assertEquals(1, txWitness.getIndexArrSize());
                } else {
                    assertEquals("sig1", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(0);
                    assertEquals(1, txWitness.getIndexArrSize());
                }
            } else {
                if (txWitness.getKey().equals("bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228")) {
                    assertEquals("sig2", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(1);
                    assertEquals(1, txWitness.getIndexArrSize());
                } else {
                    assertEquals("sig1", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(0);
                    assertEquals(1, txWitness.getIndexArrSize());
                }
            }
        }
    }

    @Test
    void handleTxWitness_WhenSameAndDiffPairKeyAndSignatureInEachTransaction() {
        Map<String, Tx> txMap = new HashMap<>();

        List<VkeyWitness> vkWitnesses1 = new ArrayList<>();
        vkWitnesses1.add(
                new VkeyWitness("91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));
        vkWitnesses1.add(
                new VkeyWitness("bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663",
                        "sig2"));
        vkWitnesses1.add(
                new VkeyWitness("bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663",
                        "sig2"));

        List<VkeyWitness> vkWitnesses2 = new ArrayList<>();
        vkWitnesses2.add(
                new VkeyWitness("aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));

        vkWitnesses2.add(
                new VkeyWitness("bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig2"));

        vkWitnesses2.add(
                new VkeyWitness("aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                        "sig1"));

        AggregatedTx aggregatedTx1 = AggregatedTx
                .builder()
                .hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses1).build())
                .build();

        AggregatedTx aggregatedTx2 = AggregatedTx
                .builder()
                .hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                .witnesses(Witnesses.builder().vkeyWitnesses(vkWitnesses2).build())
                .build();

        Collection<AggregatedTx> aggregatedTxs = new ArrayList<>(List.of(aggregatedTx1, aggregatedTx2));

        txMap.put("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());
        txMap.put("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                Tx.builder().hash("bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")
                        .build());

        txWitnessServiceImpl.handleTxWitness(aggregatedTxs, txMap);

        verify(txWitnessRepository, times(1))
                .saveAll(txWitnessesCaptor.capture());

        List<TxWitness> txWitnesses = txWitnessesCaptor.getValue();

        assertEquals(4, txWitnesses.size());

        assertThat(txWitnesses.stream().map(TxWitness::getKey)).contains(
                "91274605d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                "bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663",
                "bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228",
                "aaaaaa05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228");
        assertThat(txWitnesses.stream().map(txWitness -> txWitness.getTx().getHash())).contains(
                "b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1",
                "bbbbbbbbb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1");

        for (var txWitness : txWitnesses) {
            if (txWitness.getTx().getHash().equals("b9787d0eb3a22ce58572353cf2374b3bff003af66cd0a925f75fe767501355c1")) {
                if (txWitness.getKey().equals("bb636d88c3201329267561e58f217bd4eaeb8e7c7378eee00c18a56bed743663")) {
                    assertEquals("sig2", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(1, 2);
                    assertEquals(2, txWitness.getIndexArrSize());
                } else {
                    assertEquals("sig1", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(0);
                    assertEquals(1, txWitness.getIndexArrSize());
                }
            } else {
                if (txWitness.getKey().equals("bbbbbb05d7f9a5df9b9b72aa3da9f946390cc30fbcd055d1fbec1d06797b6228")) {
                    assertEquals("sig2", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(1);
                    assertEquals(1, txWitness.getIndexArrSize());
                } else {
                    assertEquals("sig1", txWitness.getSignature());
                    assertThat(txWitness.getIndexArr()).contains(0, 2);
                    assertEquals(2, txWitness.getIndexArrSize());
                }
            }
        }
    }
}