package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bloxbean.cardano.yaci.core.model.Datum;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import lombok.SneakyThrows;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.DatumRepository;
import org.mockito.Mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DatumServiceImplTest {

  @Test
  @SneakyThrows
  void handleDatum() {
    DatumRepository datumRepository = Mockito.mock(DatumRepository.class);
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Witnesses transactionWitness = Mockito.mock(Witnesses.class);
    Datum datumWitness = Mockito.mock(Datum.class);
    String txHash = "6497b33b10fa2619c6efbd9f874ecd1c91badb10bf70850732aab45b90524d9e";
    Tx tx = Mockito.mock(Tx.class);
    Map<String, Tx> txMap = Map.of(txHash, tx);
    AggregatedTxOut aggregatedTxOut = Mockito.mock(AggregatedTxOut.class);
    AggregatedTxOut aggregatedTxOut2 = Mockito.mock(AggregatedTxOut.class);
    Collection<AggregatedTx> aggregatedTxs = List.of(aggregatedTx);
    List<AggregatedTxOut> txOutputs = Arrays.asList(aggregatedTxOut, aggregatedTxOut2);

    Mockito.when(datumWitness.getCbor()).thenReturn(
        "d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff");
    Mockito.when(datumWitness.getJson()).thenReturn(
        "{\"bytes\":\"d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    List<Datum> datumsWitness = List.of(
        datumWitness);

    Datum inlineDatum = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff")));
    Datum inlineDatum2 = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d8799fd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd87a80d8799fd8799f581c4b4ce6898a5b7227d1a22b4ed6be7f01fa36ac84b1adcb97b35a665b4974564153494c696e65ff1a00031549ff1a001e84801a001e8480ff")));

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getWitnesses()).thenReturn(transactionWitness);
    Mockito.when(transactionWitness.getDatums()).thenReturn(datumsWitness);

    Mockito.when(aggregatedTx.getTxOutputs()).thenReturn(txOutputs);
    Mockito.when(tx.getValidContract()).thenReturn(true);

    Mockito.when(aggregatedTxOut.getInlineDatum()).thenReturn(inlineDatum);
    Mockito.when(aggregatedTxOut2.getInlineDatum()).thenReturn(inlineDatum2);

    DatumServiceImpl laclase = new DatumServiceImpl(datumRepository);
    laclase.handleDatum(aggregatedTxs, txMap);

    Mockito.verify(datumRepository, Mockito.times(1)).getExistHashByHashIn(Mockito.anySet());
    Mockito.verify(datumRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
  }

  @Test
  @SneakyThrows
  void handleDatumWitnessOutputExist() {
    DatumRepository datumRepository = Mockito.mock(DatumRepository.class);
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Witnesses transactionWitness = Mockito.mock(Witnesses.class);
    Datum datumWitness = Mockito.mock(
        Datum.class);
    String txHash = "6497b33b10fa2619c6efbd9f874ecd1c91badb10bf70850732aab45b90524d9e";
    Tx tx = Mockito.mock(Tx.class);
    Map<String, Tx> txMap = Map.of(txHash, tx);
    AggregatedTxOut aggregatedTxOut = Mockito.mock(AggregatedTxOut.class);
    AggregatedTxOut aggregatedTxOut2 = Mockito.mock(AggregatedTxOut.class);
    Collection<AggregatedTx> aggregatedTxs = List.of(aggregatedTx);
    List<AggregatedTxOut> txOutputs = Arrays.asList(aggregatedTxOut, aggregatedTxOut2);

    Mockito.when(datumWitness.getCbor()).thenReturn(
        "d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff");
    Mockito.when(datumWitness.getJson()).thenReturn(
        "{\"bytes\":\"d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    List<Datum> datumsWitness = List.of(
        datumWitness);

    Datum inlineDatum = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff")));
    Datum inlineDatum2 = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d8799fd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd87a80d8799fd8799f581c4b4ce6898a5b7227d1a22b4ed6be7f01fa36ac84b1adcb97b35a665b4974564153494c696e65ff1a00031549ff1a001e84801a001e8480ff")));

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getWitnesses()).thenReturn(transactionWitness);
    Mockito.when(transactionWitness.getDatums()).thenReturn(datumsWitness);

    Mockito.when(aggregatedTx.getTxOutputs()).thenReturn(txOutputs);
    Mockito.when(tx.getValidContract()).thenReturn(true);

    Mockito.when(aggregatedTxOut.getInlineDatum()).thenReturn(inlineDatum);
    Mockito.when(aggregatedTxOut2.getInlineDatum()).thenReturn(inlineDatum2);

    Set<org.cardanofoundation.explorer.consumercommon.entity.Datum> datums = new HashSet<>();
    datums.add(org.cardanofoundation.explorer.consumercommon.entity.Datum.builder()
        .hash("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d").build());

    Mockito.when(datumRepository.getExistHashByHashIn(Mockito.anySet()))
        .thenReturn(datums);

    DatumServiceImpl laclase = new DatumServiceImpl(datumRepository);

    laclase.handleDatum(aggregatedTxs, txMap);
    Mockito.verify(datumRepository, Mockito.times(1)).getExistHashByHashIn(Mockito.anySet());
    Mockito.verify(datumRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
  }

  @Test
  void handleDatumWitnessOutputNull() {
    DatumRepository datumRepository = Mockito.mock(DatumRepository.class);
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Witnesses transactionWitness = Mockito.mock(Witnesses.class);
    org.cardanofoundation.ledgersync.common.common.Datum datumWitness = Mockito.mock(
        org.cardanofoundation.ledgersync.common.common.Datum.class);
    String txHash = "6497b33b10fa2619c6efbd9f874ecd1c91badb10bf70850732aab45b90524d9e";
    Tx tx = Mockito.mock(Tx.class);
    Map<String, Tx> txMap = Map.of(txHash, tx);
    AggregatedTxOut aggregatedTxOut = Mockito.mock(AggregatedTxOut.class);
    AggregatedTxOut aggregatedTxOut2 = Mockito.mock(AggregatedTxOut.class);
    Collection<AggregatedTx> aggregatedTxs = List.of(aggregatedTx);
    List<AggregatedTxOut> txOutputs = Arrays.asList(aggregatedTxOut, aggregatedTxOut2);

    Mockito.when(datumWitness.getHash())
        .thenReturn("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d");
    Mockito.when(datumWitness.getCbor()).thenReturn(
        "d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff");
    Mockito.when(datumWitness.getJson()).thenReturn(
        "{\"bytes\":\"d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    org.cardanofoundation.ledgersync.common.common.Datum inlineDatum = Mockito.mock(
        org.cardanofoundation.ledgersync.common.common.Datum.class);
    org.cardanofoundation.ledgersync.common.common.Datum inlineDatum2 = Mockito.mock(
        org.cardanofoundation.ledgersync.common.common.Datum.class);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getWitnesses()).thenReturn(transactionWitness);
    Mockito.when(aggregatedTx.getTxOutputs()).thenReturn(txOutputs);
    Mockito.when(tx.getValidContract()).thenReturn(true);

    Mockito.when(aggregatedTxOut.getInlineDatum()).thenReturn(null);
    Mockito.when(aggregatedTxOut2.getInlineDatum()).thenReturn(null);
    Mockito.when(inlineDatum.getHash())
        .thenReturn("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d");
    Mockito.when(inlineDatum.getCbor()).thenReturn(
        "d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff");
    Mockito.when(inlineDatum.getJson()).thenReturn(
        "{\"bytes\":\"d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    Mockito.when(inlineDatum2.getHash())
        .thenReturn("71c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d");
    Mockito.when(inlineDatum2.getCbor()).thenReturn(
        "d8799fd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd87a80d8799fd8799f581c4b4ce6898a5b7227d1a22b4ed6be7f01fa36ac84b1adcb97b35a665b4974564153494c696e65ff1a00031549ff1a001e84801a001e8480ff");
    Mockito.when(inlineDatum2.getJson()).thenReturn(
        "{\"bytes\":\"a8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    Set<org.cardanofoundation.explorer.consumercommon.entity.Datum> datums = new HashSet<>();
    datums.add(org.cardanofoundation.explorer.consumercommon.entity.Datum.builder()
        .hash("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d").build());

    Mockito.when(datumRepository.getExistHashByHashIn(Mockito.anySet())).thenReturn(datums);
    DatumServiceImpl laclase = new DatumServiceImpl(datumRepository);

    laclase.handleDatum(aggregatedTxs, txMap);
    Mockito.verify(datumRepository, Mockito.times(0)).getExistHashByHashIn(Mockito.anySet());
    Mockito.verify(datumRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
  }

  @Test
  void getDatumsByHashes() {
  }

  @Test
  @SneakyThrows
  void handleDatumWithExistsData() {
    DatumRepository datumRepository = Mockito.mock(DatumRepository.class);
    AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
    Witnesses transactionWitness = Mockito.mock(Witnesses.class);

    AggregatedTxOut aggregatedTxOut = Mockito.mock(AggregatedTxOut.class);
    AggregatedTxOut aggregatedTxOut2 = Mockito.mock(AggregatedTxOut.class);
    Collection<AggregatedTx> aggregatedTxs = List.of(aggregatedTx);
    List<AggregatedTxOut> txOutputs = Arrays.asList(aggregatedTxOut, aggregatedTxOut2);

    Datum datumWitness = Mockito.mock(
        Datum.class);

    Mockito.when(datumWitness.getCbor()).thenReturn(
        "d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff");
    Mockito.when(datumWitness.getJson()).thenReturn(
        "{\"bytes\":\"d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff\"}");

    Datum inlineDatum = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d81858a5d8799fd8799fd8799fd8799f582069a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257ff02ff581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd227447617364666173644b6661736466736164666173d87a9fffd8799fffd8799fd8799f581c0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274ffd87a9fffff9fff200000486173646661736466ffff")));
    Datum inlineDatum2 = Datum.from(CborSerializationUtil.deserialize(HexUtil.decodeHexString("d8799fd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd8799fd8799f581c70a336982cf23779f016e53625b778f49883050eb1a66ef075f75d24ffd8799fd8799fd8799f581cac537c1a299201d53cac96ff89266711af1bbb51b5dcaec2fa5126e8ffffffffd87a80d8799fd8799f581c4b4ce6898a5b7227d1a22b4ed6be7f01fa36ac84b1adcb97b35a665b4974564153494c696e65ff1a00031549ff1a001e84801a001e8480ff")));

    Mockito.when(aggregatedTxOut.getInlineDatum()).thenReturn(inlineDatum);
    Mockito.when(aggregatedTxOut2.getInlineDatum()).thenReturn(inlineDatum2);

    List<Datum> datumsWitness = List
        .of(datumWitness);

    String txHash = "6497b33b10fa2619c6efbd9f874ecd1c91badb10bf70850732aab45b90524d9e";
    Tx tx = Mockito.mock(Tx.class);
    Map<String, Tx> txMap = Map.of(txHash, tx);

    Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
    Mockito.when(aggregatedTx.getWitnesses()).thenReturn(transactionWitness);
    Mockito.when(aggregatedTx.getTxOutputs()).thenReturn(txOutputs);
    Mockito.when(transactionWitness.getDatums()).thenReturn(datumsWitness);


    Set<org.cardanofoundation.explorer.consumercommon.entity.Datum> datums = new HashSet<>();
    datums.add(org.cardanofoundation.explorer.consumercommon.entity.Datum.builder()
        .hash("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d").build());

    Mockito.when(datumRepository.getExistHashByHashIn(Mockito.anySet())).thenReturn(datums);
    DatumServiceImpl laclase = new DatumServiceImpl(datumRepository);

    Map<String, org.cardanofoundation.explorer.consumercommon.entity.Datum> actualDatum = laclase.handleDatum(aggregatedTxs, txMap);

    Assertions.assertEquals(actualDatum.keySet().size(), 3);

    Assertions.assertTrue(actualDatum.containsKey("81c4b709d63f814af964013721d35aa0f4c91e75de8274db47dfd5a4b377eb7d"));
    Assertions.assertTrue(actualDatum.containsKey("3e7a2d9c94dc9c28521a18215b6088d8db1792a94992324108458beafbac8ed1"));
    Assertions.assertTrue(actualDatum.containsKey("8828013bf8f54bec83b14f96ced5b9061f231c51fc981bd9931a4cf7edc39537"));

    Mockito.verify(datumRepository, Mockito.times(1)).getExistHashByHashIn(Mockito.anySet());
    Mockito.verify(datumRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
  }
}
