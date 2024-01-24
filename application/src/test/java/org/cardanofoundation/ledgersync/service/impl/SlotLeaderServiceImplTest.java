package org.cardanofoundation.ledgersync.service.impl;

import java.math.BigInteger;
import java.util.Optional;

import com.bloxbean.cardano.yaci.core.model.Block;
import com.bloxbean.cardano.yaci.core.model.BlockHeader;
import com.bloxbean.cardano.yaci.core.model.HeaderBody;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockCons;
import com.bloxbean.cardano.yaci.core.model.byron.ByronBlockHead;
import com.bloxbean.cardano.yaci.core.model.byron.ByronMainBlock;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
import org.cardanofoundation.ledgersync.consumercommon.entity.SlotLeader;
import org.cardanofoundation.ledgersync.aggregate.AggregatedSlotLeader;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.repository.SlotLeaderRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class SlotLeaderServiceImplTest {

  private static final int HASH_LENGTH = 16;
  private static final String DELIMITER = "-";

  @Mock
  SlotLeaderRepository slotLeaderRepository;

  @Mock
  PoolHashRepository poolHashRepository;

  SlotLeaderServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new SlotLeaderServiceImpl(slotLeaderRepository, poolHashRepository);
  }

  @Test
  @DisplayName("Should get Byron slot leader hash and prefix successfully")
  void shouldGetByronSlotLeaderHashAndPrefixSuccessfullyTest() {
    String pubKey = "3fb2637923c78dec4d5b932c09ac92ad445aa95da00a05edb447b40a07c59d5645187692f40e31114ad0f375532cf8e4f875740e2d7d0e343e0e14b533a31fec";
    ByronMainBlock block = Mockito.mock(ByronMainBlock.class);
    ByronBlockHead byronBlockHead = Mockito.mock(ByronBlockHead.class);
    ByronBlockCons byronBlockCons = Mockito.mock(ByronBlockCons.class);

    Mockito.when(block.getHeader()).thenReturn(byronBlockHead);
    Mockito.when(byronBlockHead.getConsensusData()).thenReturn(byronBlockCons);
    Mockito.when(byronBlockCons.getPubKey()).thenReturn(pubKey);

    AggregatedSlotLeader slotLeader = victim.getSlotLeaderHashAndPrefix(block);
    Assertions.assertNotNull(slotLeader);
    Assertions.assertEquals(
        ConsumerConstant.BYRON_SLOT_LEADER_PREFIX,
        slotLeader.getPrefix());
    Assertions.assertEquals(
        "7adf1c1d73d04427ec798ab78c9416bcdf6f73fafd453cfe860ab035",
        slotLeader.getHashRaw());
  }

  @Test
  @DisplayName("Should get Shelley slot leader hash and prefix successfully")
  void shouldGetShelleySlotLeaderHashAndPrefixSuccessfullyTest() {
    String issuerVrfKey = "d1a8de6caa8fd9b175c59862ecdd5abcd0477b84b82a0e52faecc6b3c85100a4";
    Block block = Mockito.mock(Block.class);
    BlockHeader blockHeader = Mockito.mock(BlockHeader.class);
    HeaderBody headerBody = Mockito.mock(HeaderBody.class);

    Mockito.when(block.getHeader()).thenReturn(blockHeader);
    Mockito.when(blockHeader.getHeaderBody()).thenReturn(headerBody);
    Mockito.when(headerBody.getIssuerVkey()).thenReturn(issuerVrfKey);

    AggregatedSlotLeader slotLeader = victim.getSlotLeaderHashAndPrefix(block);
    Assertions.assertNotNull(slotLeader);
    Assertions.assertEquals(
        ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX,
        slotLeader.getPrefix());
    Assertions.assertEquals(
        "aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d",
        slotLeader.getHashRaw());
  }

  @Test
  @DisplayName("Should create new slot leader successfully")
  void shouldCreateNewSlotLeaderSuccessfullyTest() {
    // Byron slot leader
    String slotLeaderHash = "7adf1c1d73d04427ec798ab78c9416bcdf6f73fafd453cfe860ab035";
    String slotLeaderPrefix = ConsumerConstant.BYRON_SLOT_LEADER_PREFIX;

    Mockito.when(slotLeaderRepository.findSlotLeaderByHash(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Mockito.when(poolHashRepository.findPoolHashByHashRaw(slotLeaderHash))
        .thenReturn(Optional.empty());

    SlotLeader slotLeader = victim.getSlotLeader(slotLeaderHash, slotLeaderPrefix);
    Assertions.assertNotNull(slotLeader);
    Mockito.verify(slotLeaderRepository, Mockito.times(1))
        .findSlotLeaderByHash(Mockito.anyString());
    Mockito.verify(slotLeaderRepository, Mockito.times(1))
        .save(Mockito.any());
    Mockito.verifyNoMoreInteractions(slotLeaderRepository);

    Assertions.assertNull(slotLeader.getPoolHash());
    Assertions.assertEquals(slotLeaderHash, slotLeader.getHash());
    String[] slotLeaderDesc = slotLeader.getDescription().split(DELIMITER);
    Assertions.assertEquals(slotLeaderPrefix, slotLeaderDesc[0]);
    Assertions.assertEquals(
        slotLeaderHash.substring(BigInteger.ZERO.intValue(), HASH_LENGTH),
        slotLeaderDesc[1]);
  }

  @Test
  @DisplayName("Should create new pool slot leader successfully")
  void shouldCreateNewPoolSlotLeaderSuccessfullyTest() {
    // Shelley slot leader
    String slotLeaderHash = "aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d";

    Mockito.when(slotLeaderRepository.findSlotLeaderByHash(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Mockito.when(poolHashRepository.findPoolHashByHashRaw(slotLeaderHash))
        .thenReturn(Optional.of(Mockito.mock(PoolHash.class)));

    SlotLeader slotLeader = victim.getSlotLeader(slotLeaderHash, null);
    Assertions.assertNotNull(slotLeader);
    Mockito.verify(slotLeaderRepository, Mockito.times(1))
        .findSlotLeaderByHash(Mockito.anyString());
    Mockito.verify(slotLeaderRepository, Mockito.times(1))
        .save(Mockito.any());
    Mockito.verifyNoMoreInteractions(slotLeaderRepository);

    Assertions.assertNotNull(slotLeader.getPoolHash());
    Assertions.assertEquals(slotLeaderHash, slotLeader.getHash());
    String[] slotLeaderDesc = slotLeader.getDescription().split(DELIMITER);
    Assertions.assertEquals(ConsumerConstant.POOL_HASH_PREFIX, slotLeaderDesc[0]);
    Assertions.assertEquals(
        slotLeaderHash.substring(BigInteger.ZERO.intValue(), HASH_LENGTH),
        slotLeaderDesc[1]);
  }

  @Test
  @DisplayName("Should get existing slot leader successfully")
  void shouldGetExistingSlotLeaderSuccessfullyTest() {
    // Shelley slot leader
    String slotLeaderHash = "aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d";

    Mockito.when(slotLeaderRepository.findSlotLeaderByHash(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(SlotLeader.class)));

    SlotLeader slotLeader = victim.getSlotLeader(slotLeaderHash, null);
    Assertions.assertNotNull(slotLeader);

    Mockito.verify(slotLeaderRepository, Mockito.times(1))
        .findSlotLeaderByHash(Mockito.anyString());
    Mockito.verifyNoMoreInteractions(slotLeaderRepository);
    Mockito.verifyNoInteractions(poolHashRepository);
  }
}
