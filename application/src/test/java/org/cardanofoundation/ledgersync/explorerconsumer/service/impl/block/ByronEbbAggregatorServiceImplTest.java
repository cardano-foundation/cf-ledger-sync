package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.common.common.byron.ByronEbBlock;
import org.cardanofoundation.ledgersync.common.common.byron.ByronEbBlockCons;
import org.cardanofoundation.ledgersync.common.common.byron.ByronEbHead;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SlotLeaderService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.ByronEbbAggregatorServiceImpl;
import org.mockito.Mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ByronEbbAggregatorServiceImplTest {

    //TODO: yaci refactor this
//  @Test
//  @DisplayName("Aggregate Byron EBB block")
//  void aggregateBlockTest() {
//    ByronEbBlock block = Mockito.mock(ByronEbBlock.class);
//    ByronEbHead byronEbHead = Mockito.mock(ByronEbHead.class);
//    ByronEbBlockCons byronEbBlockCons = Mockito.mock(ByronEbBlockCons.class);
//
//    // Block 0 preprod
//    int network = 1;
//    long epochNo = 0L;
//    int cborSize = 83;
//    long blockTimeEpoch = 1654041600L;
//    Era byronEra = Era.BYRON;
//    String blockHash = "9ad7ff320c9cf74e0f5ee78d22a85ce42bb0a487d0506bf60cfb5a91ea4497d2";
//    String prevBlockHash = "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937";
//    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
//        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
//
//    Mockito.when(block.getEraType()).thenReturn(byronEra);
//    Mockito.when(block.getNetwork()).thenReturn(network);
//    Mockito.when(block.getBlockHash()).thenReturn(blockHash);
//    Mockito.when(block.getHeader()).thenReturn(byronEbHead);
//    Mockito.when(byronEbHead.getConsensusData()).thenReturn(byronEbBlockCons);
//    Mockito.when(byronEbBlockCons.getEpochId()).thenReturn(epochNo);
//    Mockito.when(byronEbHead.getPrevBlock()).thenReturn(prevBlockHash);
//    Mockito.when(block.getCborSize()).thenReturn(cborSize);
//    Mockito.when(block.getBlockTime()).thenReturn(blockTimeEpoch);
//
//    SlotLeaderService slotLeaderService = Mockito.mock(SlotLeaderService.class);
//    BlockDataService blockDataService = Mockito.mock(BlockDataService.class);
//    ByronEbbAggregatorServiceImpl victim =
//        new ByronEbbAggregatorServiceImpl(slotLeaderService, blockDataService);
//    AggregatedBlock aggregatedBlock = victim.aggregateBlock(block);
//    Assertions.assertEquals(byronEra, aggregatedBlock.getEra());
//    Assertions.assertEquals(network, aggregatedBlock.getNetwork());
//    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
//    Assertions.assertEquals((int) epochNo, aggregatedBlock.getEpochNo());
//    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
//    Assertions.assertEquals(cborSize, aggregatedBlock.getBlockSize());
//    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
//    Assertions.assertEquals(0, aggregatedBlock.getProtoMajor());
//    Assertions.assertEquals(0, aggregatedBlock.getProtoMinor());
//    Assertions.assertEquals(Collections.emptyList(), aggregatedBlock.getTxList());
//    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
//  }
}
