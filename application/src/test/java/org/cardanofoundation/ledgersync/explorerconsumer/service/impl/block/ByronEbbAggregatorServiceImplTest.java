package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import com.bloxbean.cardano.yaci.core.model.Era;
import com.bloxbean.cardano.yaci.core.model.byron.ByronEbBlock;
import com.bloxbean.cardano.yaci.core.model.byron.ByronEbBlockCons;
import com.bloxbean.cardano.yaci.core.model.byron.ByronEbHead;
import com.bloxbean.cardano.yaci.store.events.ByronEbBlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SlotLeaderService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.ByronEbbAggregatorServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ByronEbbAggregatorServiceImplTest {

  @Test
  @DisplayName("Aggregate Byron EBB block")
  void aggregateBlockTest() {
    ByronEbBlockEvent byronEbBlockEvent = Mockito.mock(ByronEbBlockEvent.class);
    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    ByronEbBlock blockCddl = Mockito.mock(ByronEbBlock.class);
    Mockito.when(byronEbBlockEvent.getByronEbBlock()).thenReturn(blockCddl);
    Mockito.when(byronEbBlockEvent.getMetadata()).thenReturn(metadata);
    ByronEbHead byronEbHead = Mockito.mock(ByronEbHead.class);
//    ByronEbBlockCons byronEbBlockCons = Mockito.mock(ByronEbBlockCons.class);
    // Block 0 preprod
    int protocolMagic = 1;
    int epochNo = 0;
//    int cborSize = 83;
    long blockTimeEpoch = 1654041600L;
    Era byronEra = Era.Byron;
    String blockHash = "9ad7ff320c9cf74e0f5ee78d22a85ce42bb0a487d0506bf60cfb5a91ea4497d2";
    String prevBlockHash = "d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));

    Mockito.when(metadata.getEra()).thenReturn(byronEra);
    Mockito.when(metadata.getBlockHash()).thenReturn(blockHash);
    Mockito.when(blockCddl.getHeader()).thenReturn(byronEbHead);
//    Mockito.when(byronEbHead.getConsensusData()).thenReturn(byronEbBlockCons);
    Mockito.when(metadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(byronEbHead.getPrevBlock()).thenReturn(prevBlockHash);
//    Mockito.when(block.getCborSize()).thenReturn(cborSize);
    Mockito.when(metadata.getBlockTime()).thenReturn(blockTimeEpoch);

    SlotLeaderService slotLeaderService = Mockito.mock(SlotLeaderService.class);
    BlockDataService blockDataService = Mockito.mock(BlockDataService.class);
    ByronEbbAggregatorServiceImpl victim =
        new ByronEbbAggregatorServiceImpl(slotLeaderService, blockDataService, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(byronEbBlockEvent);
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(byronEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals(epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
//    Assertions.assertEquals(cborSize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(0, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(0, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Collections.emptyList(), aggregatedBlock.getTxList());
    Assertions.assertEquals(0, aggregatedBlock.getTxCount());
    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
  }
}
