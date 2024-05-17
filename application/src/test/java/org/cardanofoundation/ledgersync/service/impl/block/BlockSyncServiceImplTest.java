package org.cardanofoundation.ledgersync.service.impl.block;

import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedSlotLeader;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.repository.BlockRepositoryLS;
import org.cardanofoundation.ledgersync.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class BlockSyncServiceImplTest {

  @Mock
  BlockRepositoryLS blockRepositoryLS;

  @Mock
  TransactionService transactionService;

  @Mock
  BlockDataService blockDataService;

  @Mock
  SlotLeaderService slotLeaderService;

  @Mock
  EpochService epochService;

  @Mock
  EpochParamService epochParamService;

  @Mock
  MetricCollectorService metricCollectorService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Test
  @DisplayName("Should skip block syncing on empty block batch")
  void shouldSkipBlockSyncWithNoBlocksTest() {
    Mockito.when(blockDataService.getBlockSize()).thenReturn(0);

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
            blockRepositoryLS, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();
    Mockito.verifyNoInteractions(blockRepositoryLS);

    Mockito.verifyNoInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoInteractions(slotLeaderService);
    Mockito.verifyNoInteractions(epochService);
    Mockito.verifyNoInteractions(epochParamService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Block sync should fail if previous block not found")
  void shouldFailOnNoPreviousBlockTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);

    // Prev hash from block 46 preprod
    Mockito.when(aggregatedBlock.getPrevBlockHash())
        .thenReturn("45899e8002b27df291e09188bfe3aeb5397ac03546a7d0ead93aa2500860f1af");
    Mockito.when(aggregatedBlock.getBlockNo()).thenReturn(47L);
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
            blockRepositoryLS, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService,
        metricCollectorService, aggregatedDataCachingService
    );
    Assertions.assertThrows(IllegalStateException.class, victim::startBlockSyncing);

    Mockito.verify(blockRepositoryLS, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verifyNoInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verify(blockDataService, Mockito.times(1)).getFirstAndLastBlock();
    Mockito.verify(blockDataService, Mockito.times(1)).getAllAggregatedBlocks();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoInteractions(slotLeaderService);
    Mockito.verifyNoInteractions(epochService);
    Mockito.verifyNoInteractions(epochParamService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Should do block sync with aggregated block having null slot leader successfully")
  void shouldSyncBlockWithNullSlotLeaderSuccessfullyTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);

    // Prev hash from block 0 preprod
    Mockito.when(aggregatedBlock.getPrevBlockHash())
        .thenReturn("d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937");
    Mockito.when(aggregatedBlock.getBlockNo()).thenReturn(1L);
    Mockito.when(aggregatedBlock.getBlockTime()).thenReturn(Timestamp.valueOf(LocalDateTime.of(2019, 7, 25, 2, 4, 16)));
    Mockito.when(aggregatedBlock.getSlotNo()).thenReturn(10000L);
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));
    Mockito.when(blockRepositoryLS.findBlockByHash(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(Block.class)));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
            blockRepositoryLS, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();

    Mockito.verify(blockRepositoryLS, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verify(blockRepositoryLS, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepositoryLS);
    Mockito.verify(transactionService, Mockito.times(1))
        .prepareAndHandleTxs(Mockito.anyMap(), Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verify(blockDataService, Mockito.times(1)).getFirstAndLastBlock();
    Mockito.verify(blockDataService, Mockito.times(1)).getAllAggregatedBlocks();
    Mockito.verify(blockDataService, Mockito.times(1)).clearBatchBlockData();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoMoreInteractions(slotLeaderService);
    Mockito.verify(epochService, Mockito.times(1)).handleEpoch(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(epochService);
    Mockito.verify(epochParamService, Mockito.times(1)).handleEpochParams();
    Mockito.verifyNoMoreInteractions(epochParamService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addBlockCount(anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Should do block sync successfully")
  void shouldSyncSuccessfullyTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    AggregatedSlotLeader slotLeader = Mockito.mock(AggregatedSlotLeader.class);

    // Prev hash, slot leader from block 46 preprod
    Mockito.when(aggregatedBlock.getPrevBlockHash())
        .thenReturn("45899e8002b27df291e09188bfe3aeb5397ac03546a7d0ead93aa2500860f1af");
    Mockito.when(aggregatedBlock.getBlockNo()).thenReturn(47l);
    Mockito.when(aggregatedBlock.getSlotLeader()).thenReturn(slotLeader);
    Mockito.when(aggregatedBlock.getBlockTime()).thenReturn(Timestamp.valueOf(LocalDateTime.of(2019, 7, 25, 2, 4, 16)));
    Mockito.when(aggregatedBlock.getSlotNo()).thenReturn(10000L);
    Mockito.when(slotLeader.getHashRaw())
        .thenReturn("aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d");
    Mockito.when(slotLeader.getPrefix()).thenReturn(ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX);
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));
    Mockito.when(blockRepositoryLS.findBlockByHash(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(Block.class)));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
            blockRepositoryLS, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();

    Mockito.verify(blockRepositoryLS, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verify(blockRepositoryLS, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepositoryLS);
    Mockito.verify(transactionService, Mockito.times(1))
        .prepareAndHandleTxs(Mockito.anyMap(), Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verify(blockDataService, Mockito.times(1)).getFirstAndLastBlock();
    Mockito.verify(blockDataService, Mockito.times(1)).getAllAggregatedBlocks();
    Mockito.verify(blockDataService, Mockito.times(1)).clearBatchBlockData();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(slotLeaderService, Mockito.times(1))
        .getSlotLeader(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(slotLeaderService);
    Mockito.verify(epochService, Mockito.times(1)).handleEpoch(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(epochService);
    Mockito.verify(epochParamService, Mockito.times(1)).handleEpochParams();
    Mockito.verifyNoMoreInteractions(epochParamService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addBlockCount(anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }
}
