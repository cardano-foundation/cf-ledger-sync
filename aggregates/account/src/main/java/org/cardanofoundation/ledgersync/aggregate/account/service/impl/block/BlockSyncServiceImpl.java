package org.cardanofoundation.ledgersync.aggregate.account.service.impl.block;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockDataService;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockSyncService;
import org.cardanofoundation.ledgersync.aggregate.account.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockSyncServiceImpl implements BlockSyncService {

    TransactionService transactionService;
    BlockDataService blockDataService;

    @Override
    @Transactional
    public void startBlockSyncing() {
        if (blockDataService.getBlockSize() == 0) {
            return;
        }
        this.handleBlockSync();
    }

    public void handleBlockSync() {
        var firstAndLastBlock = blockDataService.getFirstAndLastBlock();

        AggregatedBlock firstBlock = firstAndLastBlock.getFirst();
        AggregatedBlock lastBlock = firstAndLastBlock.getSecond();

        log.trace("Commit from block {} to block {} ",
                firstBlock.getBlockNo(),
                lastBlock.getBlockNo());

        // Initialize block entities
        Collection<AggregatedBlock> allAggregatedBlocks = blockDataService.getAllAggregatedBlocks();
        transactionService.prepareAndHandleTxs(allAggregatedBlocks);
        blockDataService.clearBatchBlockData();
    }

}
