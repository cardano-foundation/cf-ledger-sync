package org.cardanofoundation.ledgersync.aggregate.account.service.impl.block;

import com.bloxbean.cardano.yaci.core.model.byron.ByronEbBlock;
import com.bloxbean.cardano.yaci.store.events.ByronEbBlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockAggregatorService;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockDataService;
import org.cardanofoundation.ledgersync.common.common.Era;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ByronEbbAggregatorServiceImpl extends BlockAggregatorService<ByronEbBlockEvent> {
    private int protocolMagic;

    public ByronEbbAggregatorServiceImpl(
            BlockDataService blockDataService,
            @Value("${store.cardano.protocol-magic}") int protocolMagic
    ) {
        super(blockDataService);
        this.protocolMagic = protocolMagic;
    }

    @Override
    public AggregatedBlock aggregateBlock(ByronEbBlockEvent byronEbBlockEvent) {
        EventMetadata metadata = byronEbBlockEvent.getMetadata();
        ByronEbBlock blockCddl = byronEbBlockEvent.getByronEbBlock();
        var blockHash = metadata.getBlockHash();
        var consensusData = blockCddl.getHeader().getConsensusData();
        int epochNo = (int) metadata.getEpochNumber();
        // int blockSize = blockCddl.getCborSize(); //TODO size of block
        int blockSize = 0;
        var blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
                metadata.getBlockTime(), 0, ZoneOffset.ofHours(0)));
        var prevBlockHash = blockCddl.getHeader().getPrevBlock();

        return AggregatedBlock.builder()
                .era(Era.valueOf(metadata.getEra().name().toUpperCase()))
                .network(protocolMagic)
                .hash(blockHash)
                .epochNo(epochNo)
                .prevBlockHash(prevBlockHash)
                .blockSize(blockSize)
                .blockTime(blockTime)
                .protoMajor(0)
                .protoMinor(0)
                .txCount(0L)
                .txList(Collections.emptyList())
                .auxiliaryDataMap(Collections.emptyMap())
                .isGenesis(Boolean.FALSE)
                .build();
    }
}
