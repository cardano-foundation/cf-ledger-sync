package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.client.plutus.spec.ExUnits;
import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.Datum;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import com.bloxbean.cardano.yaci.core.model.certs.*;
import org.cardanofoundation.ledgersync.consumercommon.entity.RedeemerData;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptPurposeType;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.projection.TxOutProjection;
import org.cardanofoundation.ledgersync.repository.RedeemerDataRepository;
import org.cardanofoundation.ledgersync.repository.RedeemerRepository;
import org.cardanofoundation.ledgersync.repository.TxOutRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.math.BigInteger;
import java.util.*;

import static org.cardanofoundation.ledgersync.util.CertificateUtil.buildStakeDelegationCert;
import static org.cardanofoundation.ledgersync.util.CertificateUtil.buildStakeRegistrationCert;

@ExtendWith(MockitoExtension.class)
class RedeemerServiceImplTest {

    @Mock
    TxOutRepository txOutRepository;

    @Mock
    RedeemerRepository redeemerRepository;

    @Mock
    RedeemerDataRepository redeemerDataRepository;

    @Captor
    ArgumentCaptor<Collection<Redeemer>> redeemersCaptor;

    @Captor
    ArgumentCaptor<Collection<RedeemerData>> redeemerDataCaptor;

    RedeemerServiceImpl victim;

    @BeforeEach
    void setUp() {
        victim = new RedeemerServiceImpl(txOutRepository, redeemerRepository, redeemerDataRepository);
    }

    @Test
    @DisplayName("Should skip redeemer handling if no redeemer objects supplied")
    void shouldSkipRedeemerHandlingTest() {
        Map<String, Tx> txMap = Collections.emptyMap();
        Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();
        Collection<AggregatedTx> aggregatedTxs = Collections.emptyList();

        victim.handleRedeemers(aggregatedTxs, txMap, newTxOutMap);
        Mockito.verifyNoInteractions(txOutRepository);
        Mockito.verifyNoInteractions(redeemerRepository);
        Mockito.verifyNoInteractions(redeemerDataRepository);
    }

    @Test
    @DisplayName("Should handle redeemers successfully")
    void shouldHandleRedeemersSuccessfullyTest() {
        String txHash = "a4d36bf5bacf4a658dcb22b721c4d128239a5cb9dd1273f751ea2882cbace81f";
        String paymentCred = "168376d9c3f792610e5ecbfd2895de76f52d6ba06842e24d03814535";
        TxOutProjection txOutProjection = Mockito.mock(TxOutProjection.class);
        AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
        Witnesses witnesses = Mockito.mock(Witnesses.class);
        Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));

        Mockito.when(txOutProjection.getAddressHasScript()).thenReturn(Boolean.TRUE);
        Mockito.when(txOutProjection.getPaymentCred()).thenReturn(paymentCred);
        Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
        Mockito.when(aggregatedTx.getWitnesses()).thenReturn(witnesses);
        Mockito.when(aggregatedTx.getTxInputs()).thenReturn(givenAggregatedTxIns());
        List<Amount> mints = givenMints();
        Mockito.when(aggregatedTx.getMint()).thenReturn(mints);
        List<Certificate> certificates = givenCertificates();
        Mockito.when(aggregatedTx.getCertificates()).thenReturn(certificates);
        Map<String, BigInteger> withdrawal = givenWithdrawal();
        Mockito.when(aggregatedTx.getWithdrawals()).thenReturn(withdrawal);
        // mintRedeemer, certRedeemer, rewardRedeemer have the same datum data
        com.bloxbean.cardano.yaci.core.model.Redeemer spendRedeemer = givenSpendRedeemer();
        com.bloxbean.cardano.yaci.core.model.Redeemer mintRedeemer = givenMintRedeemer();
        com.bloxbean.cardano.yaci.core.model.Redeemer certRedeemer = givenCertRedeemer();
        com.bloxbean.cardano.yaci.core.model.Redeemer rewardRedeemer = givenRewardRedeemer();
        Mockito.when(witnesses.getRedeemers()).thenReturn(List.of(
                spendRedeemer, mintRedeemer, certRedeemer, rewardRedeemer
        ));
        Mockito.when(redeemerDataRepository.findAllByHashIn(Mockito.anySet()))
                .thenReturn(Collections.emptyList());
        Mockito.when(txOutRepository.findTxOutByTxHashAndTxOutIndex(
                        Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(Optional.of(txOutProjection));

        victim.handleRedeemers(List.of(aggregatedTx), txMap, Collections.emptyMap());

        Mockito.verify(redeemerRepository, Mockito.times(1))
                .saveAll(redeemersCaptor.capture());
        Mockito.verifyNoMoreInteractions(redeemerRepository);
        Mockito.verify(redeemerDataRepository, Mockito.times(1))
                .saveAll(redeemerDataCaptor.capture());
        Mockito.verifyNoMoreInteractions(redeemerDataRepository);

        Collection<RedeemerData> redeemerDataCollection = redeemerDataCaptor.getValue();
        Assertions.assertEquals(2, redeemerDataCollection.size());
        Collection<Redeemer> redeemers = redeemersCaptor.getValue();
        redeemers.forEach(redeemer -> {
            ScriptPurposeType purpose = redeemer.getPurpose();

            switch (purpose) {
                case SPEND -> {
                    Assertions.assertEquals(spendRedeemer.getIndex(), redeemer.getIndex());
                    Assertions.assertEquals(paymentCred, redeemer.getScriptHash());

                    ExUnits exUnits = spendRedeemer.getExUnits();
                    Assertions.assertEquals(exUnits.getMem().longValue(), redeemer.getUnitMem());
                    Assertions.assertEquals(exUnits.getSteps().longValue(), redeemer.getUnitSteps());

                    Datum plutusData = spendRedeemer.getData();
                    RedeemerData redeemerData = redeemer.getRedeemerData();
                    Assertions.assertEquals(plutusData.getHash(), redeemerData.getHash());
                    Assertions.assertEquals(plutusData.getJson(), redeemerData.getValue());
                    Assertions.assertEquals(
                            plutusData.getCbor(),
                            HexUtil.encodeHexString(redeemerData.getBytes())
                    );
                }
                case MINT -> {
                    Assertions.assertEquals(mintRedeemer.getIndex(), redeemer.getIndex());
                    Amount mint = mints.get(redeemer.getIndex());
                    Assertions.assertEquals(mint.getPolicyId(), redeemer.getScriptHash());

                    ExUnits exUnits = mintRedeemer.getExUnits();
                    Assertions.assertEquals(exUnits.getMem().longValue(), redeemer.getUnitMem());
                    Assertions.assertEquals(exUnits.getSteps().longValue(), redeemer.getUnitSteps());

                    Datum plutusData = mintRedeemer.getData();
                    RedeemerData redeemerData = redeemer.getRedeemerData();
                    Assertions.assertEquals(plutusData.getHash(), redeemerData.getHash());
                    Assertions.assertEquals(plutusData.getJson(), redeemerData.getValue());
                    Assertions.assertEquals(
                            plutusData.getCbor(),
                            HexUtil.encodeHexString(redeemerData.getBytes())
                    );
                }
                case CERT -> {
                    Assertions.assertEquals(certRedeemer.getIndex(), redeemer.getIndex());
                    Certificate certificate = certificates.get(redeemer.getIndex());
                    Assertions.assertEquals(
                            certificate.getType() == CertificateType.STAKE_DELEGATION ?
                                    ((StakeDelegation) certificate).getStakeCredential().getHash() :
                                    ((StakeDeregistration) certificate).getStakeCredential().getHash(),
                            redeemer.getScriptHash());

                    ExUnits exUnits = certRedeemer.getExUnits();
                    Assertions.assertEquals(exUnits.getMem().longValue(), redeemer.getUnitMem());
                    Assertions.assertEquals(exUnits.getSteps().longValue(), redeemer.getUnitSteps());

                    Datum plutusData = certRedeemer.getData();
                    RedeemerData redeemerData = redeemer.getRedeemerData();
                    Assertions.assertEquals(plutusData.getHash(), redeemerData.getHash());
                    Assertions.assertEquals(plutusData.getJson(), redeemerData.getValue());
                    Assertions.assertEquals(
                            plutusData.getCbor(),
                            HexUtil.encodeHexString(redeemerData.getBytes())
                    );
                }
                case REWARD -> {
                    Assertions.assertEquals(rewardRedeemer.getIndex(), redeemer.getIndex());
                    String rewardAccount = new ArrayList<>(withdrawal.keySet()).get(redeemer.getIndex());
                    // Trim network tag
                    String scriptHash = rewardAccount.substring(2);
                    Assertions.assertEquals(scriptHash, redeemer.getScriptHash());

                    ExUnits exUnits = rewardRedeemer.getExUnits();
                    Assertions.assertEquals(exUnits.getMem().longValue(), redeemer.getUnitMem());
                    Assertions.assertEquals(exUnits.getSteps().longValue(), redeemer.getUnitSteps());

                    Datum plutusData = rewardRedeemer.getData();
                    RedeemerData redeemerData = redeemer.getRedeemerData();
                    Assertions.assertEquals(plutusData.getHash(), redeemerData.getHash());
                    Assertions.assertEquals(plutusData.getJson(), redeemerData.getValue());
                    Assertions.assertEquals(
                            plutusData.getCbor(),
                            HexUtil.encodeHexString(redeemerData.getBytes())
                    );
                }
            }
        });
    }

    @Test
    @DisplayName("Should handle spend redeemer from fallback tx out successfully")
    void shouldHandleSpendRedeemerFromFallbackTxOutSuccessfullyTest() {
        String txHash = "a4d36bf5bacf4a658dcb22b721c4d128239a5cb9dd1273f751ea2882cbace81f";
        String paymentCred = "168376d9c3f792610e5ecbfd2895de76f52d6ba06842e24d03814535";
        AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
        Witnesses witnesses = Mockito.mock(Witnesses.class);
        Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
        TxOut txOut = Mockito.mock(TxOut.class);
        Map<Pair<String, Short>, TxOut> newTxOutMap = Map.of(
                Pair.of("06977fc2872331fb54510146aec1d2c367fa777208e3893cea2a2444991fbdb7", (short) 0),
                txOut
        );

        Mockito.when(txOut.getAddressHasScript()).thenReturn(Boolean.TRUE);
        Mockito.when(txOut.getPaymentCred()).thenReturn(paymentCred);
        Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
        Mockito.when(aggregatedTx.getWitnesses()).thenReturn(witnesses);
        Mockito.when(aggregatedTx.getTxInputs()).thenReturn(givenAggregatedTxIns());
        com.bloxbean.cardano.yaci.core.model.Redeemer spendRedeemer = givenSpendRedeemer();
        Mockito.when(witnesses.getRedeemers()).thenReturn(List.of(spendRedeemer));
        Mockito.when(redeemerDataRepository.findAllByHashIn(Mockito.anySet()))
                .thenReturn(Collections.emptyList());

        victim.handleRedeemers(List.of(aggregatedTx), txMap, newTxOutMap);

        Mockito.verify(redeemerRepository, Mockito.times(1))
                .saveAll(redeemersCaptor.capture());
        Mockito.verifyNoMoreInteractions(redeemerRepository);
        Mockito.verify(redeemerDataRepository, Mockito.times(1))
                .saveAll(redeemerDataCaptor.capture());
        Mockito.verifyNoMoreInteractions(redeemerDataRepository);

        Collection<RedeemerData> redeemerDataCollection = redeemerDataCaptor.getValue();
        Assertions.assertEquals(1, redeemerDataCollection.size());

        Collection<Redeemer> redeemers = redeemersCaptor.getValue();
        Assertions.assertEquals(1, redeemers.size());
        Redeemer redeemer = new ArrayList<>(
                redeemers).get(0);
        Assertions.assertEquals(spendRedeemer.getIndex(), redeemer.getIndex());
        Assertions.assertEquals(paymentCred, redeemer.getScriptHash());

        ExUnits exUnits = spendRedeemer.getExUnits();
        Assertions.assertEquals(exUnits.getMem().longValue(), redeemer.getUnitMem());
        Assertions.assertEquals(exUnits.getSteps().longValue(), redeemer.getUnitSteps());

        Datum plutusData = spendRedeemer.getData();
        RedeemerData redeemerData = redeemer.getRedeemerData();
        Assertions.assertEquals(plutusData.getHash(), redeemerData.getHash());
        Assertions.assertEquals(plutusData.getJson(), redeemerData.getValue());
        Assertions.assertEquals(
                plutusData.getCbor(),
                HexUtil.encodeHexString(redeemerData.getBytes())
        );
    }

    @Test
    @DisplayName("Should fail if cannot find tx in for spend redeemer")
    void shouldFailIfCannotFindTxInForSpendRedeemerTest() {
        String txHash = "a4d36bf5bacf4a658dcb22b721c4d128239a5cb9dd1273f751ea2882cbace81f";
        TxOutProjection txOutProjection = Mockito.mock(TxOutProjection.class);
        AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
        Witnesses witnesses = Mockito.mock(Witnesses.class);
        Map<String, Tx> txMap = Map.of(txHash, Mockito.mock(Tx.class));
        Map<Pair<String, Short>, TxOut> newTxOutMap = Collections.emptyMap();

        Mockito.when(txOutProjection.getAddressHasScript()).thenReturn(Boolean.FALSE);
        Mockito.when(aggregatedTx.getHash()).thenReturn(txHash);
        Mockito.when(aggregatedTx.getWitnesses()).thenReturn(witnesses);
        Mockito.when(aggregatedTx.getTxInputs()).thenReturn(givenAggregatedTxIns());
        com.bloxbean.cardano.yaci.core.model.Redeemer spendRedeemer = givenSpendRedeemer();
        Mockito.when(witnesses.getRedeemers()).thenReturn(List.of(spendRedeemer));
        Mockito.when(redeemerDataRepository.findAllByHashIn(Mockito.anySet()))
                .thenReturn(Collections.emptyList());
        Mockito.when(txOutRepository.findTxOutByTxHashAndTxOutIndex(
                        Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(Optional.of(txOutProjection));

        List<AggregatedTx> aggregatedTxList = List.of(aggregatedTx);
        Assertions.assertThrows(IllegalStateException.class, () ->
                victim.handleRedeemers(aggregatedTxList, txMap, newTxOutMap));

        Mockito.verifyNoMoreInteractions(txOutRepository);
        Mockito.verifyNoMoreInteractions(redeemerDataRepository);
        Mockito.verifyNoInteractions(redeemerRepository);
    }

    private static com.bloxbean.cardano.yaci.core.model.Redeemer givenSpendRedeemer() {
        Datum plutusData = Datum.builder()
                        .hash("923918e403bf43c34b4ef6b48eb2ee04babed17320d8d1b9ff9ad086e86f44ec")
                        .cbor("d87980")
                        .json("{\"constructor\":0,\"fields\":[]}")
                        .build();
        ExUnits exUnits = ExUnits.builder()
                .mem(BigInteger.valueOf(1801746L))
                .steps(BigInteger.valueOf(807371536L))
                .build();
        return com.bloxbean.cardano.yaci.core.model.Redeemer.builder()
                .tag(RedeemerTag.Spend)
                .index(0)
                .data(plutusData)
                .exUnits(exUnits)
                .build();
    }

    private static com.bloxbean.cardano.yaci.core.model.Redeemer givenMintRedeemer() {
        Datum plutusData = Datum.builder()
                        .hash("923918e403bf43c34b4ef6b48eb2ee04babed17320d8d1b9ff9ad086e86f44ec")
                        .cbor("d87980")
                        .json("{\"constructor\":0,\"fields\":[]}")
                        .build();
        ExUnits exUnits = ExUnits.builder()
                .mem(BigInteger.valueOf(881262L))
                .steps(BigInteger.valueOf(270069846L))
                .build();
        return com.bloxbean.cardano.yaci.core.model.Redeemer.builder()
                .tag(RedeemerTag.Mint)
                .index(1)
                .data(plutusData)
                .exUnits(exUnits)
                .build();
    }

    private static com.bloxbean.cardano.yaci.core.model.Redeemer givenCertRedeemer() {
        Datum plutusData = Datum.builder()
                        .hash("1b44214718f87314c393e4f1cd32b06bc7df47cf60925fe1ea7393551c170c85")
                        .cbor("d8799fff")
                        .json("{\"constructor\":0,\"fields\":[]}")
                        .build();
        ExUnits exUnits = ExUnits.builder()
                .mem(BigInteger.valueOf(289786L))
                .steps(BigInteger.valueOf(99413862L))
                .build();
        return com.bloxbean.cardano.yaci.core.model.Redeemer.builder()
                .tag(RedeemerTag.Cert)
                .index(1)
                .data(plutusData)
                .exUnits(exUnits)
                .build();
    }

    private static com.bloxbean.cardano.yaci.core.model.Redeemer givenRewardRedeemer() {
        Datum plutusData = Datum.builder()
                        .hash("923918e403bf43c34b4ef6b48eb2ee04babed17320d8d1b9ff9ad086e86f44ec")
                        .cbor("d87980")
                        .json("{\"constructor\":0,\"fields\":[]}")
                        .build();
        ExUnits exUnits = ExUnits.builder()
                .mem(BigInteger.valueOf(440050L))
                .steps(BigInteger.valueOf(135739306L))
                .build();
        return com.bloxbean.cardano.yaci.core.model.Redeemer.builder()
                .tag(RedeemerTag.Reward)
                .index(0)
                .data(plutusData)
                .exUnits(exUnits)
                .build();
    }


    private static Set<AggregatedTxIn> givenAggregatedTxIns() {
        return Set.of(
                new AggregatedTxIn(0, "aa80303b33aeb356d7df1355fee6af948be362b4220a0d5ad5e96ae73d9560ba",
                        null),
                new AggregatedTxIn(0, "ba7d913e1c92004ceaef0c20c3ca142ee397eab673c115bb35dfd4c447d94c07",
                        null),
                new AggregatedTxIn(0, "5f3ea4ca0cf7d93432de946d7d5e2d78df3a7f6bc5e2e7ccfdf19995a4e3b5e9",
                        null),
                new AggregatedTxIn(1, "cc784da3e4a1e04411c6c1c2abf1265218b208db39b19b5428ab6c384090d268",
                        null),
                new AggregatedTxIn(0, "ad7b8068953574670b14e0b95027c2132413eaee829466889a96eb24fec1e1cc",
                        null),
                new AggregatedTxIn(0, "06977fc2872331fb54510146aec1d2c367fa777208e3893cea2a2444991fbdb7",
                        null)
        );
    }

    private static List<Amount> givenMints() {
        return List.of(
                new Amount(
                        "1cf569e1ec3e0fee92f1f5002bfd4213b796c151c708db46e6e2d3a4.",
                        "1cf569e1ec3e0fee92f1f5002bfd4213b796c151c708db46e6e2d3a4",
                        "assetName1",
                        "assetName1".getBytes(),
                        BigInteger.ONE
                ),
                new Amount(
                        "8b14b900bbf9f43d911da209a28e7bd2cce500d8e4bc928c9ca714fb.",
                        "8b14b900bbf9f43d911da209a28e7bd2cce500d8e4bc928c9ca714fb",
                        "assetName2",
                        "assetName2".getBytes(),
                        BigInteger.ONE
                ),
                new Amount(
                        "b413bc466dadcb6bcf93e840a9eedabe04e83aa9d55f9deeb94d9743.",
                        "b413bc466dadcb6bcf93e840a9eedabe04e83aa9d55f9deeb94d9743",
                        "assetName3",
                        "assetName3".getBytes(),
                        BigInteger.ONE
                )
        );
    }

    private static List<Certificate> givenCertificates() {
        return List.of(
                buildStakeRegistrationCert(StakeCredType.SCRIPTHASH,
                        "5d3bc621c161b04734b95d4ed3189c24ddd9b3ab72898f491c7c8c25"),
                buildStakeDelegationCert(StakeCredType.SCRIPTHASH,
                        "5d3bc621c161b04734b95d4ed3189c24ddd9b3ab72898f491c7c8c25")
        );
    }

    private static Map<String, BigInteger> givenWithdrawal() {
        return Map.of("f01b8374d0b294a4a3399ff25f06a0707b16214f1e9b67cf89ba2454a6", BigInteger.ZERO);
    }
}
