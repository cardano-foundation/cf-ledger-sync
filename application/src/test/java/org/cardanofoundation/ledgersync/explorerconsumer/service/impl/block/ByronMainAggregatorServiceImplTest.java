package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.bloxbean.cardano.yaci.core.model.Epoch;
import com.bloxbean.cardano.yaci.core.model.Era;
import com.bloxbean.cardano.yaci.core.model.byron.BlockVersion;
import com.bloxbean.cardano.yaci.core.model.byron.ByronAddress;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockBody;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockCons;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockExtraData;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockHead;
import com.bloxbean.cardano.yaci.core.model.byron.ByronMainBlock;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTx;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxIn;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxOut;
import com.bloxbean.cardano.yaci.core.model.byron.payload.ByronTxPayload;
import com.bloxbean.cardano.yaci.store.events.ByronMainBlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBatchBlockData;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.SlotLeaderRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.BlockDataServiceImpl;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.SlotLeaderServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ByronMainAggregatorServiceImplTest {

  @Mock
  ByronMainBlockEvent byronMainBlockEvent;

  @Mock
  EventMetadata eventMetadata;

  @Mock
  ByronMainBlock blockCddl;

  @Mock
  ByronBlockHead byronBlockHead;

  @Mock
  ByronBlockCons byronBlockCons;

  @Mock
  ByronBlockExtraData<String> byronBlockExtraData;

  @Mock
  BlockVersion blockVersion;

  @Mock
  ByronBlockBody byronMainBody;

  @Mock
  Epoch slotId;

  @Test
  @DisplayName("Aggregate an empty block")
  void aggregateEmptyBlockTest() {
    // Block 1 preprod
    int protocolMagic = 1;
    int epochNo = 0;
    long slotNo = 2L;
    long epochSlotNo = 2L;
    long blockNo = 1L;
//    int cborSize = 625;
    long blockTimeEpoch = 1654041640L;
    int protoMajor = 0;
    int protoMinor = 0;
    Era byronEra = Era.Byron;
    String blockHash = "1d031daf47281f69cd95ab929c269fd26b1434a56a5bbbd65b7afe85ef96b233";
    String prevBlockHash = "9ad7ff320c9cf74e0f5ee78d22a85ce42bb0a487d0506bf60cfb5a91ea4497d2";
    String pubKey = "3fb2637923c78dec4d5b932c09ac92ad445aa95da00a05edb447b40a07c59d5645187692f40e31114ad0f375532cf8e4f875740e2d7d0e343e0e14b533a31fec";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    Mockito.when(byronMainBlockEvent.getEventMetadata()).thenReturn(eventMetadata);
    Mockito.when(byronMainBlockEvent.getByronMainBlock()).thenReturn(blockCddl);
    Mockito.when(blockCddl.getBody()).thenReturn(byronMainBody);
    Mockito.when(byronMainBody.getTxPayload()).thenReturn(Collections.emptyList());
    Mockito.when(eventMetadata.getEra()).thenReturn(byronEra);
//    Mockito.when(eventMetadata.getNetwork()).thenReturn(network);
    Mockito.when(eventMetadata.getBlockHash()).thenReturn(blockHash);
    Mockito.when(blockCddl.getHeader()).thenReturn(byronBlockHead);
    Mockito.when(byronBlockHead.getConsensusData()).thenReturn(byronBlockCons);
    Mockito.when(byronBlockHead.getExtraData()).thenReturn(byronBlockExtraData);
    Mockito.when(byronBlockExtraData.getBlockVersion()).thenReturn(blockVersion);
    Mockito.when(blockVersion.getMajor()).thenReturn((short) protoMajor);
    Mockito.when(blockVersion.getMinor()).thenReturn((short) protoMinor);
    Mockito.when(byronBlockCons.getSlotId()).thenReturn(slotId);
    Mockito.when(byronBlockCons.getPubKey()).thenReturn(pubKey);
    Mockito.when(eventMetadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(eventMetadata.getSlot()).thenReturn(slotNo);
    Mockito.when(eventMetadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(eventMetadata.getBlock()).thenReturn(blockNo);
    Mockito.when(byronBlockHead.getPrevBlock()).thenReturn(prevBlockHash);
//    Mockito.when(blockCddl.getCborSize()).thenReturn(cborSize);
    Mockito.when(eventMetadata.getBlockTime()).thenReturn(blockTimeEpoch);

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    BlockDataService blockDataService = Mockito.mock(BlockDataService.class);
    ByronMainAggregatorServiceImpl victim =
        new ByronMainAggregatorServiceImpl(slotLeaderServiceImpl, blockDataService, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(byronMainBlockEvent);
    Assertions.assertEquals(byronEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals((int) epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(ConsumerConstant.BYRON_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("7adf1c1d73d04427ec798ab78c9416bcdf6f73fafd453cfe860ab035", aggregatedBlock.getSlotLeader().getHashRaw());
//    Assertions.assertEquals(cborSize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(0L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Collections.emptyList(), aggregatedBlock.getTxList());
    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
  }

  @Test
  @DisplayName("Aggregate block with tx")
  void aggregateBlockWithTxTest() {
    // Block 23265 mainnet
    int protocolMagic = 764824073;
    int epochNo = 1;
    long slotNo = 23278L;
    long epochSlotNo = 1678L;
    long blockNo = 23265L;
    int cborSize = 1157;
    long blockTimeEpoch = 1506668651L;
    int protoMajor = 0;
    int protoMinor = 0;

    Era byronEra = Era.Byron;
    String blockHash = "50019945fad138d607bbdc9da60e2d723d50d8044c609d81983610180c9e81bf";
    String prevBlockHash = "9517b366ba1cd2fc6b3f122e5c5e9fec9f2912748e4d6e289b3980fbcd63b112";
    String pubKey = "0bdb1f5ef3d994037593f2266255f134a564658bb2df814b3b9cefb96da34fa9c888591c85b770fd36726d5f3d991c668828affc7bbe0872fd699136e664d9d8";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    Mockito.when(byronMainBlockEvent.getEventMetadata()).thenReturn(eventMetadata);
    Mockito.when(byronMainBlockEvent.getByronMainBlock()).thenReturn(blockCddl);
    List<ByronTxPayload> byronTxPayloads = givenTxPayloadBlock23265Mainnet();
    Mockito.when(blockCddl.getBody()).thenReturn(byronMainBody);
    Mockito.when(byronMainBody.getTxPayload()).thenReturn(byronTxPayloads);
    Mockito.when(eventMetadata.getEra()).thenReturn(byronEra);
//    Mockito.when(block.getNetwork()).thenReturn(network);
    Mockito.when(eventMetadata.getBlockHash()).thenReturn(blockHash);
    Mockito.when(blockCddl.getHeader()).thenReturn(byronBlockHead);
    Mockito.when(byronBlockHead.getConsensusData()).thenReturn(byronBlockCons);
    Mockito.when(byronBlockHead.getExtraData()).thenReturn(byronBlockExtraData);
    Mockito.when(byronBlockExtraData.getBlockVersion()).thenReturn(blockVersion);
    Mockito.when(blockVersion.getMajor()).thenReturn((short) protoMajor);
    Mockito.when(blockVersion.getMinor()).thenReturn((short) protoMinor);
    Mockito.when(eventMetadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(byronBlockCons.getSlotId()).thenReturn(slotId);
    Mockito.when(byronBlockCons.getPubKey()).thenReturn(pubKey);
    Mockito.when(byronBlockCons.getSlotId()).thenReturn(slotId);
    Mockito.when(eventMetadata.getSlot()).thenReturn(slotNo);
    Mockito.when(eventMetadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(eventMetadata.getBlock()).thenReturn(blockNo);
    Mockito.when(byronBlockHead.getPrevBlock()).thenReturn(prevBlockHash);
//    Mockito.when(blockCddl.getCborSize()).thenReturn(cborSize);
    Mockito.when(eventMetadata.getBlockTime()).thenReturn(blockTimeEpoch);

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    AggregatedBatchBlockData aggregatedBatchBlockData = new AggregatedBatchBlockData();
    BlockDataServiceImpl blockDataServiceImpl = new BlockDataServiceImpl(aggregatedBatchBlockData);
    ByronMainAggregatorServiceImpl victim =
        new ByronMainAggregatorServiceImpl(slotLeaderServiceImpl, blockDataServiceImpl, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(byronMainBlockEvent);
    Assertions.assertEquals(byronEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals((int) epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(ConsumerConstant.BYRON_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("64c61078e9577d3b66ecd9e4e99fa9e5e1b5cf6097c11b23d139f6b8", aggregatedBlock.getSlotLeader().getHashRaw());
//    Assertions.assertEquals(cborSize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(2L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(2, aggregatedBlock.getTxList().size());
    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
  }

  private List<ByronTxPayload> givenTxPayloadBlock23265Mainnet() {
    ByronTxPayload byronTxPayload = Mockito.mock(ByronTxPayload.class);
    ByronTxPayload byronTxPayload2 = Mockito.mock(ByronTxPayload.class);
    ByronTx byronTx = Mockito.mock(ByronTx.class);
    ByronTx byronTx2 = Mockito.mock(ByronTx.class);
    ByronTxIn byronTxIn = Mockito.mock(ByronTxIn.class);
    ByronTxIn byronTxIn2 = Mockito.mock(ByronTxIn.class);
    ByronTxOut byronTxOut = Mockito.mock(ByronTxOut.class);
    ByronTxOut byronTxOut2 = Mockito.mock(ByronTxOut.class);
    ByronAddress byronAddress = Mockito.mock(ByronAddress.class);
    ByronAddress byronAddress2 = Mockito.mock(ByronAddress.class);

    // From tx with hash 8b3c52c91d190963482bf6623acd56ed25bae66ccb3a8e43225d6d31278ece75
    Mockito.when(byronTx.getTxHash()).thenReturn("8b3c52c91d190963482bf6623acd56ed25bae66ccb3a8e43225d6d31278ece75");
    Mockito.when(byronTxIn.getTxId()).thenReturn("a4c27eb40aa55d7b1a0fbd7fb513e4450d82af8dd62dfe773d967065edf280e3");
    Mockito.when(byronTxIn.getIndex()).thenReturn(0);
    Mockito.when(byronTxOut.getAddress()).thenReturn(byronAddress);
    Mockito.when(byronAddress.getBase58Raw()).thenReturn("DdzFFzCqrhsvZh368cyQhexi4WBPZ2BJYqdXfFf3F8RZPqXPy79r3pbovk3cR616dXFfaf6if4w18ncw9NUymu2r2R5ij64vPESdF7hW");
    Mockito.when(byronTxOut.getAmount()).thenReturn(BigInteger.valueOf(1922224000000L));
    Mockito.when(byronTx.getInputs()).thenReturn(List.of(byronTxIn));
    Mockito.when(byronTx.getOutputs()).thenReturn(List.of(byronTxOut));
    Mockito.when(byronTxPayload.getTransaction()).thenReturn(byronTx);

    // From tx with hash 00cba5fa9ecc5859d0323ea79849d34bec08ccee4a4dac469b52e79b9e5b44b3
    Mockito.when(byronTx2.getTxHash()).thenReturn("00cba5fa9ecc5859d0323ea79849d34bec08ccee4a4dac469b52e79b9e5b44b3");
    Mockito.when(byronTxIn2.getTxId()).thenReturn("63906bc6ad13a89230dcda3afcbea05391b4d395fbfa58e7b8d144a3a2b39d1a");
    Mockito.when(byronTxIn2.getIndex()).thenReturn(0);
    Mockito.when(byronTxOut2.getAddress()).thenReturn(byronAddress2);
    Mockito.when(byronAddress2.getBase58Raw()).thenReturn("DdzFFzCqrht7b2dihNqUkwwLDUGeXRTPgermEzTTiPL6zcfjHvVVKnTeoN5cBboQ1ydJeuUSegUzxJLckmBG47Xgh997rygjN8YgqWA7");
    Mockito.when(byronTxOut2.getAmount()).thenReturn(BigInteger.valueOf(1666666000000L));
    Mockito.when(byronTx2.getInputs()).thenReturn(List.of(byronTxIn2));
    Mockito.when(byronTx2.getOutputs()).thenReturn(List.of(byronTxOut2));
    Mockito.when(byronTxPayload2.getTransaction()).thenReturn(byronTx2);

    return List.of(byronTxPayload, byronTxPayload2);
  }
}
