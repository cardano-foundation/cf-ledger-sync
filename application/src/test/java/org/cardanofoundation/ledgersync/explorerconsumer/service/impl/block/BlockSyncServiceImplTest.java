package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block;

import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Pair;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedSlotLeader;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.EpochParamService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.EpochService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MetricCollectorService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SlotLeaderService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TransactionService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TxChartService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.BlockSyncServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BlockSyncServiceImplTest {

  @Mock
  BlockRepository blockRepository;

  @Mock
  TxRepository txRepository;

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
  TxChartService txChartService;

  @Mock
  MetricCollectorService metricCollectorService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Test
  @DisplayName("Should skip block syncing on empty block batch")
  void shouldSkipBlockSyncWithNoBlocksTest() {
    Mockito.when(blockDataService.getBlockSize()).thenReturn(0);

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
        blockRepository, txRepository, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService, txChartService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();
    Mockito.verifyNoInteractions(blockRepository);
    Mockito.verifyNoInteractions(txRepository);
    Mockito.verifyNoInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoInteractions(slotLeaderService);
    Mockito.verifyNoInteractions(epochService);
    Mockito.verifyNoInteractions(epochParamService);
    Mockito.verifyNoInteractions(txChartService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Block sync should fail if previous block not found")
  void shouldFailOnNoPreviousBlockTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);

    // Prev hash from block 46 preprod
    Mockito.when(aggregatedBlock.getPrevBlockHash())
        .thenReturn("45899e8002b27df291e09188bfe3aeb5397ac03546a7d0ead93aa2500860f1af");
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
        blockRepository, txRepository, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService, txChartService,
        metricCollectorService, aggregatedDataCachingService
    );
    Assertions.assertThrows(IllegalStateException.class, victim::startBlockSyncing);

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verifyNoInteractions(txRepository);
    Mockito.verifyNoInteractions(transactionService);
    Mockito.verify(blockDataService, Mockito.times(1)).getBlockSize();
    Mockito.verify(blockDataService, Mockito.times(1)).getFirstAndLastBlock();
    Mockito.verify(blockDataService, Mockito.times(1)).getAllAggregatedBlocks();
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verifyNoInteractions(slotLeaderService);
    Mockito.verifyNoInteractions(epochService);
    Mockito.verifyNoInteractions(epochParamService);
    Mockito.verifyNoInteractions(txChartService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Should do block sync with aggregated block having null slot leader successfully")
  void shouldSyncBlockWithNullSlotLeaderSuccessfullyTest() {
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);

    // Prev hash from block 0 preprod
    Mockito.when(aggregatedBlock.getPrevBlockHash())
        .thenReturn("d4b8de7a11d929a323373cbab6c1a9bdc931beffff11db111cf9d57356ee1937");
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));
    Mockito.when(blockRepository.findBlockByHash(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(Block.class)));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
        blockRepository, txRepository, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService, txChartService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verify(blockRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verify(txRepository, Mockito.times(1)).findFirstByOrderByIdDesc();
    Mockito.verifyNoMoreInteractions(txRepository);
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
    Mockito.verify(epochParamService, Mockito.times(1)).handleEpochParams(anyInt());
    Mockito.verifyNoMoreInteractions(epochParamService);
    Mockito.verify(txChartService, Mockito.times(1)).handleTxChart(Mockito.any());
    Mockito.verifyNoMoreInteractions(txChartService);
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
    Mockito.when(aggregatedBlock.getSlotLeader()).thenReturn(slotLeader);
    Mockito.when(slotLeader.getHashRaw())
        .thenReturn("aae9293510344ddd636364c2673e34e03e79e3eefa8dbaa70e326f7d");
    Mockito.when(slotLeader.getPrefix()).thenReturn(ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX);
    Mockito.when(blockDataService.getBlockSize()).thenReturn(1);
    Mockito.when(blockDataService.getFirstAndLastBlock())
        .thenReturn(Pair.of(aggregatedBlock, aggregatedBlock));
    Mockito.when(blockDataService.getAllAggregatedBlocks()).thenReturn(List.of(aggregatedBlock));
    Mockito.when(blockRepository.findBlockByHash(Mockito.anyString()))
        .thenReturn(Optional.of(Mockito.mock(Block.class)));

    BlockSyncServiceImpl victim = new BlockSyncServiceImpl(
        blockRepository, txRepository, transactionService, blockDataService,
        slotLeaderService, epochService, epochParamService, txChartService,
        metricCollectorService, aggregatedDataCachingService
    );
    victim.startBlockSyncing();

    Mockito.verify(blockRepository, Mockito.times(1)).findBlockByHash(Mockito.anyString());
    Mockito.verify(blockRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(blockRepository);
    Mockito.verify(txRepository, Mockito.times(1)).findFirstByOrderByIdDesc();
    Mockito.verifyNoMoreInteractions(txRepository);
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
    Mockito.verify(epochParamService, Mockito.times(1)).handleEpochParams(anyInt());
    Mockito.verifyNoMoreInteractions(epochParamService);
    Mockito.verify(txChartService, Mockito.times(1)).handleTxChart(Mockito.any());
    Mockito.verifyNoMoreInteractions(txChartService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .addBlockCount(anyInt());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1))
        .saveLatestTxs();
    Mockito.verify(aggregatedDataCachingService, Mockito.times(1)).commit();
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);
  }
}
