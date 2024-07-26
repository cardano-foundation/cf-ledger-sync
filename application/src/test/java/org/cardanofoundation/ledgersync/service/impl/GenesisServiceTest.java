package org.cardanofoundation.ledgersync.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.context.annotation.Profile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.consumercommon.entity.EpochParam;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.dto.GenesisData;
import org.cardanofoundation.ledgersync.service.impl.genesis.GenesisDataServiceImpl;
import org.cardanofoundation.ledgersync.service.impl.genesis.GenesisLocalFetching;
import org.cardanofoundation.ledgersync.service.impl.genesis.GenesisWebClientFetching;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.cardanofoundation.ledgersync.service.impl.genesis.GenesisDataServiceImpl.COIN_PER_BYTE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class GenesisServiceTest {

  @InjectMocks
  GenesisDataServiceImpl genesisDataService;
  @InjectMocks
  GenesisWebClientFetching genesisWebClientFetching;

  @InjectMocks
  GenesisLocalFetching genesisLocalFetching;

  @BeforeEach
  @Profile("internet")
  void setup() {
    final int size = 16 * 1024 * 1024;
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
        .build();
    ReflectionTestUtils.setField(genesisWebClientFetching, "webClient", WebClient.builder()
        .exchangeStrategies(strategies));
    ReflectionTestUtils.setField(genesisDataService, "objectMapper", new ObjectMapper());
  }

  @BeforeEach
  @Profile("!internet")
  void setupLocal() {
    ReflectionTestUtils.setField(genesisDataService, "objectMapper", new ObjectMapper());
  }

  @Test
  @Profile("internet")
  void testGenesisMainnetInternetFetching() {

    GenesisData data = GenesisData.builder()
        .txOuts(new ArrayList<>())
        .txs(new ArrayList<>())
        .build();

    ReflectionTestUtils.setField(genesisDataService, "genesisHash",
        "5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d432e940ebb");
    ReflectionTestUtils.setField(genesisDataService, "genesisByron",
        "https://book.world.dev.cardano.org/environments/mainnet/byron-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisShelley",
        "https://book.world.dev.cardano.org/environments/mainnet/shelley-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisAlonzo",
        "https://book.world.dev.cardano.org/environments/mainnet/alonzo-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisConway",
            "https://book.world.dev.cardano.org/environments/mainnet/conway-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisFetching", genesisWebClientFetching);

    genesisDataService.fetchShelleyGenesis(data);
    genesisDataService.fetchAlonzoGenesis(data);
    genesisDataService.setupBabbageGenesis(data);
    genesisDataService.fetchConwayGenesis(data);
    genesisDataService.fetchTransactionAndTransactionOutput(data);
    genesisDataService.fetchBlockAndSlotLeader(data);

    Assertions.assertNotNull(data);
    Assertions.assertNotNull(data.getBlock());
    Assertions.assertNotNull(data.getTxs());
    Assertions.assertNotNull(data.getTxOuts());
    Assertions.assertNotNull(data.getSlotLeaders());
    Assertions.assertNotNull(data.getAlonzoCostModel());
    Assertions.assertNotNull(data.getShelley());
    Assertions.assertNotNull(data.getAlonzo());
    Assertions.assertNotNull(data.getConway());

    //check block
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d432e940ebb",
        data.getBlock().getHash());
    Assertions.assertEquals(14505, data.getBlock().getTxCount());
    Assertions.assertEquals(
        Timestamp.valueOf(LocalDateTime.ofEpochSecond(1506203091L, 0, ZoneOffset.UTC)),
        data.getStartTime());
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d43",
        data.getBlock().getSlotLeader().getHash());
    //check slot leader
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d43",
        data.getSlotLeaders().get(0).getHash());

    // check tx
    Assertions.assertEquals(14505, data.getTxs().size());

    Tx tx = data.getTxs().get(14504);
    Assertions.assertEquals("d234c34a92244cae87c5b9fb0b97fa3e89407769f411cc957340e9e3a2ac638e",
        tx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(648176763000000L), tx.getOutSum());

    Tx fistTx = data.getTxs().get(0);
    Assertions.assertEquals("8ee33c9906974706223d7d500d63bbee2369d7150f972757a9fdded2f706b938",
        fistTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(9999300000000L), fistTx.getOutSum());

    Tx middleTx = data.getTxs().get(7600);
    Assertions.assertEquals("c91f00be263c4aaa2880e30c773c3185669ae952e65770d88ecf264ca5a4ff4c",
        middleTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(385022000000L), middleTx.getOutSum());

    // check txout
    Assertions.assertEquals(14505, data.getTxOuts().size());

    TxOut txOut = data.getTxOuts().get(14504);
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWByAeZ+ktmy6xH0rzOFH3m6J+Yr+9Ljy3Vx6aNtyoAIavNR8oA==")
        , txOut.getAddressRaw()));
    Assertions.assertEquals(
        "Ae2tdPwUPEZ9dH9VC4iVXZRNYe5HGc73AKVMYHExpgYBmDMkgCUgnJGqqqq"
        , txOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(648176763000000L), txOut.getValue());

    TxOut fistTxOut = data.getTxOuts().get(0);
    Assertions.assertEquals("Ae2tdPwUPEZHFQnrr2dYB4GEQ8WVKspEyrg29pJ3f7qdjzaxjeShEEokF5f",
        fistTxOut.getAddress());
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWBzM33NbfVyv5E5l51qC/UMF/ncSkkqOEZb+/b3foAIanLwv/g=="),
        fistTxOut.getAddressRaw()));
    Assertions.assertEquals(BigInteger.valueOf(9999300000000L), fistTxOut.getValue());

    TxOut middleTxOut = data.getTxOuts().get(7600);
    Assertions.assertEquals("Ae2tdPwUPEZ9xCuFBz8BrhshnM5ZQaRnB7ez5LAp3uRXW9FBAvSXXWkJzQi",
        middleTxOut.getAddress());
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWByDvtsoFHc/ZLrdPMYy6v9PZedqajunmWcm96CvoAIaslOZWw=="),
        middleTxOut.getAddressRaw()));
    Assertions.assertEquals(BigInteger.valueOf(385022000000L), middleTxOut.getValue());

    // shelley protocol
    EpochParam shelleyGenesis = EpochParam.builder()
        .minFeeA(44)
        .minFeeB(155381)
        .protocolMinor(0)
        .protocolMajor(2)
        .decentralisation(1.0)
        .maxEpoch(18)
        .extraEntropy("NeutralNonce")
        .maxTxSize(16384)
        .maxBlockSize(65536)
        .maxBhSize(1100)
        .minUtxoValue(BigInteger.valueOf(1000000L))
        .poolDeposit(BigInteger.valueOf(500000000L))
        .minPoolCost(BigInteger.valueOf(340000000L))
        .keyDeposit(BigInteger.valueOf(2000000L))
        .optimalPoolCount(150)
        .influence(0.3)
        .treasuryGrowthRate(0.2)
        .monetaryExpandRate(0.003)
        .build();

    Assertions.assertEquals(shelleyGenesis.hashCode(), data.getShelley().hashCode());
    // alonzo protocol
    EpochParam alonzoGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(34482L))
        .priceMem((double) 577 / 10000)
        .priceStep((double) 721 / 10000000)
        .maxTxExMem(BigInteger.valueOf(10000000L))
        .maxTxExSteps(BigInteger.valueOf(10000000000L))
        .maxBlockExMem(BigInteger.valueOf(50000000L))
        .maxBlockExSteps(BigInteger.valueOf(40000000000L))
        .maxValSize(BigInteger.valueOf(5000))
        .collateralPercent(150)
        .maxCollateralInputs(3)
        .costModel(CostModel.builder()
            .costs(
                "{\"PlutusV1\":{\"sha2_256-memory-arguments\":4,\"equalsString-cpu-arguments-constant\":1000,\"cekDelayCost-exBudgetMemory\":100,\"lessThanEqualsByteString-cpu-arguments-intercept\":103599,\"divideInteger-memory-arguments-minimum\":1,\"appendByteString-cpu-arguments-slope\":621,\"blake2b-cpu-arguments-slope\":29175,\"iData-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-slope\":1000,\"unBData-cpu-arguments\":150000,\"multiplyInteger-cpu-arguments-intercept\":61516,\"cekConstCost-exBudgetMemory\":100,\"nullList-cpu-arguments\":150000,\"equalsString-cpu-arguments-intercept\":150000,\"trace-cpu-arguments\":150000,\"mkNilData-memory-arguments\":32,\"lengthOfByteString-cpu-arguments\":150000,\"cekBuiltinCost-exBudgetCPU\":29773,\"bData-cpu-arguments\":150000,\"subtractInteger-cpu-arguments-slope\":0,\"unIData-cpu-arguments\":150000,\"consByteString-memory-arguments-intercept\":0,\"divideInteger-memory-arguments-slope\":1,\"divideInteger-cpu-arguments-model-arguments-slope\":118,\"listData-cpu-arguments\":150000,\"headList-cpu-arguments\":150000,\"chooseData-memory-arguments\":32,\"equalsInteger-cpu-arguments-intercept\":136542,\"sha3_256-cpu-arguments-slope\":82363,\"sliceByteString-cpu-arguments-slope\":5000,\"unMapData-cpu-arguments\":150000,\"lessThanInteger-cpu-arguments-intercept\":179690,\"mkCons-cpu-arguments\":150000,\"appendString-memory-arguments-intercept\":0,\"modInteger-cpu-arguments-model-arguments-slope\":118,\"ifThenElse-cpu-arguments\":1,\"mkNilPairData-cpu-arguments\":150000,\"lessThanEqualsInteger-cpu-arguments-intercept\":145276,\"addInteger-memory-arguments-slope\":1,\"chooseList-memory-arguments\":32,\"constrData-memory-arguments\":32,\"decodeUtf8-cpu-arguments-intercept\":150000,\"equalsData-memory-arguments\":1,\"subtractInteger-memory-arguments-slope\":1,\"appendByteString-memory-arguments-intercept\":0,\"lengthOfByteString-memory-arguments\":4,\"headList-memory-arguments\":32,\"listData-memory-arguments\":32,\"consByteString-cpu-arguments-intercept\":150000,\"unIData-memory-arguments\":32,\"remainderInteger-memory-arguments-minimum\":1,\"bData-memory-arguments\":32,\"lessThanByteString-cpu-arguments-slope\":248,\"encodeUtf8-memory-arguments-intercept\":0,\"cekStartupCost-exBudgetCPU\":100,\"multiplyInteger-memory-arguments-intercept\":0,\"unListData-memory-arguments\":32,\"remainderInteger-cpu-arguments-model-arguments-slope\":118,\"cekVarCost-exBudgetCPU\":29773,\"remainderInteger-memory-arguments-slope\":1,\"cekForceCost-exBudgetCPU\":29773,\"sha2_256-cpu-arguments-slope\":29175,\"equalsInteger-memory-arguments\":1,\"indexByteString-memory-arguments\":1,\"addInteger-memory-arguments-intercept\":1,\"chooseUnit-cpu-arguments\":150000,\"sndPair-cpu-arguments\":150000,\"cekLamCost-exBudgetCPU\":29773,\"fstPair-cpu-arguments\":150000,\"quotientInteger-memory-arguments-minimum\":1,\"decodeUtf8-cpu-arguments-slope\":1000,\"lessThanInteger-memory-arguments\":1,\"lessThanEqualsInteger-cpu-arguments-slope\":1366,\"fstPair-memory-arguments\":32,\"modInteger-memory-arguments-intercept\":0,\"unConstrData-cpu-arguments\":150000,\"lessThanEqualsInteger-memory-arguments\":1,\"chooseUnit-memory-arguments\":32,\"sndPair-memory-arguments\":32,\"addInteger-cpu-arguments-intercept\":197209,\"decodeUtf8-memory-arguments-slope\":8,\"equalsData-cpu-arguments-intercept\":150000,\"mapData-cpu-arguments\":150000,\"mkPairData-cpu-arguments\":150000,\"quotientInteger-cpu-arguments-constant\":148000,\"consByteString-memory-arguments-slope\":1,\"cekVarCost-exBudgetMemory\":100,\"indexByteString-cpu-arguments\":150000,\"unListData-cpu-arguments\":150000,\"equalsInteger-cpu-arguments-slope\":1326,\"cekStartupCost-exBudgetMemory\":100,\"subtractInteger-cpu-arguments-intercept\":197209,\"divideInteger-cpu-arguments-model-arguments-intercept\":425507,\"divideInteger-memory-arguments-intercept\":0,\"cekForceCost-exBudgetMemory\":100,\"blake2b-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-constant\":148000,\"tailList-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-intercept\":150000,\"equalsString-cpu-arguments-slope\":1000,\"lessThanByteString-memory-arguments\":1,\"multiplyInteger-cpu-arguments-slope\":11218,\"appendByteString-cpu-arguments-intercept\":396231,\"lessThanEqualsByteString-cpu-arguments-slope\":248,\"modInteger-memory-arguments-slope\":1,\"addInteger-cpu-arguments-slope\":0,\"equalsData-cpu-arguments-slope\":10000,\"decodeUtf8-memory-arguments-intercept\":0,\"chooseList-cpu-arguments\":150000,\"constrData-cpu-arguments\":150000,\"equalsByteString-memory-arguments\":1,\"cekApplyCost-exBudgetCPU\":29773,\"quotientInteger-memory-arguments-slope\":1,\"verifySignature-cpu-arguments-intercept\":3345831,\"unMapData-memory-arguments\":32,\"mkCons-memory-arguments\":32,\"sliceByteString-memory-arguments-slope\":1,\"sha3_256-memory-arguments\":4,\"ifThenElse-memory-arguments\":1,\"mkNilPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-slope\":247,\"appendString-cpu-arguments-intercept\":150000,\"quotientInteger-cpu-arguments-model-arguments-slope\":118,\"cekApplyCost-exBudgetMemory\":100,\"equalsString-memory-arguments\":1,\"multiplyInteger-memory-arguments-slope\":1,\"cekBuiltinCost-exBudgetMemory\":100,\"remainderInteger-memory-arguments-intercept\":0,\"sha2_256-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-model-arguments-intercept\":425507,\"lessThanEqualsByteString-memory-arguments\":1,\"tailList-memory-arguments\":32,\"mkNilData-cpu-arguments\":150000,\"chooseData-cpu-arguments\":150000,\"unBData-memory-arguments\":32,\"blake2b-memory-arguments\":4,\"iData-memory-arguments\":32,\"nullList-memory-arguments\":32,\"cekDelayCost-exBudgetCPU\":29773,\"subtractInteger-memory-arguments-intercept\":1,\"lessThanByteString-cpu-arguments-intercept\":103599,\"consByteString-cpu-arguments-slope\":1000,\"appendByteString-memory-arguments-slope\":1,\"trace-memory-arguments\":32,\"divideInteger-cpu-arguments-constant\":148000,\"cekConstCost-exBudgetCPU\":29773,\"encodeUtf8-memory-arguments-slope\":8,\"quotientInteger-cpu-arguments-model-arguments-intercept\":425507,\"mapData-memory-arguments\":32,\"appendString-cpu-arguments-slope\":1000,\"modInteger-cpu-arguments-constant\":148000,\"verifySignature-cpu-arguments-slope\":1,\"unConstrData-memory-arguments\":32,\"quotientInteger-memory-arguments-intercept\":0,\"equalsByteString-cpu-arguments-constant\":150000,\"sliceByteString-memory-arguments-intercept\":0,\"mkPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-intercept\":112536,\"appendString-memory-arguments-slope\":1,\"lessThanInteger-cpu-arguments-slope\":497,\"modInteger-cpu-arguments-model-arguments-intercept\":425507,\"modInteger-memory-arguments-minimum\":1,\"sha3_256-cpu-arguments-intercept\":0,\"verifySignature-memory-arguments\":1,\"cekLamCost-exBudgetMemory\":100,\"sliceByteString-cpu-arguments-intercept\":150000}}")
            .hash("144099ae3a42f67fa87a8b1a0144a7f243e6b87d8c6f080fc21c67fdd55d6478")
            .build())
        .minUtxoValue(null)
        .build();
    Assertions.assertEquals(alonzoGenesis, data.getAlonzo());
    // babbage protocol
    EpochParam babbageGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(COIN_PER_BYTE))
        .build();

    Assertions.assertEquals(babbageGenesis.hashCode(), data.getBabbage().hashCode());

    // conway protocol
    EpochParam conwayGenesis = EpochParam.builder()
          .pvtMotionNoConfidence(0.51)
          .pvtCommitteeNormal(0.51)
          .pvtCommitteeNoConfidence(0.51)
          .pvtHardForkInitiation(0.51)
          .pvtPPSecurityGroup(0.51)
          .dvtMotionNoConfidence(0.67)
          .dvtCommitteeNormal(0.67)
          .dvtCommitteeNoConfidence(0.6)
          .dvtUpdateToConstitution(0.75)
          .dvtHardForkInitiation(0.6)
          .dvtPPNetworkGroup(0.67)
          .dvtPPEconomicGroup(0.67)
          .dvtPPTechnicalGroup(0.67)
          .dvtPPGovGroup(0.75)
          .dvtTreasuryWithdrawal(0.67)
          .committeeMinSize(BigInteger.valueOf(7))
          .committeeMaxTermLength(BigInteger.valueOf(146))
          .govActionLifetime(BigInteger.valueOf(6))
          .govActionDeposit(BigInteger.valueOf(100000000000L))
          .drepDeposit(BigInteger.valueOf(500000000))
          .drepActivity(BigInteger.valueOf(20))
          .ccThreshold(0.67)
          .minFeeRefScriptCostPerByte(15.0)
          .costModel(CostModel.builder()
                  .hash("genesis.conway")
                  .costs("{\"PlutusV3\":[100788,420,1,1,1000,173,0,1,1000,59957,4,1,11183,32,201305,8356,4,16000,100,16000,100,16000,100,16000,100,16000,100,16000,100,100,100,16000,100,94375,32,132994,32,61462,4,72010,178,0,1,22151,32,91189,769,4,2,85848,123203,7305,-900,1716,549,57,85848,0,1,1,1000,42921,4,2,24548,29498,38,1,898148,27279,1,51775,558,1,39184,1000,60594,1,141895,32,83150,32,15299,32,76049,1,13169,4,22100,10,28999,74,1,28999,74,1,43285,552,1,44749,541,1,33852,32,68246,32,72362,32,7243,32,7391,32,11546,32,85848,123203,7305,-900,1716,549,57,85848,0,1,90434,519,0,1,74433,32,85848,123203,7305,-900,1716,549,57,85848,0,1,1,85848,123203,7305,-900,1716,549,57,85848,0,1,955506,213312,0,2,270652,22588,4,1457325,64566,4,20467,1,4,0,141992,32,100788,420,1,1,81663,32,59498,32,20142,32,24588,32,20744,32,25933,32,24623,32,43053543,10,53384111,14333,10,43574283,26308,10,16000,100,16000,100,962335,18,2780678,6,442008,1,52538055,3756,18,267929,18,76433006,8868,18,52948122,18,1995836,36,3227919,12,901022,1,166917843,4307,36,284546,36,158221314,26549,36,74698472,36,333849714,1,254006273,72,2174038,72,2261318,64571,4,207616,8310,4,1293828,28716,63,0,1,1006041,43623,251,0,1]}").build())
          .build();

      Assertions.assertEquals(conwayGenesis.hashCode(), data.getConway().hashCode());
  }

  @Test
  @Profile("internet")
  void testGenesisPreprodInternetFetching() {
    GenesisData data = GenesisData.builder()
        .txOuts(new ArrayList<>())
        .txs(new ArrayList<>())
        .build();

    ReflectionTestUtils.setField(genesisDataService, "genesisHash",
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937");
    ReflectionTestUtils.setField(genesisDataService, "genesisByron",
        "https://book.world.dev.cardano.org/environments/preprod/byron-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisShelley",
        "https://book.world.dev.cardano.org/environments/preprod/shelley-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisAlonzo",
        "https://book.world.dev.cardano.org/environments/preprod/alonzo-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisConway",
            "https://book.world.dev.cardano.org/environments/mainnet/conway-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisFetching", genesisWebClientFetching);

    genesisDataService.fetchTransactionAndTransactionOutput(data);
    genesisDataService.fetchShelleyGenesis(data);
    genesisDataService.fetchAlonzoGenesis(data);
    genesisDataService.setupBabbageGenesis(data);
    genesisDataService.fetchConwayGenesis(data);
    genesisDataService.fetchBlockAndSlotLeader(data);
    // check block
    Assertions.assertEquals(
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937",
        data.getBlock().getHash());
    // check slot leader
    Assertions.assertEquals(
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d573",
        data.getSlotLeaders().get(0).getHash());
    // check tx
    Assertions.assertEquals(8, data.getTxs().size());
    Tx firstTx = data.getTxs().get(0);
    Assertions.assertEquals("8e0280beebc3d12626e87b182f4205d75e49981042f54081cd35f3a4a85630b0",
        firstTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(0L), firstTx.getOutSum());

    Tx middleTx = data.getTxs().get(2);
    Assertions.assertEquals("5526b1373acfc774794a62122f95583ff17febb2ca8a0fe948d097e29cf99099",
        middleTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(30000000000000000L), middleTx.getOutSum());

    Tx lastTx = data.getTxs().get(7);
    Assertions.assertEquals("b731574b44de062ade1e70d0040abde47a6626c7d8e98816a9d87e6bd6228b45",
        lastTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(0L), lastTx.getOutSum());
    // check txout
    Assertions.assertEquals(8, data.getTxOuts().size());

    TxOut firstTxOut = data.getTxOuts().get(0);
    Assertions.assertEquals("FHnt4NL7yPXhCzCHVywZLqVsvwuG3HvwmjKXQJBrXh3h2aigv6uxkePbpzRNV8q",
        firstTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(0L), firstTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxYzA1NmQ4OTA3YjQ1MzBkYWJlYzBhYjc3NDU2YTJiNWM3ZTY5NTE1MGQ3NTM0MzgwYTgwOTMwOTFlYTEwMjQxMDEwMDFhZTBhZjg3ZGU"),
        HexUtil.encodeHexString(firstTxOut.getAddressRaw()).getBytes()));

    TxOut middleTxOut = data.getTxOuts().get(2);
    Assertions.assertEquals("FHnt4NL7yPXuYUxBF33VX5dZMBDAab2kvSNLRzCskvuKNCSDknzrQvKeQhGUw5a",
        middleTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(30000000000000000L), middleTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxYzU4ZDIxMTQxODRjNWVkNzg0Y2M0MTllYjY4MGE3ZDMzYzY5OGQwZWM5NjNlMmU4MDVlODNlMjYwYTEwMjQxMDEwMDFhNzRjMDJhMDk="),
        HexUtil.encodeHexString(middleTxOut.getAddressRaw()).getBytes()));

    TxOut lastTxOut = data.getTxOuts().get(7);
    Assertions.assertEquals("FHnt4NL7yPYJiN5Y8VsQr6LP6YgN51BHBPegNjVwKkq6AooCkbTpfZ2bqkVkfXU",
        lastTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(0L), lastTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxY2Y1NzNmZTFlMGY4ZGU0MGQzOTYzMmVlNmRhNmQ3ZGM1YTRlNDNkMjk3NzZlYjkwMzQ5ZmU2MzBjYTEwMjQxMDEwMDFhYmUwMjcwOTc"),
        HexUtil.encodeHexString(lastTxOut.getAddressRaw()).getBytes()));

    // shelley protocol
    EpochParam shelleyGenesis = EpochParam.builder()
        .minFeeA(44)
        .minFeeB(155381)
        .protocolMinor(0)
        .protocolMajor(2)
        .decentralisation(1.0)
        .maxEpoch(18)
        .extraEntropy("NeutralNonce")
        .maxTxSize(16384)
        .maxBlockSize(65536)
        .maxBhSize(1100)
        .minUtxoValue(BigInteger.valueOf(1000000L))
        .poolDeposit(BigInteger.valueOf(500000000L))
        .minPoolCost(BigInteger.valueOf(340000000L))
        .keyDeposit(BigInteger.valueOf(2000000L))
        .optimalPoolCount(150)
        .influence(0.3)
        .treasuryGrowthRate(0.2)
        .monetaryExpandRate(0.003)
        .build();

    Assertions.assertEquals(shelleyGenesis.hashCode(), data.getShelley().hashCode());
    // alonzo protocol
    EpochParam alonzoGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(34482L))
        .priceMem((double) 577 / 10000)
        .priceStep((double) 721 / 10000000)
        .maxTxExMem(BigInteger.valueOf(10000000L))
        .maxTxExSteps(BigInteger.valueOf(10000000000L))
        .maxBlockExMem(BigInteger.valueOf(50000000L))
        .maxBlockExSteps(BigInteger.valueOf(40000000000L))
        .maxValSize(BigInteger.valueOf(5000))
        .collateralPercent(150)
        .maxCollateralInputs(3)
        .costModel(CostModel.builder()
            .costs(
                "{\"PlutusV1\":{\"sha2_256-memory-arguments\":4,\"equalsString-cpu-arguments-constant\":1000,\"cekDelayCost-exBudgetMemory\":100,\"lessThanEqualsByteString-cpu-arguments-intercept\":103599,\"divideInteger-memory-arguments-minimum\":1,\"appendByteString-cpu-arguments-slope\":621,\"blake2b-cpu-arguments-slope\":29175,\"iData-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-slope\":1000,\"unBData-cpu-arguments\":150000,\"multiplyInteger-cpu-arguments-intercept\":61516,\"cekConstCost-exBudgetMemory\":100,\"nullList-cpu-arguments\":150000,\"equalsString-cpu-arguments-intercept\":150000,\"trace-cpu-arguments\":150000,\"mkNilData-memory-arguments\":32,\"lengthOfByteString-cpu-arguments\":150000,\"cekBuiltinCost-exBudgetCPU\":29773,\"bData-cpu-arguments\":150000,\"subtractInteger-cpu-arguments-slope\":0,\"unIData-cpu-arguments\":150000,\"consByteString-memory-arguments-intercept\":0,\"divideInteger-memory-arguments-slope\":1,\"divideInteger-cpu-arguments-model-arguments-slope\":118,\"listData-cpu-arguments\":150000,\"headList-cpu-arguments\":150000,\"chooseData-memory-arguments\":32,\"equalsInteger-cpu-arguments-intercept\":136542,\"sha3_256-cpu-arguments-slope\":82363,\"sliceByteString-cpu-arguments-slope\":5000,\"unMapData-cpu-arguments\":150000,\"lessThanInteger-cpu-arguments-intercept\":179690,\"mkCons-cpu-arguments\":150000,\"appendString-memory-arguments-intercept\":0,\"modInteger-cpu-arguments-model-arguments-slope\":118,\"ifThenElse-cpu-arguments\":1,\"mkNilPairData-cpu-arguments\":150000,\"lessThanEqualsInteger-cpu-arguments-intercept\":145276,\"addInteger-memory-arguments-slope\":1,\"chooseList-memory-arguments\":32,\"constrData-memory-arguments\":32,\"decodeUtf8-cpu-arguments-intercept\":150000,\"equalsData-memory-arguments\":1,\"subtractInteger-memory-arguments-slope\":1,\"appendByteString-memory-arguments-intercept\":0,\"lengthOfByteString-memory-arguments\":4,\"headList-memory-arguments\":32,\"listData-memory-arguments\":32,\"consByteString-cpu-arguments-intercept\":150000,\"unIData-memory-arguments\":32,\"remainderInteger-memory-arguments-minimum\":1,\"bData-memory-arguments\":32,\"lessThanByteString-cpu-arguments-slope\":248,\"encodeUtf8-memory-arguments-intercept\":0,\"cekStartupCost-exBudgetCPU\":100,\"multiplyInteger-memory-arguments-intercept\":0,\"unListData-memory-arguments\":32,\"remainderInteger-cpu-arguments-model-arguments-slope\":118,\"cekVarCost-exBudgetCPU\":29773,\"remainderInteger-memory-arguments-slope\":1,\"cekForceCost-exBudgetCPU\":29773,\"sha2_256-cpu-arguments-slope\":29175,\"equalsInteger-memory-arguments\":1,\"indexByteString-memory-arguments\":1,\"addInteger-memory-arguments-intercept\":1,\"chooseUnit-cpu-arguments\":150000,\"sndPair-cpu-arguments\":150000,\"cekLamCost-exBudgetCPU\":29773,\"fstPair-cpu-arguments\":150000,\"quotientInteger-memory-arguments-minimum\":1,\"decodeUtf8-cpu-arguments-slope\":1000,\"lessThanInteger-memory-arguments\":1,\"lessThanEqualsInteger-cpu-arguments-slope\":1366,\"fstPair-memory-arguments\":32,\"modInteger-memory-arguments-intercept\":0,\"unConstrData-cpu-arguments\":150000,\"lessThanEqualsInteger-memory-arguments\":1,\"chooseUnit-memory-arguments\":32,\"sndPair-memory-arguments\":32,\"addInteger-cpu-arguments-intercept\":197209,\"decodeUtf8-memory-arguments-slope\":8,\"equalsData-cpu-arguments-intercept\":150000,\"mapData-cpu-arguments\":150000,\"mkPairData-cpu-arguments\":150000,\"quotientInteger-cpu-arguments-constant\":148000,\"consByteString-memory-arguments-slope\":1,\"cekVarCost-exBudgetMemory\":100,\"indexByteString-cpu-arguments\":150000,\"unListData-cpu-arguments\":150000,\"equalsInteger-cpu-arguments-slope\":1326,\"cekStartupCost-exBudgetMemory\":100,\"subtractInteger-cpu-arguments-intercept\":197209,\"divideInteger-cpu-arguments-model-arguments-intercept\":425507,\"divideInteger-memory-arguments-intercept\":0,\"cekForceCost-exBudgetMemory\":100,\"blake2b-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-constant\":148000,\"tailList-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-intercept\":150000,\"equalsString-cpu-arguments-slope\":1000,\"lessThanByteString-memory-arguments\":1,\"multiplyInteger-cpu-arguments-slope\":11218,\"appendByteString-cpu-arguments-intercept\":396231,\"lessThanEqualsByteString-cpu-arguments-slope\":248,\"modInteger-memory-arguments-slope\":1,\"addInteger-cpu-arguments-slope\":0,\"equalsData-cpu-arguments-slope\":10000,\"decodeUtf8-memory-arguments-intercept\":0,\"chooseList-cpu-arguments\":150000,\"constrData-cpu-arguments\":150000,\"equalsByteString-memory-arguments\":1,\"cekApplyCost-exBudgetCPU\":29773,\"quotientInteger-memory-arguments-slope\":1,\"verifySignature-cpu-arguments-intercept\":3345831,\"unMapData-memory-arguments\":32,\"mkCons-memory-arguments\":32,\"sliceByteString-memory-arguments-slope\":1,\"sha3_256-memory-arguments\":4,\"ifThenElse-memory-arguments\":1,\"mkNilPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-slope\":247,\"appendString-cpu-arguments-intercept\":150000,\"quotientInteger-cpu-arguments-model-arguments-slope\":118,\"cekApplyCost-exBudgetMemory\":100,\"equalsString-memory-arguments\":1,\"multiplyInteger-memory-arguments-slope\":1,\"cekBuiltinCost-exBudgetMemory\":100,\"remainderInteger-memory-arguments-intercept\":0,\"sha2_256-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-model-arguments-intercept\":425507,\"lessThanEqualsByteString-memory-arguments\":1,\"tailList-memory-arguments\":32,\"mkNilData-cpu-arguments\":150000,\"chooseData-cpu-arguments\":150000,\"unBData-memory-arguments\":32,\"blake2b-memory-arguments\":4,\"iData-memory-arguments\":32,\"nullList-memory-arguments\":32,\"cekDelayCost-exBudgetCPU\":29773,\"subtractInteger-memory-arguments-intercept\":1,\"lessThanByteString-cpu-arguments-intercept\":103599,\"consByteString-cpu-arguments-slope\":1000,\"appendByteString-memory-arguments-slope\":1,\"trace-memory-arguments\":32,\"divideInteger-cpu-arguments-constant\":148000,\"cekConstCost-exBudgetCPU\":29773,\"encodeUtf8-memory-arguments-slope\":8,\"quotientInteger-cpu-arguments-model-arguments-intercept\":425507,\"mapData-memory-arguments\":32,\"appendString-cpu-arguments-slope\":1000,\"modInteger-cpu-arguments-constant\":148000,\"verifySignature-cpu-arguments-slope\":1,\"unConstrData-memory-arguments\":32,\"quotientInteger-memory-arguments-intercept\":0,\"equalsByteString-cpu-arguments-constant\":150000,\"sliceByteString-memory-arguments-intercept\":0,\"mkPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-intercept\":112536,\"appendString-memory-arguments-slope\":1,\"lessThanInteger-cpu-arguments-slope\":497,\"modInteger-cpu-arguments-model-arguments-intercept\":425507,\"modInteger-memory-arguments-minimum\":1,\"sha3_256-cpu-arguments-intercept\":0,\"verifySignature-memory-arguments\":1,\"cekLamCost-exBudgetMemory\":100,\"sliceByteString-cpu-arguments-intercept\":150000}}")
            .hash("144099ae3a42f67fa87a8b1a0144a7f243e6b87d8c6f080fc21c67fdd55d6478")
            .build())
        .minUtxoValue(null)
        .build();
    Assertions.assertEquals(alonzoGenesis, data.getAlonzo());
    // babbage protocol
    EpochParam babbageGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(COIN_PER_BYTE))
        .build();

    Assertions.assertEquals(babbageGenesis.hashCode(), data.getBabbage().hashCode());

    // conway protocol
    EpochParam conwayGenesis = EpochParam.builder()
      .pvtMotionNoConfidence(0.51)
      .pvtCommitteeNormal(0.51)
      .pvtCommitteeNoConfidence(0.51)
      .pvtHardForkInitiation(0.51)
      .pvtPPSecurityGroup(0.51)
      .dvtMotionNoConfidence(0.67)
      .dvtCommitteeNormal(0.67)
      .dvtCommitteeNoConfidence(0.6)
      .dvtUpdateToConstitution(0.75)
      .dvtHardForkInitiation(0.6)
      .dvtPPNetworkGroup(0.67)
      .dvtPPEconomicGroup(0.67)
      .dvtPPTechnicalGroup(0.67)
      .dvtPPGovGroup(0.75)
      .dvtTreasuryWithdrawal(0.67)
      .committeeMinSize(BigInteger.valueOf(7))
      .committeeMaxTermLength(BigInteger.valueOf(146))
      .govActionLifetime(BigInteger.valueOf(6))
      .govActionDeposit(BigInteger.valueOf(100000000000L))
      .drepDeposit(BigInteger.valueOf(500000000))
      .drepActivity(BigInteger.valueOf(20))
      .ccThreshold(0.67)
      .minFeeRefScriptCostPerByte(15.0)
      .costModel(CostModel.builder()
              .hash("genesis.conway")
              .costs("{\"PlutusV3\":[100788,420,1,1,1000,173,0,1,1000,59957,4,1,11183,32,201305,8356,4,16000,100,16000,100,16000,100,16000,100,16000,100,16000,100,100,100,16000,100,94375,32,132994,32,61462,4,72010,178,0,1,22151,32,91189,769,4,2,85848,123203,7305,-900,1716,549,57,85848,0,1,1,1000,42921,4,2,24548,29498,38,1,898148,27279,1,51775,558,1,39184,1000,60594,1,141895,32,83150,32,15299,32,76049,1,13169,4,22100,10,28999,74,1,28999,74,1,43285,552,1,44749,541,1,33852,32,68246,32,72362,32,7243,32,7391,32,11546,32,85848,123203,7305,-900,1716,549,57,85848,0,1,90434,519,0,1,74433,32,85848,123203,7305,-900,1716,549,57,85848,0,1,1,85848,123203,7305,-900,1716,549,57,85848,0,1,955506,213312,0,2,270652,22588,4,1457325,64566,4,20467,1,4,0,141992,32,100788,420,1,1,81663,32,59498,32,20142,32,24588,32,20744,32,25933,32,24623,32,43053543,10,53384111,14333,10,43574283,26308,10,16000,100,16000,100,962335,18,2780678,6,442008,1,52538055,3756,18,267929,18,76433006,8868,18,52948122,18,1995836,36,3227919,12,901022,1,166917843,4307,36,284546,36,158221314,26549,36,74698472,36,333849714,1,254006273,72,2174038,72,2261318,64571,4,207616,8310,4,1293828,28716,63,0,1,1006041,43623,251,0,1]}")
              .build())
      .build();

      Assertions.assertEquals(conwayGenesis.hashCode(), data.getConway().hashCode());
  }

  @Test
  void testGenesisMainnetLocal() {

    GenesisData data = GenesisData.builder()
        .txOuts(new ArrayList<>())
        .txs(new ArrayList<>())
        .build();
    ReflectionTestUtils.setField(genesisDataService, "genesisHash",
        "5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d432e940ebb");
    ReflectionTestUtils.setField(genesisDataService, "genesisByron",
        "classpath:networks/mainnet/byron-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisShelley",
        "classpath:networks/mainnet/shelley-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisAlonzo",
        "classpath:networks/mainnet/alonzo-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisConway",
        "classpath:networks/mainnet/conway-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisFetching", genesisLocalFetching);

    genesisDataService.fetchShelleyGenesis(data);
    genesisDataService.fetchAlonzoGenesis(data);
    genesisDataService.setupBabbageGenesis(data);
    genesisDataService.fetchConwayGenesis(data);
    genesisDataService.fetchTransactionAndTransactionOutput(data);
    genesisDataService.fetchBlockAndSlotLeader(data);

    Assertions.assertNotNull(data);
    Assertions.assertNotNull(data.getBlock());
    Assertions.assertNotNull(data.getTxs());
    Assertions.assertNotNull(data.getTxOuts());
    Assertions.assertNotNull(data.getSlotLeaders());
    Assertions.assertNotNull(data.getAlonzoCostModel());
    Assertions.assertNotNull(data.getShelley());
    Assertions.assertNotNull(data.getAlonzo());

    //check block
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d432e940ebb",
        data.getBlock().getHash());
    Assertions.assertEquals(14505, data.getBlock().getTxCount());
    Assertions.assertEquals(
        Timestamp.valueOf(LocalDateTime.ofEpochSecond(1506203091L, 0, ZoneOffset.UTC)),
        data.getStartTime());
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d43",
        data.getBlock().getSlotLeader().getHash());
    //check slot leader
    Assertions.assertEquals("5f20df933584822601f9e3f8c024eb5eb252fe8cefb24d1317dc3d43",
        data.getSlotLeaders().get(0).getHash());

    // check tx
    Assertions.assertEquals(14505, data.getTxs().size());

    Tx tx = data.getTxs().get(14504);
    Assertions.assertEquals("d234c34a92244cae87c5b9fb0b97fa3e89407769f411cc957340e9e3a2ac638e",
        tx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(648176763000000L), tx.getOutSum());

    Tx fistTx = data.getTxs().get(0);
    Assertions.assertEquals("8ee33c9906974706223d7d500d63bbee2369d7150f972757a9fdded2f706b938",
        fistTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(9999300000000L), fistTx.getOutSum());

    Tx middleTx = data.getTxs().get(7600);
    Assertions.assertEquals("c91f00be263c4aaa2880e30c773c3185669ae952e65770d88ecf264ca5a4ff4c",
        middleTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(385022000000L), middleTx.getOutSum());

    // check txout
    Assertions.assertEquals(14505, data.getTxOuts().size());

    TxOut txOut = data.getTxOuts().get(14504);
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWByAeZ+ktmy6xH0rzOFH3m6J+Yr+9Ljy3Vx6aNtyoAIavNR8oA==")
        , txOut.getAddressRaw()));
    Assertions.assertEquals(
        "Ae2tdPwUPEZ9dH9VC4iVXZRNYe5HGc73AKVMYHExpgYBmDMkgCUgnJGqqqq"
        , txOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(648176763000000L), txOut.getValue());

    TxOut fistTxOut = data.getTxOuts().get(0);
    Assertions.assertEquals("Ae2tdPwUPEZHFQnrr2dYB4GEQ8WVKspEyrg29pJ3f7qdjzaxjeShEEokF5f",
        fistTxOut.getAddress());
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWBzM33NbfVyv5E5l51qC/UMF/ncSkkqOEZb+/b3foAIanLwv/g=="),
        fistTxOut.getAddressRaw()));
    Assertions.assertEquals(BigInteger.valueOf(9999300000000L), fistTxOut.getValue());

    TxOut middleTxOut = data.getTxOuts().get(7600);
    Assertions.assertEquals("Ae2tdPwUPEZ9xCuFBz8BrhshnM5ZQaRnB7ez5LAp3uRXW9FBAvSXXWkJzQi",
        middleTxOut.getAddress());
    Assertions.assertTrue(Arrays.equals(
        Base64.getDecoder().decode("gtgYWCGDWByDvtsoFHc/ZLrdPMYy6v9PZedqajunmWcm96CvoAIaslOZWw=="),
        middleTxOut.getAddressRaw()));
    Assertions.assertEquals(BigInteger.valueOf(385022000000L), middleTxOut.getValue());

    // shelley protocol
    EpochParam shelleyGenesis = EpochParam.builder()
        .minFeeA(44)
        .minFeeB(155381)
        .protocolMinor(0)
        .protocolMajor(2)
        .decentralisation(1.0)
        .maxEpoch(18)
        .extraEntropy("NeutralNonce")
        .maxTxSize(16384)
        .maxBlockSize(65536)
        .maxBhSize(1100)
        .minUtxoValue(BigInteger.valueOf(1000000L))
        .poolDeposit(BigInteger.valueOf(500000000L))
        .minPoolCost(BigInteger.valueOf(340000000L))
        .keyDeposit(BigInteger.valueOf(2000000L))
        .optimalPoolCount(150)
        .influence(0.3)
        .treasuryGrowthRate(0.2)
        .monetaryExpandRate(0.003)
        .build();

    Assertions.assertEquals(shelleyGenesis.hashCode(), data.getShelley().hashCode());
    // alonzo protocol
    EpochParam alonzoGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(34482L))
        .priceMem((double) 577 / 10000)
        .priceStep((double) 721 / 10000000)
        .maxTxExMem(BigInteger.valueOf(10000000L))
        .maxTxExSteps(BigInteger.valueOf(10000000000L))
        .maxBlockExMem(BigInteger.valueOf(50000000L))
        .maxBlockExSteps(BigInteger.valueOf(40000000000L))
        .maxValSize(BigInteger.valueOf(5000))
        .collateralPercent(150)
        .maxCollateralInputs(3)
        .costModel(CostModel.builder()
            .costs(
                "{\"PlutusV1\":{\"sha2_256-memory-arguments\":4,\"equalsString-cpu-arguments-constant\":1000,\"cekDelayCost-exBudgetMemory\":100,\"lessThanEqualsByteString-cpu-arguments-intercept\":103599,\"divideInteger-memory-arguments-minimum\":1,\"appendByteString-cpu-arguments-slope\":621,\"blake2b-cpu-arguments-slope\":29175,\"iData-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-slope\":1000,\"unBData-cpu-arguments\":150000,\"multiplyInteger-cpu-arguments-intercept\":61516,\"cekConstCost-exBudgetMemory\":100,\"nullList-cpu-arguments\":150000,\"equalsString-cpu-arguments-intercept\":150000,\"trace-cpu-arguments\":150000,\"mkNilData-memory-arguments\":32,\"lengthOfByteString-cpu-arguments\":150000,\"cekBuiltinCost-exBudgetCPU\":29773,\"bData-cpu-arguments\":150000,\"subtractInteger-cpu-arguments-slope\":0,\"unIData-cpu-arguments\":150000,\"consByteString-memory-arguments-intercept\":0,\"divideInteger-memory-arguments-slope\":1,\"divideInteger-cpu-arguments-model-arguments-slope\":118,\"listData-cpu-arguments\":150000,\"headList-cpu-arguments\":150000,\"chooseData-memory-arguments\":32,\"equalsInteger-cpu-arguments-intercept\":136542,\"sha3_256-cpu-arguments-slope\":82363,\"sliceByteString-cpu-arguments-slope\":5000,\"unMapData-cpu-arguments\":150000,\"lessThanInteger-cpu-arguments-intercept\":179690,\"mkCons-cpu-arguments\":150000,\"appendString-memory-arguments-intercept\":0,\"modInteger-cpu-arguments-model-arguments-slope\":118,\"ifThenElse-cpu-arguments\":1,\"mkNilPairData-cpu-arguments\":150000,\"lessThanEqualsInteger-cpu-arguments-intercept\":145276,\"addInteger-memory-arguments-slope\":1,\"chooseList-memory-arguments\":32,\"constrData-memory-arguments\":32,\"decodeUtf8-cpu-arguments-intercept\":150000,\"equalsData-memory-arguments\":1,\"subtractInteger-memory-arguments-slope\":1,\"appendByteString-memory-arguments-intercept\":0,\"lengthOfByteString-memory-arguments\":4,\"headList-memory-arguments\":32,\"listData-memory-arguments\":32,\"consByteString-cpu-arguments-intercept\":150000,\"unIData-memory-arguments\":32,\"remainderInteger-memory-arguments-minimum\":1,\"bData-memory-arguments\":32,\"lessThanByteString-cpu-arguments-slope\":248,\"encodeUtf8-memory-arguments-intercept\":0,\"cekStartupCost-exBudgetCPU\":100,\"multiplyInteger-memory-arguments-intercept\":0,\"unListData-memory-arguments\":32,\"remainderInteger-cpu-arguments-model-arguments-slope\":118,\"cekVarCost-exBudgetCPU\":29773,\"remainderInteger-memory-arguments-slope\":1,\"cekForceCost-exBudgetCPU\":29773,\"sha2_256-cpu-arguments-slope\":29175,\"equalsInteger-memory-arguments\":1,\"indexByteString-memory-arguments\":1,\"addInteger-memory-arguments-intercept\":1,\"chooseUnit-cpu-arguments\":150000,\"sndPair-cpu-arguments\":150000,\"cekLamCost-exBudgetCPU\":29773,\"fstPair-cpu-arguments\":150000,\"quotientInteger-memory-arguments-minimum\":1,\"decodeUtf8-cpu-arguments-slope\":1000,\"lessThanInteger-memory-arguments\":1,\"lessThanEqualsInteger-cpu-arguments-slope\":1366,\"fstPair-memory-arguments\":32,\"modInteger-memory-arguments-intercept\":0,\"unConstrData-cpu-arguments\":150000,\"lessThanEqualsInteger-memory-arguments\":1,\"chooseUnit-memory-arguments\":32,\"sndPair-memory-arguments\":32,\"addInteger-cpu-arguments-intercept\":197209,\"decodeUtf8-memory-arguments-slope\":8,\"equalsData-cpu-arguments-intercept\":150000,\"mapData-cpu-arguments\":150000,\"mkPairData-cpu-arguments\":150000,\"quotientInteger-cpu-arguments-constant\":148000,\"consByteString-memory-arguments-slope\":1,\"cekVarCost-exBudgetMemory\":100,\"indexByteString-cpu-arguments\":150000,\"unListData-cpu-arguments\":150000,\"equalsInteger-cpu-arguments-slope\":1326,\"cekStartupCost-exBudgetMemory\":100,\"subtractInteger-cpu-arguments-intercept\":197209,\"divideInteger-cpu-arguments-model-arguments-intercept\":425507,\"divideInteger-memory-arguments-intercept\":0,\"cekForceCost-exBudgetMemory\":100,\"blake2b-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-constant\":148000,\"tailList-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-intercept\":150000,\"equalsString-cpu-arguments-slope\":1000,\"lessThanByteString-memory-arguments\":1,\"multiplyInteger-cpu-arguments-slope\":11218,\"appendByteString-cpu-arguments-intercept\":396231,\"lessThanEqualsByteString-cpu-arguments-slope\":248,\"modInteger-memory-arguments-slope\":1,\"addInteger-cpu-arguments-slope\":0,\"equalsData-cpu-arguments-slope\":10000,\"decodeUtf8-memory-arguments-intercept\":0,\"chooseList-cpu-arguments\":150000,\"constrData-cpu-arguments\":150000,\"equalsByteString-memory-arguments\":1,\"cekApplyCost-exBudgetCPU\":29773,\"quotientInteger-memory-arguments-slope\":1,\"verifySignature-cpu-arguments-intercept\":3345831,\"unMapData-memory-arguments\":32,\"mkCons-memory-arguments\":32,\"sliceByteString-memory-arguments-slope\":1,\"sha3_256-memory-arguments\":4,\"ifThenElse-memory-arguments\":1,\"mkNilPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-slope\":247,\"appendString-cpu-arguments-intercept\":150000,\"quotientInteger-cpu-arguments-model-arguments-slope\":118,\"cekApplyCost-exBudgetMemory\":100,\"equalsString-memory-arguments\":1,\"multiplyInteger-memory-arguments-slope\":1,\"cekBuiltinCost-exBudgetMemory\":100,\"remainderInteger-memory-arguments-intercept\":0,\"sha2_256-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-model-arguments-intercept\":425507,\"lessThanEqualsByteString-memory-arguments\":1,\"tailList-memory-arguments\":32,\"mkNilData-cpu-arguments\":150000,\"chooseData-cpu-arguments\":150000,\"unBData-memory-arguments\":32,\"blake2b-memory-arguments\":4,\"iData-memory-arguments\":32,\"nullList-memory-arguments\":32,\"cekDelayCost-exBudgetCPU\":29773,\"subtractInteger-memory-arguments-intercept\":1,\"lessThanByteString-cpu-arguments-intercept\":103599,\"consByteString-cpu-arguments-slope\":1000,\"appendByteString-memory-arguments-slope\":1,\"trace-memory-arguments\":32,\"divideInteger-cpu-arguments-constant\":148000,\"cekConstCost-exBudgetCPU\":29773,\"encodeUtf8-memory-arguments-slope\":8,\"quotientInteger-cpu-arguments-model-arguments-intercept\":425507,\"mapData-memory-arguments\":32,\"appendString-cpu-arguments-slope\":1000,\"modInteger-cpu-arguments-constant\":148000,\"verifySignature-cpu-arguments-slope\":1,\"unConstrData-memory-arguments\":32,\"quotientInteger-memory-arguments-intercept\":0,\"equalsByteString-cpu-arguments-constant\":150000,\"sliceByteString-memory-arguments-intercept\":0,\"mkPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-intercept\":112536,\"appendString-memory-arguments-slope\":1,\"lessThanInteger-cpu-arguments-slope\":497,\"modInteger-cpu-arguments-model-arguments-intercept\":425507,\"modInteger-memory-arguments-minimum\":1,\"sha3_256-cpu-arguments-intercept\":0,\"verifySignature-memory-arguments\":1,\"cekLamCost-exBudgetMemory\":100,\"sliceByteString-cpu-arguments-intercept\":150000}}")
            .hash("144099ae3a42f67fa87a8b1a0144a7f243e6b87d8c6f080fc21c67fdd55d6478")
            .build())
        .minUtxoValue(null)
        .build();
    Assertions.assertEquals(alonzoGenesis, data.getAlonzo());
    // babbage protocol
    EpochParam babbageGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(COIN_PER_BYTE))
        .build();

    Assertions.assertEquals(babbageGenesis.hashCode(), data.getBabbage().hashCode());

    // conway protocol
    EpochParam conwayGenesis = EpochParam.builder()
        .pvtMotionNoConfidence(0.51)
        .pvtCommitteeNormal(0.51)
        .pvtCommitteeNoConfidence(0.51)
        .pvtHardForkInitiation(0.51)
        .pvtPPSecurityGroup(0.51)
        .dvtMotionNoConfidence(0.67)
        .dvtCommitteeNormal(0.67)
        .dvtCommitteeNoConfidence(0.6)
        .dvtUpdateToConstitution(0.75)
        .dvtHardForkInitiation(0.6)
        .dvtPPNetworkGroup(0.67)
        .dvtPPEconomicGroup(0.67)
        .dvtPPTechnicalGroup(0.67)
        .dvtPPGovGroup(0.75)
        .dvtTreasuryWithdrawal(0.67)
        .committeeMinSize(BigInteger.valueOf(7))
        .committeeMaxTermLength(BigInteger.valueOf(146))
        .govActionLifetime(BigInteger.valueOf(6))
        .govActionDeposit(BigInteger.valueOf(100000000000L))
        .drepDeposit(BigInteger.valueOf(500000000))
        .drepActivity(BigInteger.valueOf(20))
        .ccThreshold(0.67)
        .minFeeRefScriptCostPerByte(15.0)
        .costModel(CostModel.builder()
                .hash("genesis.conway")
                .costs("{\"PlutusV3\":[100788,420,1,1,1000,173,0,1,1000,59957,4,1,11183,32,201305,8356,4,16000,100,16000,100,16000,100,16000,100,16000,100,16000,100,100,100,16000,100,94375,32,132994,32,61462,4,72010,178,0,1,22151,32,91189,769,4,2,85848,123203,7305,-900,1716,549,57,85848,0,1,1,1000,42921,4,2,24548,29498,38,1,898148,27279,1,51775,558,1,39184,1000,60594,1,141895,32,83150,32,15299,32,76049,1,13169,4,22100,10,28999,74,1,28999,74,1,43285,552,1,44749,541,1,33852,32,68246,32,72362,32,7243,32,7391,32,11546,32,85848,123203,7305,-900,1716,549,57,85848,0,1,90434,519,0,1,74433,32,85848,123203,7305,-900,1716,549,57,85848,0,1,1,85848,123203,7305,-900,1716,549,57,85848,0,1,955506,213312,0,2,270652,22588,4,1457325,64566,4,20467,1,4,0,141992,32,100788,420,1,1,81663,32,59498,32,20142,32,24588,32,20744,32,25933,32,24623,32,43053543,10,53384111,14333,10,43574283,26308,10,16000,100,16000,100,962335,18,2780678,6,442008,1,52538055,3756,18,267929,18,76433006,8868,18,52948122,18,1995836,36,3227919,12,901022,1,166917843,4307,36,284546,36,158221314,26549,36,74698472,36,333849714,1,254006273,72,2174038,72,2261318,64571,4,207616,8310,4,1293828,28716,63,0,1,1006041,43623,251,0,1]}").build())
        .build();

    Assertions.assertEquals(conwayGenesis.hashCode(), data.getConway().hashCode());
  }

  @Test
  void testGenesisPreProdLocal() {

    GenesisData data = GenesisData.builder()
        .txOuts(new ArrayList<>())
        .txs(new ArrayList<>())
        .build();

    ReflectionTestUtils.setField(genesisDataService, "genesisHash",
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937");
    ReflectionTestUtils.setField(genesisDataService, "genesisByron",
        "classpath:networks/preprod/byron-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisShelley",
        "classpath:networks/preprod/shelley-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisAlonzo",
        "classpath:networks/preprod/alonzo-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisConway",
            "classpath:networks/preprod/conway-genesis.json");
    ReflectionTestUtils.setField(genesisDataService, "genesisFetching", genesisLocalFetching);

    genesisDataService.fetchTransactionAndTransactionOutput(data);
    genesisDataService.fetchShelleyGenesis(data);
    genesisDataService.fetchAlonzoGenesis(data);
    genesisDataService.setupBabbageGenesis(data);
    genesisDataService.fetchBlockAndSlotLeader(data);
    genesisDataService.fetchConwayGenesis(data);
    // check block
    Assertions.assertEquals(
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937",
        data.getBlock().getHash());
    // check slot leader
    Assertions.assertEquals(
        "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d573",
        data.getSlotLeaders().get(0).getHash());
    // check tx
    Assertions.assertEquals(8, data.getTxs().size());
    Tx firstTx = data.getTxs().get(0);
    Assertions.assertEquals("8e0280beebc3d12626e87b182f4205d75e49981042f54081cd35f3a4a85630b0",
        firstTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(0L), firstTx.getOutSum());

    Tx middleTx = data.getTxs().get(2);
    Assertions.assertEquals("5526b1373acfc774794a62122f95583ff17febb2ca8a0fe948d097e29cf99099",
        middleTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(30000000000000000L), middleTx.getOutSum());

    Tx lastTx = data.getTxs().get(7);
    Assertions.assertEquals("b731574b44de062ade1e70d0040abde47a6626c7d8e98816a9d87e6bd6228b45",
        lastTx.getHash());
    Assertions.assertEquals(BigInteger.valueOf(0L), lastTx.getOutSum());
    // check txout
    Assertions.assertEquals(8, data.getTxOuts().size());

    TxOut firstTxOut = data.getTxOuts().get(0);
    Assertions.assertEquals("FHnt4NL7yPXhCzCHVywZLqVsvwuG3HvwmjKXQJBrXh3h2aigv6uxkePbpzRNV8q",
        firstTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(0L), firstTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxYzA1NmQ4OTA3YjQ1MzBkYWJlYzBhYjc3NDU2YTJiNWM3ZTY5NTE1MGQ3NTM0MzgwYTgwOTMwOTFlYTEwMjQxMDEwMDFhZTBhZjg3ZGU"),
        HexUtil.encodeHexString(firstTxOut.getAddressRaw()).getBytes()));

    TxOut middleTxOut = data.getTxOuts().get(2);
    Assertions.assertEquals("FHnt4NL7yPXuYUxBF33VX5dZMBDAab2kvSNLRzCskvuKNCSDknzrQvKeQhGUw5a",
        middleTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(30000000000000000L), middleTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxYzU4ZDIxMTQxODRjNWVkNzg0Y2M0MTllYjY4MGE3ZDMzYzY5OGQwZWM5NjNlMmU4MDVlODNlMjYwYTEwMjQxMDEwMDFhNzRjMDJhMDk="),
        HexUtil.encodeHexString(middleTxOut.getAddressRaw()).getBytes()));

    TxOut lastTxOut = data.getTxOuts().get(7);
    Assertions.assertEquals("FHnt4NL7yPYJiN5Y8VsQr6LP6YgN51BHBPegNjVwKkq6AooCkbTpfZ2bqkVkfXU",
        lastTxOut.getAddress());
    Assertions.assertEquals(BigInteger.valueOf(0L), lastTxOut.getValue());
    Assertions.assertTrue(Arrays.equals(Base64.getDecoder().decode(
            "ODJkODE4NTgyNDgzNTgxY2Y1NzNmZTFlMGY4ZGU0MGQzOTYzMmVlNmRhNmQ3ZGM1YTRlNDNkMjk3NzZlYjkwMzQ5ZmU2MzBjYTEwMjQxMDEwMDFhYmUwMjcwOTc"),
        HexUtil.encodeHexString(lastTxOut.getAddressRaw()).getBytes()));

    // shelley protocol
    EpochParam shelleyGenesis = EpochParam.builder()
        .minFeeA(44)
        .minFeeB(155381)
        .protocolMinor(0)
        .protocolMajor(2)
        .decentralisation(1.0)
        .maxEpoch(18)
        .extraEntropy("NeutralNonce")
        .maxTxSize(16384)
        .maxBlockSize(65536)
        .maxBhSize(1100)
        .minUtxoValue(BigInteger.valueOf(1000000L))
        .poolDeposit(BigInteger.valueOf(500000000L))
        .minPoolCost(BigInteger.valueOf(340000000L))
        .keyDeposit(BigInteger.valueOf(2000000L))
        .optimalPoolCount(150)
        .influence(0.3)
        .treasuryGrowthRate(0.2)
        .monetaryExpandRate(0.003)
        .build();

    Assertions.assertEquals(shelleyGenesis.hashCode(), data.getShelley().hashCode());
    // alonzo protocol
    EpochParam alonzoGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(34482L))
        .priceMem((double) 577 / 10000)
        .priceStep((double) 721 / 10000000)
        .maxTxExMem(BigInteger.valueOf(10000000L))
        .maxTxExSteps(BigInteger.valueOf(10000000000L))
        .maxBlockExMem(BigInteger.valueOf(50000000L))
        .maxBlockExSteps(BigInteger.valueOf(40000000000L))
        .maxValSize(BigInteger.valueOf(5000))
        .collateralPercent(150)
        .maxCollateralInputs(3)
        .costModel(CostModel.builder()
            .costs(
                "{\"PlutusV1\":{\"sha2_256-memory-arguments\":4,\"equalsString-cpu-arguments-constant\":1000,\"cekDelayCost-exBudgetMemory\":100,\"lessThanEqualsByteString-cpu-arguments-intercept\":103599,\"divideInteger-memory-arguments-minimum\":1,\"appendByteString-cpu-arguments-slope\":621,\"blake2b-cpu-arguments-slope\":29175,\"iData-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-slope\":1000,\"unBData-cpu-arguments\":150000,\"multiplyInteger-cpu-arguments-intercept\":61516,\"cekConstCost-exBudgetMemory\":100,\"nullList-cpu-arguments\":150000,\"equalsString-cpu-arguments-intercept\":150000,\"trace-cpu-arguments\":150000,\"mkNilData-memory-arguments\":32,\"lengthOfByteString-cpu-arguments\":150000,\"cekBuiltinCost-exBudgetCPU\":29773,\"bData-cpu-arguments\":150000,\"subtractInteger-cpu-arguments-slope\":0,\"unIData-cpu-arguments\":150000,\"consByteString-memory-arguments-intercept\":0,\"divideInteger-memory-arguments-slope\":1,\"divideInteger-cpu-arguments-model-arguments-slope\":118,\"listData-cpu-arguments\":150000,\"headList-cpu-arguments\":150000,\"chooseData-memory-arguments\":32,\"equalsInteger-cpu-arguments-intercept\":136542,\"sha3_256-cpu-arguments-slope\":82363,\"sliceByteString-cpu-arguments-slope\":5000,\"unMapData-cpu-arguments\":150000,\"lessThanInteger-cpu-arguments-intercept\":179690,\"mkCons-cpu-arguments\":150000,\"appendString-memory-arguments-intercept\":0,\"modInteger-cpu-arguments-model-arguments-slope\":118,\"ifThenElse-cpu-arguments\":1,\"mkNilPairData-cpu-arguments\":150000,\"lessThanEqualsInteger-cpu-arguments-intercept\":145276,\"addInteger-memory-arguments-slope\":1,\"chooseList-memory-arguments\":32,\"constrData-memory-arguments\":32,\"decodeUtf8-cpu-arguments-intercept\":150000,\"equalsData-memory-arguments\":1,\"subtractInteger-memory-arguments-slope\":1,\"appendByteString-memory-arguments-intercept\":0,\"lengthOfByteString-memory-arguments\":4,\"headList-memory-arguments\":32,\"listData-memory-arguments\":32,\"consByteString-cpu-arguments-intercept\":150000,\"unIData-memory-arguments\":32,\"remainderInteger-memory-arguments-minimum\":1,\"bData-memory-arguments\":32,\"lessThanByteString-cpu-arguments-slope\":248,\"encodeUtf8-memory-arguments-intercept\":0,\"cekStartupCost-exBudgetCPU\":100,\"multiplyInteger-memory-arguments-intercept\":0,\"unListData-memory-arguments\":32,\"remainderInteger-cpu-arguments-model-arguments-slope\":118,\"cekVarCost-exBudgetCPU\":29773,\"remainderInteger-memory-arguments-slope\":1,\"cekForceCost-exBudgetCPU\":29773,\"sha2_256-cpu-arguments-slope\":29175,\"equalsInteger-memory-arguments\":1,\"indexByteString-memory-arguments\":1,\"addInteger-memory-arguments-intercept\":1,\"chooseUnit-cpu-arguments\":150000,\"sndPair-cpu-arguments\":150000,\"cekLamCost-exBudgetCPU\":29773,\"fstPair-cpu-arguments\":150000,\"quotientInteger-memory-arguments-minimum\":1,\"decodeUtf8-cpu-arguments-slope\":1000,\"lessThanInteger-memory-arguments\":1,\"lessThanEqualsInteger-cpu-arguments-slope\":1366,\"fstPair-memory-arguments\":32,\"modInteger-memory-arguments-intercept\":0,\"unConstrData-cpu-arguments\":150000,\"lessThanEqualsInteger-memory-arguments\":1,\"chooseUnit-memory-arguments\":32,\"sndPair-memory-arguments\":32,\"addInteger-cpu-arguments-intercept\":197209,\"decodeUtf8-memory-arguments-slope\":8,\"equalsData-cpu-arguments-intercept\":150000,\"mapData-cpu-arguments\":150000,\"mkPairData-cpu-arguments\":150000,\"quotientInteger-cpu-arguments-constant\":148000,\"consByteString-memory-arguments-slope\":1,\"cekVarCost-exBudgetMemory\":100,\"indexByteString-cpu-arguments\":150000,\"unListData-cpu-arguments\":150000,\"equalsInteger-cpu-arguments-slope\":1326,\"cekStartupCost-exBudgetMemory\":100,\"subtractInteger-cpu-arguments-intercept\":197209,\"divideInteger-cpu-arguments-model-arguments-intercept\":425507,\"divideInteger-memory-arguments-intercept\":0,\"cekForceCost-exBudgetMemory\":100,\"blake2b-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-constant\":148000,\"tailList-cpu-arguments\":150000,\"encodeUtf8-cpu-arguments-intercept\":150000,\"equalsString-cpu-arguments-slope\":1000,\"lessThanByteString-memory-arguments\":1,\"multiplyInteger-cpu-arguments-slope\":11218,\"appendByteString-cpu-arguments-intercept\":396231,\"lessThanEqualsByteString-cpu-arguments-slope\":248,\"modInteger-memory-arguments-slope\":1,\"addInteger-cpu-arguments-slope\":0,\"equalsData-cpu-arguments-slope\":10000,\"decodeUtf8-memory-arguments-intercept\":0,\"chooseList-cpu-arguments\":150000,\"constrData-cpu-arguments\":150000,\"equalsByteString-memory-arguments\":1,\"cekApplyCost-exBudgetCPU\":29773,\"quotientInteger-memory-arguments-slope\":1,\"verifySignature-cpu-arguments-intercept\":3345831,\"unMapData-memory-arguments\":32,\"mkCons-memory-arguments\":32,\"sliceByteString-memory-arguments-slope\":1,\"sha3_256-memory-arguments\":4,\"ifThenElse-memory-arguments\":1,\"mkNilPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-slope\":247,\"appendString-cpu-arguments-intercept\":150000,\"quotientInteger-cpu-arguments-model-arguments-slope\":118,\"cekApplyCost-exBudgetMemory\":100,\"equalsString-memory-arguments\":1,\"multiplyInteger-memory-arguments-slope\":1,\"cekBuiltinCost-exBudgetMemory\":100,\"remainderInteger-memory-arguments-intercept\":0,\"sha2_256-cpu-arguments-intercept\":2477736,\"remainderInteger-cpu-arguments-model-arguments-intercept\":425507,\"lessThanEqualsByteString-memory-arguments\":1,\"tailList-memory-arguments\":32,\"mkNilData-cpu-arguments\":150000,\"chooseData-cpu-arguments\":150000,\"unBData-memory-arguments\":32,\"blake2b-memory-arguments\":4,\"iData-memory-arguments\":32,\"nullList-memory-arguments\":32,\"cekDelayCost-exBudgetCPU\":29773,\"subtractInteger-memory-arguments-intercept\":1,\"lessThanByteString-cpu-arguments-intercept\":103599,\"consByteString-cpu-arguments-slope\":1000,\"appendByteString-memory-arguments-slope\":1,\"trace-memory-arguments\":32,\"divideInteger-cpu-arguments-constant\":148000,\"cekConstCost-exBudgetCPU\":29773,\"encodeUtf8-memory-arguments-slope\":8,\"quotientInteger-cpu-arguments-model-arguments-intercept\":425507,\"mapData-memory-arguments\":32,\"appendString-cpu-arguments-slope\":1000,\"modInteger-cpu-arguments-constant\":148000,\"verifySignature-cpu-arguments-slope\":1,\"unConstrData-memory-arguments\":32,\"quotientInteger-memory-arguments-intercept\":0,\"equalsByteString-cpu-arguments-constant\":150000,\"sliceByteString-memory-arguments-intercept\":0,\"mkPairData-memory-arguments\":32,\"equalsByteString-cpu-arguments-intercept\":112536,\"appendString-memory-arguments-slope\":1,\"lessThanInteger-cpu-arguments-slope\":497,\"modInteger-cpu-arguments-model-arguments-intercept\":425507,\"modInteger-memory-arguments-minimum\":1,\"sha3_256-cpu-arguments-intercept\":0,\"verifySignature-memory-arguments\":1,\"cekLamCost-exBudgetMemory\":100,\"sliceByteString-cpu-arguments-intercept\":150000}}")
            .hash("144099ae3a42f67fa87a8b1a0144a7f243e6b87d8c6f080fc21c67fdd55d6478")
            .build())
        .minUtxoValue(null)
        .build();
    Assertions.assertEquals(alonzoGenesis, data.getAlonzo());
    // babbage protocol
    EpochParam babbageGenesis = EpochParam.builder()
        .coinsPerUtxoSize(BigInteger.valueOf(COIN_PER_BYTE))
        .build();

    Assertions.assertEquals(babbageGenesis.hashCode(), data.getBabbage().hashCode());

    // conway protocol
    EpochParam conwayGenesis = EpochParam.builder()
          .pvtMotionNoConfidence(0.51)
          .pvtCommitteeNormal(0.51)
          .pvtCommitteeNoConfidence(0.51)
          .pvtHardForkInitiation(0.51)
          .pvtPPSecurityGroup(0.51)
          .dvtMotionNoConfidence(0.67)
          .dvtCommitteeNormal(0.67)
          .dvtCommitteeNoConfidence(0.6)
          .dvtUpdateToConstitution(0.75)
          .dvtHardForkInitiation(0.6)
          .dvtPPNetworkGroup(0.67)
          .dvtPPEconomicGroup(0.67)
          .dvtPPTechnicalGroup(0.67)
          .dvtPPGovGroup(0.75)
          .dvtTreasuryWithdrawal(0.67)
          .committeeMinSize(BigInteger.valueOf(7))
          .committeeMaxTermLength(BigInteger.valueOf(146))
          .govActionLifetime(BigInteger.valueOf(6))
          .govActionDeposit(BigInteger.valueOf(100000000000L))
          .drepDeposit(BigInteger.valueOf(500000000))
          .drepActivity(BigInteger.valueOf(20))
          .ccThreshold(0.67)
          .minFeeRefScriptCostPerByte(15.0)
          .costModel(CostModel.builder()
                  .hash("genesis.conway")
                  .costs("{\"PlutusV3\":[100788,420,1,1,1000,173,0,1,1000,59957,4,1,11183,32,201305,8356,4,16000,100,16000,100,16000,100,16000,100,16000,100,16000,100,100,100,16000,100,94375,32,132994,32,61462,4,72010,178,0,1,22151,32,91189,769,4,2,85848,123203,7305,-900,1716,549,57,85848,0,1,1,1000,42921,4,2,24548,29498,38,1,898148,27279,1,51775,558,1,39184,1000,60594,1,141895,32,83150,32,15299,32,76049,1,13169,4,22100,10,28999,74,1,28999,74,1,43285,552,1,44749,541,1,33852,32,68246,32,72362,32,7243,32,7391,32,11546,32,85848,123203,7305,-900,1716,549,57,85848,0,1,90434,519,0,1,74433,32,85848,123203,7305,-900,1716,549,57,85848,0,1,1,85848,123203,7305,-900,1716,549,57,85848,0,1,955506,213312,0,2,270652,22588,4,1457325,64566,4,20467,1,4,0,141992,32,100788,420,1,1,81663,32,59498,32,20142,32,24588,32,20744,32,25933,32,24623,32,43053543,10,53384111,14333,10,43574283,26308,10,16000,100,16000,100,962335,18,2780678,6,442008,1,52538055,3756,18,267929,18,76433006,8868,18,52948122,18,1995836,36,3227919,12,901022,1,166917843,4307,36,284546,36,158221314,26549,36,74698472,36,333849714,1,254006273,72,2174038,72,2261318,64571,4,207616,8310,4,1293828,28716,63,0,1,1006041,43623,251,0,1]}")
                  .build())
             .build();

    Assertions.assertEquals(conwayGenesis.hashCode(), data.getConway().hashCode());
  }
}

