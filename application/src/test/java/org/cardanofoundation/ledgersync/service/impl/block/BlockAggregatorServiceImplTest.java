package org.cardanofoundation.ledgersync.service.impl.block;

import co.nstant.in.cbor.model.Array;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.yaci.core.model.*;
import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.store.events.BlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import lombok.SneakyThrows;
import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.*;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.repository.SlotLeaderRepository;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.cardanofoundation.ledgersync.service.SlotLeaderService;
import org.cardanofoundation.ledgersync.service.impl.BlockDataServiceImpl;
import org.cardanofoundation.ledgersync.service.impl.SlotLeaderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

import static org.cardanofoundation.ledgersync.util.CertificateUtil.*;

@ExtendWith(MockitoExtension.class)
class BlockAggregatorServiceImplTest {

  @Mock
  BlockEvent blockEvent;

  @Mock
  BlockHeader blockHeader;

  @Mock
  HeaderBody headerBody;

  @Test
  @DisplayName("Aggregate an empty block")
  void aggregateEmptyBlockTest() {
    // Block 46 preprod
    int protocolMagic = 1;
    int epochNo = 4;
    long slotNo = 86400L;
    long epochSlotNo = 0L;
    long blockNo = 46L;
    long blockTimeEpoch = 1655769600L;
    int protoMajor = 3;
    int protoMinor = 0;
    long blockBodySize = 3L;
    Era shelleyEra = Era.Shelley;
    String blockHash = "c971bfb21d2732457f9febf79d9b02b20b9a3bef12c561a78b818bcb8b35a574";
    String prevBlockHash = "45899e8002b27df291e09188bfe3aeb5397ac03546a7d0ead93aa2500860f1af";
    String issuerVrfKey = "d1a8de6caa8fd9b175c59862ecdd5abcd0477b84b82a0e52faecc6b3c85100a4";
    String vrfKey = "51995f616f8a025f974b20330a53c0c81e8ea95973d73d15fff7bab57589311d";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    String opCert = "2b9a5add912f3edc5c325d6250b9cc154de8f35e2924f5b1c707a4123808d064";
    Integer opCertCounter = 0;

    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    Block blockCddl = Mockito.mock(Block.class);
    Mockito.when(blockEvent.getBlock()).thenReturn(blockCddl);
    Mockito.when(blockEvent.getMetadata()).thenReturn(metadata);
    OperationalCert operationalCert = Mockito.mock(OperationalCert.class);

    Mockito.when(blockCddl.getEra()).thenReturn(Era.Shelley);
    Mockito.when(blockCddl.getHeader()).thenReturn(blockHeader);
    Mockito.when(blockHeader.getHeaderBody()).thenReturn(headerBody);
    Mockito.when(metadata.getBlock()).thenReturn(blockNo);
    Mockito.when(metadata.getSlot()).thenReturn(slotNo);
    Mockito.when(metadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(metadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(headerBody.getPrevHash()).thenReturn(prevBlockHash);
    Mockito.when(headerBody.getIssuerVkey()).thenReturn(issuerVrfKey);
    Mockito.when(headerBody.getVrfVkey()).thenReturn(vrfKey);
    Mockito.when(headerBody.getBlockBodySize()).thenReturn(blockBodySize);
    Mockito.when(headerBody.getProtocolVersion())
        .thenReturn(new ProtocolVersion(protoMajor, protoMinor));
    Mockito.when(headerBody.getOperationalCert()).thenReturn(operationalCert);
    Mockito.when(operationalCert.getHotVKey()).thenReturn(opCert);
    Mockito.when(operationalCert.getSequenceNumber()).thenReturn(opCertCounter);
    Mockito.when(headerBody.getBlockHash()).thenReturn(blockHash);
    Mockito.when(metadata.getBlockTime()).thenReturn(blockTimeEpoch);

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    BlockDataService blockDataService = Mockito.mock(BlockDataService.class);
    BlockAggregatorServiceImpl victim =
        new BlockAggregatorServiceImpl(slotLeaderServiceImpl, blockDataService, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(blockEvent);
    Assertions.assertEquals(shelleyEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals(epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(
        ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d",
        aggregatedBlock.getSlotLeader().getHashRaw());
    Assertions.assertEquals(blockBodySize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(0L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Bech32.encode(
            HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX),
        aggregatedBlock.getVrfKey());
    Assertions.assertEquals(opCert, aggregatedBlock.getOpCert());
    Assertions.assertEquals((long) opCertCounter, aggregatedBlock.getOpCertCounter());
    Assertions.assertEquals(Collections.emptyList(), aggregatedBlock.getTxList());
    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
  }

  @Test
  @DisplayName("Aggregate block with tx")
  void aggregateBlockWithTxTest() {
    // Block 50 preprod
    int protocolMagic = 1;
    int epochNo = 4;
    long slotNo = 86480L;
    long epochSlotNo = 80L;
    long blockNo = 50L;
    long blockTimeEpoch = 1655769680L;
    int protoMajor = 3;
    int protoMinor = 0;
    long blockBodySize = 1157L;
    Era shelleyEra = Era.Shelley;
    String blockHash = "6b1c0c2ccd1fec376235c6580a667b67be92028e183dc46236eb551f1c40d621";
    String prevBlockHash = "216d41a801d018b991664886ed641958ca938023ef5817b2ea0d55e38430b406";
    String issuerVrfKey = "d4dd69a41071bc2dc8e64a97f4bd6379524ce0c2b665728043a067e34d3e218a";
    String vrfKey = "859cd08dd516e4db46ac84428d05b9fd7a20917a897fe3d8a49018dd2ce605f0";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    String opCert = "b2613a87a56751da1444807b534614cf4186d43c8b6965ee69853d36f8cd2c62";
    Integer opCertCounter = 0;
    OperationalCert operationalCert = Mockito.mock(OperationalCert.class);
    List<TransactionBody> transactionBodies = givenTxBodiesBlock50Preprod();
    List<Witnesses> witnesses = givenTxWitnessesBlock50Preprod();
    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    Block blockCddl = Mockito.mock(Block.class);
    Mockito.when(blockEvent.getBlock()).thenReturn(blockCddl);
    Mockito.when(blockEvent.getMetadata()).thenReturn(metadata);
    Mockito.when(blockCddl.getEra()).thenReturn(Era.Shelley);
    Mockito.when(blockCddl.getHeader()).thenReturn(blockHeader);
    Mockito.when(blockHeader.getHeaderBody()).thenReturn(headerBody);
    Mockito.when(metadata.getBlock()).thenReturn(blockNo);
    Mockito.when(metadata.getSlot()).thenReturn(slotNo);
    Mockito.when(metadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(metadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(headerBody.getPrevHash()).thenReturn(prevBlockHash);
    Mockito.when(headerBody.getIssuerVkey()).thenReturn(issuerVrfKey);
    Mockito.when(headerBody.getVrfVkey()).thenReturn(vrfKey);
    Mockito.when(headerBody.getBlockBodySize()).thenReturn(blockBodySize);
    Mockito.when(headerBody.getProtocolVersion())
        .thenReturn(new ProtocolVersion(protoMajor, protoMinor));
    Mockito.when(headerBody.getOperationalCert()).thenReturn(operationalCert);
    Mockito.when(operationalCert.getHotVKey()).thenReturn(opCert);
    Mockito.when(operationalCert.getSequenceNumber()).thenReturn(opCertCounter);
    Mockito.when(headerBody.getBlockHash()).thenReturn(blockHash);
    Mockito.when(metadata.getBlockTime()).thenReturn(blockTimeEpoch);
    Mockito.when(blockCddl.getTransactionBodies()).thenReturn(transactionBodies);
    Mockito.when(blockCddl.getTransactionWitness()).thenReturn(witnesses);

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    AggregatedBatchBlockData aggregatedBatchBlockData = new AggregatedBatchBlockData();
    BlockDataServiceImpl blockDataServiceImpl = new BlockDataServiceImpl(aggregatedBatchBlockData);
    BlockAggregatorServiceImpl victim =
        new BlockAggregatorServiceImpl(slotLeaderServiceImpl, blockDataServiceImpl, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(blockEvent);
    Assertions.assertEquals(shelleyEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals(epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(
        ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("de7ca985023cf892f4de7f5f1d0a7181668884752d9ebb9e96c95059",
        aggregatedBlock.getSlotLeader().getHashRaw());
    Assertions.assertEquals(blockBodySize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(1L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Bech32.encode(
            HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX),
        aggregatedBlock.getVrfKey());
    Assertions.assertEquals(opCert, aggregatedBlock.getOpCert());
    Assertions.assertEquals((long) opCertCounter, aggregatedBlock.getOpCertCounter());

    List<AggregatedTx> aggregatedTxList = aggregatedBlock.getTxList();
    Assertions.assertEquals(1L, aggregatedTxList.size());
    IntStream.range(0, transactionBodies.size()).forEach(idx -> {
      AggregatedTx aggregatedTx = aggregatedTxList.get(idx);
      Assertions.assertEquals(witnesses.get(idx), aggregatedTx.getWitnesses());
    });

    Assertions.assertEquals(Collections.emptyMap(), aggregatedBlock.getAuxiliaryDataMap());
  }

  @Test
  @DisplayName("Aggregate block with failed tx")
  void aggregateBlockWithFailedTxTest() {
    // Block 949034 preprod
    int protocolMagic = 1;
    int epochNo = 70;
    long slotNo = 28687610L;
    long epochSlotNo = 89210L;
    long blockNo = 949034L;
    long blockTimeEpoch = 1684370810L;
    int protoMajor = 8;
    int protoMinor = 0;
    long blockBodySize = 395L;
    Era babbageEra = Era.Babbage;
    String blockHash = "8555cbdfa779c65b1e86d22ebb69673a5504abe78abdde855fe686874eaa5301";
    String prevBlockHash = "ec25d747154d56c5d73b7f18450aa6c91ec82b459eaf7ca5bea3cbdfe5d48886";
    String issuerVrfKey = "9e4035bb7028fafe9df82db88d9534d0f7ec08121e570276465c6cd2dc888f0f";
    String vrfKey = "ca8491ea0847481804609a8b79aa6bf9dfeb20002777d90274ea28db73ff0dae";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    OperationalCert operationalCert = Mockito.mock(OperationalCert.class);
    String opCert = "c33c045370d6570ea884012fc54b510beb2d0660de98c52acd71af92585fb367";
    Integer opCertCounter = 1;
    List<TransactionBody> transactionBodies = givenTxBodiesBlock949034Preprod();
    List<Witnesses> witnesses = givenTxWitnessesBlock949034Preprod();
    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    Block blockCddl = Mockito.mock(Block.class);
    Mockito.when(blockEvent.getBlock()).thenReturn(blockCddl);
    Mockito.when(blockEvent.getMetadata()).thenReturn(metadata);
    Mockito.when(blockCddl.getEra()).thenReturn(Era.Babbage);
    Mockito.when(blockCddl.getHeader()).thenReturn(blockHeader);
    Mockito.when(blockHeader.getHeaderBody()).thenReturn(headerBody);
    Mockito.when(metadata.getBlock()).thenReturn(blockNo);
    Mockito.when(metadata.getSlot()).thenReturn(slotNo);
    Mockito.when(metadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(metadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(headerBody.getPrevHash()).thenReturn(prevBlockHash);
    Mockito.when(headerBody.getIssuerVkey()).thenReturn(issuerVrfKey);
    Mockito.when(headerBody.getVrfVkey()).thenReturn(vrfKey);
    Mockito.when(headerBody.getBlockBodySize()).thenReturn(blockBodySize);
    Mockito.when(headerBody.getProtocolVersion())
        .thenReturn(new ProtocolVersion(protoMajor, protoMinor));
    Mockito.when(headerBody.getOperationalCert()).thenReturn(operationalCert);
    Mockito.when(headerBody.getOperationalCert()).thenReturn(operationalCert);
    Mockito.when(operationalCert.getHotVKey()).thenReturn(opCert);
    Mockito.when(operationalCert.getSequenceNumber()).thenReturn(opCertCounter);
    Mockito.when(headerBody.getBlockHash()).thenReturn(blockHash);
    Mockito.when(metadata.getBlockTime()).thenReturn(blockTimeEpoch);
    Mockito.when(blockCddl.getTransactionBodies()).thenReturn(transactionBodies);
    Mockito.when(blockCddl.getTransactionWitness()).thenReturn(witnesses);
    Mockito.when(blockCddl.getInvalidTransactions()).thenReturn(List.of(0));

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    AggregatedBatchBlockData aggregatedBatchBlockData = new AggregatedBatchBlockData();
    BlockDataServiceImpl blockDataServiceImpl = new BlockDataServiceImpl(aggregatedBatchBlockData);
    BlockAggregatorServiceImpl victim =
        new BlockAggregatorServiceImpl(slotLeaderServiceImpl, blockDataServiceImpl, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(blockEvent);
    Assertions.assertEquals(babbageEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals(epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(
        ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("9b4c92eb5cb4c072a0e6670777e1bc8586a202bceebc702e81b1315e",
        aggregatedBlock.getSlotLeader().getHashRaw());
    Assertions.assertEquals(blockBodySize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(1L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Bech32.encode(
            HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX),
        aggregatedBlock.getVrfKey());
    Assertions.assertEquals(opCert, aggregatedBlock.getOpCert());
    Assertions.assertEquals((long) opCertCounter, aggregatedBlock.getOpCertCounter());

    List<AggregatedTx> aggregatedTxList = aggregatedBlock.getTxList();
    Assertions.assertEquals(1L, aggregatedTxList.size());
    AggregatedTx aggregatedTx = aggregatedTxList.get(0);
    TransactionBody transactionBody = transactionBodies.get(0);
    Assertions.assertFalse(aggregatedTx.isValidContract());

    TransactionOutput collateralReturn = transactionBody.getCollateralReturn();
    AggregatedTxOut aggregatedTxCollateralReturn = aggregatedTx.getCollateralReturn();
    Assertions.assertNotNull(aggregatedTxCollateralReturn);
    Assertions.assertEquals(1, aggregatedTxCollateralReturn.getIndex());

    AggregatedAddress aggregatedAddress = aggregatedTxCollateralReturn.getAddress();
    Assertions.assertNotNull(aggregatedAddress);
    Assertions.assertEquals(collateralReturn.getAddress(), aggregatedAddress.getAddress());
    Assertions.assertEquals(BigInteger.ZERO, aggregatedTxCollateralReturn.getNativeAmount()); //
  }

  @Test
  @DisplayName("Aggregate block with tx having cert")
  void aggregateBlockWithTxWithCertTest() {
    // Block 48 preprod
    int protocolMagic = 1;
    int epochNo = 4;
    long slotNo = 86440L;
    long epochSlotNo = 40L;
    long blockNo = 48L;
    long blockTimeEpoch = 1655769640L;
    int protoMajor = 3;
    int protoMinor = 0;
    long blockBodySize = 1880L;
    Era shelleyEra = Era.Shelley;
    String blockHash = "49ef96c51afd2fbef46a73e6b535d7aea10a079a84df19d730e1a127be7e76f2";
    String prevBlockHash = "664b6ec8a708b9cf90b87c904e688477887b55cbf4ee6c36877166a2ef216665";
    String issuerVrfKey = "9aae625d4d15bcb3733d420e064f1cd338f386e0af049fcd42b455a69d28ad36";
    String vrfKey = "990ed20a21e979e67ae7fd32c86cc6901fe6db52a71bdd3fe6cc45027699ea9f";
    Timestamp blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
        blockTimeEpoch, 0, ZoneOffset.ofHours(0)));
    OperationalCert operationalCert = Mockito.mock(OperationalCert.class);
    String opCert = "8c5c56fa647f8fc1b2bb165f7ac54b16d6cae30625f74f4cab86e048ba442a84";
    Integer opCertCounter = 0;

    List<TransactionBody> transactionBodies = givenTxBodiesBlock48Preprod();
    List<Witnesses> witnesses = givenTxWitnessesBlock48Preprod();
    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    Block blockCddl = Mockito.mock(Block.class);
    Mockito.when(blockEvent.getBlock()).thenReturn(blockCddl);
    Mockito.when(blockEvent.getMetadata()).thenReturn(metadata);

    Mockito.when(blockCddl.getEra()).thenReturn(Era.Shelley);
    Mockito.when(blockCddl.getHeader()).thenReturn(blockHeader);
    Mockito.when(blockHeader.getHeaderBody()).thenReturn(headerBody);
    Mockito.when(metadata.getBlock()).thenReturn(blockNo);
    Mockito.when(metadata.getSlot()).thenReturn(slotNo);
    Mockito.when(metadata.getEpochNumber()).thenReturn(epochNo);
    Mockito.when(metadata.getEpochSlot()).thenReturn(epochSlotNo);
    Mockito.when(headerBody.getPrevHash()).thenReturn(prevBlockHash);
    Mockito.when(headerBody.getIssuerVkey()).thenReturn(issuerVrfKey);
    Mockito.when(headerBody.getVrfVkey()).thenReturn(vrfKey);
    Mockito.when(headerBody.getBlockBodySize()).thenReturn(blockBodySize);
    Mockito.when(headerBody.getProtocolVersion())
        .thenReturn(new ProtocolVersion(protoMajor, protoMinor));
    Mockito.when(headerBody.getOperationalCert()).thenReturn(operationalCert);
    Mockito.when(operationalCert.getHotVKey()).thenReturn(opCert);
    Mockito.when(operationalCert.getSequenceNumber()).thenReturn(opCertCounter);
    Mockito.when(headerBody.getBlockHash()).thenReturn(blockHash);
    Mockito.when(metadata.getBlockTime()).thenReturn(blockTimeEpoch);
    Mockito.when(blockCddl.getTransactionBodies()).thenReturn(transactionBodies);
    Mockito.when(blockCddl.getTransactionWitness()).thenReturn(witnesses);

    SlotLeaderRepository slotLeaderRepository = Mockito.mock(SlotLeaderRepository.class);
    PoolHashRepository poolHashRepository = Mockito.mock(PoolHashRepository.class);
    SlotLeaderServiceImpl slotLeaderServiceImpl =
        new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
    AggregatedBatchBlockData aggregatedBatchBlockData = new AggregatedBatchBlockData();
    BlockDataServiceImpl blockDataServiceImpl = new BlockDataServiceImpl(aggregatedBatchBlockData);
    BlockAggregatorServiceImpl victim =
        new BlockAggregatorServiceImpl(slotLeaderServiceImpl, blockDataServiceImpl, protocolMagic);
    AggregatedBlock aggregatedBlock = victim.aggregateBlock(blockEvent);
    Assertions.assertEquals(shelleyEra.getValue(), aggregatedBlock.getEra().getValue());
    Assertions.assertEquals(protocolMagic, aggregatedBlock.getNetwork());
    Assertions.assertEquals(blockHash, aggregatedBlock.getHash());
    Assertions.assertEquals(epochNo, aggregatedBlock.getEpochNo());
    Assertions.assertEquals((int) epochSlotNo, aggregatedBlock.getEpochSlotNo());
    Assertions.assertEquals((int) slotNo, aggregatedBlock.getSlotNo());
    Assertions.assertEquals(blockNo, aggregatedBlock.getBlockNo());
    Assertions.assertEquals(prevBlockHash, aggregatedBlock.getPrevBlockHash());
    Assertions.assertEquals(
        ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX, aggregatedBlock.getSlotLeader().getPrefix());
    Assertions.assertEquals("b3b539e9e7ed1b32fbf778bf2ebf0a6b9f980eac90ac86623d11881a",
        aggregatedBlock.getSlotLeader().getHashRaw());
    Assertions.assertEquals(blockBodySize, aggregatedBlock.getBlockSize());
    Assertions.assertEquals(blockTime, aggregatedBlock.getBlockTime());
    Assertions.assertEquals(1L, aggregatedBlock.getTxCount());
    Assertions.assertEquals(protoMajor, aggregatedBlock.getProtoMajor());
    Assertions.assertEquals(protoMinor, aggregatedBlock.getProtoMinor());
    Assertions.assertEquals(Bech32.encode(
            HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX),
        aggregatedBlock.getVrfKey());
    Assertions.assertEquals(opCert, aggregatedBlock.getOpCert());
    Assertions.assertEquals((long) opCertCounter, aggregatedBlock.getOpCertCounter());

    List<AggregatedTx> aggregatedTxList = aggregatedBlock.getTxList();
    Assertions.assertEquals(1L, aggregatedTxList.size());
    AggregatedTx aggregatedTx = aggregatedTxList.get(0);
    Assertions.assertEquals(witnesses.get(0), aggregatedTx.getWitnesses());
    Assertions.assertEquals(9, aggregatedTx.getCertificates().size());
    Assertions.assertEquals(4, blockDataServiceImpl.getStakeAddressTxHashMap().size());
  }

  @Test
  @DisplayName("Block aggregation should fail on tx array size and witnesses size not equal")
  void aggregateBlockWithTxShouldFailWithDifferentWitnessesSizeTest() {
    EventMetadata metadata = Mockito.mock(EventMetadata.class);
    Block blockCddl = Mockito.mock(Block.class);
    Mockito.when(blockEvent.getBlock()).thenReturn(blockCddl);
    Mockito.when(blockEvent.getMetadata()).thenReturn(metadata);
    Mockito.when(blockCddl.getTransactionBodies()).thenReturn(List.of(new TransactionBody()));

    SlotLeaderService slotLeaderService = Mockito.mock(SlotLeaderService.class);
    BlockDataService blockDataService = Mockito.mock(BlockDataService.class);
    BlockAggregatorServiceImpl victim =
        new BlockAggregatorServiceImpl(slotLeaderService, blockDataService, 1);
    Assertions.assertThrows(IllegalStateException.class, () -> victim.aggregateBlock(blockEvent));
  }

  private static List<TransactionBody> givenTxBodiesBlock48Preprod() {
    String txHash = "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758";
    TransactionInput txIn = buildTxIn(
        "b75ec46c406113372efeb1e57d9880856c240c9b531e3c680c1c4d8bf2253625", 0);
    List<TransactionOutput> txOuts = List.of(
        buildTxOut("addr_test1vz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclgmzkket",
            List.of(buildAmount(Constant.LOVELACE, "", "TE9WRUxBQ0U=",
                BigInteger.valueOf(29699998493561944L))),
            null, null, null),
        buildTxOut(
            "addr_test1qz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclvk35gzr67hz78plv88jemfs2p9e2780xm98cfrf4vvu0rq83pdz2",
            List.of(buildAmount(Constant.LOVELACE, "", "TE9WRUxBQ0U=",
                BigInteger.valueOf(100000000000000L))),
            null, null, null),
        buildTxOut(
            "addr_test1qz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzcl03xqsyk5v0wrqen92yncmn0m0d8k0lcvwt2rkqu3gppw3sdexkvh",
            List.of(buildAmount(Constant.LOVELACE, "", "TE9WRUxBQ0U=",
                BigInteger.valueOf(100000000000000L))),
            null, null, null),
        buildTxOut(
            "addr_test1qz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclfe9t57q689t694cfavck9sh2uw545vp2hz7m7yn03r57kslz5rjf",
            List.of(buildAmount(Constant.LOVELACE, "", "TE9WRUxBQ0U=",
                BigInteger.valueOf(100000000000000L))),
            null, null, null)
    );

    List<Certificate> certificates = List.of(
        buildStakeRegistrationCert(StakeCredType.ADDR_KEYHASH,
            "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6"),
        buildPoolRegistrationCert(
            "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2",
            "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6"),
        buildStakeDelegationCert(StakeCredType.ADDR_KEYHASH,
            "968d1021ebd7178e1fb0e79676982825cabc779b653e1234d58ce3c6"),
        buildStakeRegistrationCert(StakeCredType.ADDR_KEYHASH,
            "f130204b518f70c19995449e3737eded3d9ffc31cb50ec0e45010ba3"),
        buildPoolRegistrationCert(
            "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2",
            "f130204b518f70c19995449e3737eded3d9ffc31cb50ec0e45010ba3"),
        buildStakeDelegationCert(StakeCredType.ADDR_KEYHASH,
            "f130204b518f70c19995449e3737eded3d9ffc31cb50ec0e45010ba3"),
        buildStakeRegistrationCert(StakeCredType.ADDR_KEYHASH,
            "392ae9e068e55e8b5c27acc58b0bab8ea568c0aae2f6fc49be23a7ad"),
        buildPoolRegistrationCert(
            "e0acd71b5584acbd18cb1132f7923656cae9d05cfae08e75bd4d508dc2",
            "392ae9e068e55e8b5c27acc58b0bab8ea568c0aae2f6fc49be23a7ad"),
        buildStakeDelegationCert(StakeCredType.ADDR_KEYHASH,
            "392ae9e068e55e8b5c27acc58b0bab8ea568c0aae2f6fc49be23a7ad")
    );

    TransactionBody txBody = TransactionBody.builder()
        .txHash(txHash)
        .inputs(Set.of(txIn))
        .outputs(txOuts)
        .fee(BigInteger.valueOf(238057L))
        .ttl(90000L)
        .certificates(certificates)
        .validityIntervalStart(0)
        .mint(Collections.emptyList())
        .netowrkId(0)
        .build();

    return List.of(txBody);
  }

  private static List<TransactionBody> givenTxBodiesBlock50Preprod() {
    String txHash = "a00696a0c2d70c381a265a845e43c55e1d00f96b27c06defc015dc92eb206240";
    TransactionInput txIn = buildTxIn(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758", 0);
    Amount amount = buildAmount(
        Constant.LOVELACE, "", "TE9WRUxBQ0U=", BigInteger.valueOf(29699998493355696L));
    TransactionOutput txOut = buildTxOut(
        "addr_test1vz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclgmzkket", List.of(amount), null,
        null, null);
    TransactionBody txBody = TransactionBody.builder()
        .txHash(txHash)
        .inputs(Set.of(txIn))
        .outputs(List.of(txOut))
        .fee(BigInteger.valueOf(206245L))
        .ttl(90000L)
        .certificates(Collections.emptyList())
        .update(givenUpdateTxIdx0Block50())
        .validityIntervalStart(0)
        .mint(Collections.emptyList())
        .netowrkId(0)
        .build();

    return List.of(txBody);
  }

  private static List<TransactionBody> givenTxBodiesBlock949034Preprod() {
    String txHash = "66642962ca48a10a2a7f63d52af4b4dc6ab3a47c7d86d9affe372cd29f02ea01";
    TransactionInput txIn = buildTxIn(
        "6d2174d3956d8eb2b3e1e198e817ccf1332a599d5d7320400bfd820490d706be", 0);
    Amount amount = buildAmount(
        Constant.LOVELACE, "", "TE9WRUxBQ0U=", BigInteger.valueOf(48000000L));
    Amount collateralReturnAmount = buildAmount(
        Constant.LOVELACE, "", "TE9WRUxBQ0U=", BigInteger.valueOf(7000000L));
    TransactionOutput txOut = buildTxOut(
        "addr_test1qqrfa3gfec40c7w7hms4rdakahgssldmqtlxmy54dfucygkcra4ulhfn3g7j9gmnvmefjwzfsd55fq5ndecwlhgcw4zqnztums",
        List.of(amount), null, null, null);
    TransactionInput collateralInput = buildTxIn(
        "e15187e82abcac38688feec70a62919d13aea039ecef1f879aad0cd832572c35", 0);
    TransactionOutput collateralReturn = buildTxOut(
        "addr_test1qqrfa3gfec40c7w7hms4rdakahgssldmqtlxmy54dfucygkcra4ulhfn3g7j9gmnvmefjwzfsd55fq5ndecwlhgcw4zqnztums",
        List.of(collateralReturnAmount), null, null, null);
    TransactionBody txBody = TransactionBody.builder()
        .txHash(txHash)
        .inputs(Set.of(txIn))
        .outputs(List.of(txOut))
        .fee(BigInteger.valueOf(2000000L))
        .certificates(Collections.emptyList())
        .validityIntervalStart(0)
        .mint(Collections.emptyList())
        .scriptDataHash("46990f906d057f5a033a78a258425b1e10c06bc887bf017728ea324ff3c4843c")
        .collateralInputs(Set.of(collateralInput))
        .netowrkId(0)
        .collateralReturn(collateralReturn)
        .totalCollateral(BigInteger.valueOf(3000000L))
        .build();

    return List.of(txBody);
  }

  private static Update givenUpdateTxIdx0Block50() {
    ProtocolParamUpdate protocolParamUpdate = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate2 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate3 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate4 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate5 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate6 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();
    ProtocolParamUpdate protocolParamUpdate7 = ProtocolParamUpdate.builder()
        .protocolMajorVer(3)
        .protocolMinorVer(0)
        .costModels(Collections.emptyMap())
        .build();

    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = buildProtocolParamUpdates(
        Pair.of("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b", protocolParamUpdate),
        Pair.of("f3b9e74f7d0f24d2314ea5dfbca94b65b2059d1ff94d97436b82d5b4", protocolParamUpdate2),
        Pair.of("b260ffdb6eba541fcf18601923457307647dce807851b9d19da133ab", protocolParamUpdate3),
        Pair.of("b00470cd193d67aac47c373602fccd4195aad3002c169b5570de1126", protocolParamUpdate4),
        Pair.of("dd2a7d71a05bed11db61555ba4c658cb1ce06c8024193d064f2a66ae", protocolParamUpdate5),
        Pair.of("ced1599fd821a39593e00592e5292bdc1437ae0f7af388ef5257344a", protocolParamUpdate6),
        Pair.of("8a4b77c4f534f8b8cc6f269e5ebb7ba77fa63a476e50e05e66d7051c", protocolParamUpdate7)
    );

    return new Update(protocolParamUpdateMap, 4);
  }


  private static List<Witnesses> givenTxWitnessesBlock48Preprod() {
    // VkeyWitness is not even used, just fake it
    List<VkeyWitness> vkeyWitnesses = List.of(Mockito.mock(VkeyWitness.class));

    return List.of(Witnesses.builder().vkeyWitnesses(vkeyWitnesses).build());
  }

  private static List<Witnesses> givenTxWitnessesBlock50Preprod() {
    // VkeyWitness is not even used, just fake it
    List<VkeyWitness> vkeyWitnesses = List.of(Mockito.mock(VkeyWitness.class));

    return List.of(Witnesses.builder().vkeyWitnesses(vkeyWitnesses).build());
  }

  @SneakyThrows
  private static List<Witnesses> givenTxWitnessesBlock949034Preprod() {
    // VkeyWitness is not even used, just fake it
    List<VkeyWitness> vkeyWitnesses = List.of(Mockito.mock(VkeyWitness.class));
    List<Datum> datumList = List.of(
        buildDatum("19077a", "{\\\"int\\\":1914}")
    );
    var redeemer = Redeemer.deserializePreConway((Array) CborSerializationUtil.deserialize(HexUtil.decodeHexString("840000d87b9f5820561940091ccf4859b053c522d7b82be8de0d39c0ce9221c4e18289e0192ec95dff821a00109f3e1a14616369")));
    List<Redeemer> redeemers = List.of(
        // fake for test
        redeemer
    );

    Witnesses witnesses = Witnesses.builder()
        .vkeyWitnesses(vkeyWitnesses)
        .datums(datumList)
        .redeemers(redeemers)
        .build();

    return List.of(witnesses);
  }

  private static TransactionInput buildTxIn(String txId, int index) {
    return new TransactionInput(txId, index);
  }

  private static TransactionOutput buildTxOut(String address, List<Amount> amounts,
                                              String datumHash, String inlineDatum,
                                              String scriptRef) {
    return new TransactionOutput(address, amounts, datumHash, inlineDatum, scriptRef);
  }

  private static Amount buildAmount(String unit, String policyId,
                                    String assetNameB64, BigInteger quantity) {
    return new Amount(unit, policyId, assetNameB64, Base64.getDecoder().decode(assetNameB64), quantity);
  }

  private static Datum buildDatum(String cbor, String json) {
    return new Datum(HexUtil.encodeHexString(Blake2bUtil.blake2bHash256(HexUtil.decodeHexString(cbor))), cbor, json);
  }

  @SafeVarargs
  private static Map<String, ProtocolParamUpdate> buildProtocolParamUpdates(
      Pair<String, ProtocolParamUpdate>... protocolParamUpdatePairs) {
    Map<String, ProtocolParamUpdate> protocolParamUpdateMap = new LinkedHashMap<>();

    for (Pair<String, ProtocolParamUpdate> protocolParamUpdatePair : protocolParamUpdatePairs) {
      String key = protocolParamUpdatePair.getFirst();
      ProtocolParamUpdate value = protocolParamUpdatePair.getSecond();
      protocolParamUpdateMap.put(key, value);
    }

    return protocolParamUpdateMap;
  }

  private static ExUnits buildExUnits(BigInteger mem, BigInteger steps) {
    return ExUnits.builder().mem(mem).steps(steps).build();
  }
}
