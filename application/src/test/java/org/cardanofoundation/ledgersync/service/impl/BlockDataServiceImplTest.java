package org.cardanofoundation.ledgersync.service.impl;

import java.util.Map;

import org.springframework.data.util.Pair;

import org.cardanofoundation.ledgersync.aggregate.AggregatedBatchBlockData;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BlockDataServiceImplTest {

  BlockDataServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new BlockDataServiceImpl(new AggregatedBatchBlockData());
  }

  @Test
  @DisplayName("Save and get stake address with tx hash")
  void saveAndGetStakeAddressWithTxHashTest() {
    victim.saveFirstAppearedTxHashForStakeAddress(
        "e0968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6",
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758");
    victim.saveFirstAppearedTxHashForStakeAddress(
        "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2",
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758");
    victim.saveFirstAppearedTxHashForStakeAddress(
        "e0392ae9e068e55e8b5c27acc58b0bab8ea568c0aae2f6fc49be23a7ad",
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758");

    Map<String, String> stakeAddressTxHashMap = victim.getStakeAddressTxHashMap();
    Assertions.assertEquals(3, stakeAddressTxHashMap.size());
  }

  @Test
  @DisplayName("Save and get asset fingerprint's first appeared block number and tx idx")
  void saveAndGetFingerprintFirstAppearedBlockNoAndTxIdxTest() {
    String fingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    String fingerprint2 = "asset1u22slc95y2x5g7r38v3eaw705healz6x2skvr8";
    long blockNo = 177242L;
    long blockNo2 = 178176L;
    long txIdx = 1L;
    long txIdx2 = 0L;
    victim.setFingerprintFirstAppearedBlockNoAndTxIdx(fingerprint, blockNo, txIdx);
    victim.setFingerprintFirstAppearedBlockNoAndTxIdx(fingerprint2, blockNo2, txIdx2);

    Pair<Long, Long> firstAppearedBlockNoAndTxIdx = victim
        .getFingerprintFirstAppearedBlockNoAndTxIdx(fingerprint);
    Assertions.assertEquals(blockNo, firstAppearedBlockNoAndTxIdx.getFirst());
    Assertions.assertEquals(txIdx, firstAppearedBlockNoAndTxIdx.getSecond());

    Pair<Long, Long> firstAppearedBlockNoAndTxIdx2 = victim
        .getFingerprintFirstAppearedBlockNoAndTxIdx(fingerprint2);
    Assertions.assertEquals(blockNo2, firstAppearedBlockNoAndTxIdx2.getFirst());
    Assertions.assertEquals(txIdx2, firstAppearedBlockNoAndTxIdx2.getSecond());
  }

  @Test
  @DisplayName("Aggregated block saving test")
  void aggregatedBlockSavingTest() {
    String blockHash = "1d031daf47281f69cd95ab929c269fd26b1434a56a5bbbd65b7afe85ef96b233";
    String blockHash2 = "9972ffaee13b4afcf1a133434161ce25e8ecaf34b7a76e06b0c642125cf911a9";
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    AggregatedBlock aggregatedBlock2 = Mockito.mock(AggregatedBlock.class);

    Mockito.when(aggregatedBlock.getHash()).thenReturn(blockHash);
    Mockito.when(aggregatedBlock2.getHash()).thenReturn(blockHash2);

    victim.saveAggregatedBlock(aggregatedBlock);
    Assertions.assertEquals(aggregatedBlock, victim.getAggregatedBlock(blockHash));
    Assertions.assertEquals(1, victim.getBlockSize());
    Assertions.assertEquals(aggregatedBlock, victim.getFirstAndLastBlock().getFirst());
    Assertions.assertEquals(aggregatedBlock, victim.getFirstAndLastBlock().getSecond());

    victim.saveAggregatedBlock(aggregatedBlock2);
    Assertions.assertEquals(aggregatedBlock, victim.getAggregatedBlock(blockHash));
    Assertions.assertEquals(aggregatedBlock2, victim.getAggregatedBlock(blockHash2));
    Assertions.assertEquals(2, victim.getBlockSize());
    Assertions.assertEquals(aggregatedBlock, victim.getFirstAndLastBlock().getFirst());
    Assertions.assertEquals(aggregatedBlock2, victim.getFirstAndLastBlock().getSecond());
  }

  @Test
  @DisplayName("Asset fingerprint minted check test")
  void assetFingerprintMintedCheckTest() {
    String notMintedAssetFingerprint = "asset132r28qxkhg0wddjjpt2qffzd9m7g37arndlxsv";
    String assetNotMintedTxHash = "c4e64338c4bebe9a870abf946ffe46caede52e8b3d5831caf0717c016f70ea3e";

    // Nothing case
    Assertions.assertFalse(victim.isAssetFingerprintNotMintedInTx(notMintedAssetFingerprint, assetNotMintedTxHash));

    // Save and check
    victim.saveAssetFingerprintNotMintedAtTx(notMintedAssetFingerprint, assetNotMintedTxHash);
    Assertions.assertTrue(victim.isAssetFingerprintNotMintedInTx(notMintedAssetFingerprint, assetNotMintedTxHash));
  }

  @Test
  @DisplayName("Clear batch block data")
  void clearBatchBlockDataTest() {
    Assertions.assertDoesNotThrow(victim::clearBatchBlockData);
  }
}
