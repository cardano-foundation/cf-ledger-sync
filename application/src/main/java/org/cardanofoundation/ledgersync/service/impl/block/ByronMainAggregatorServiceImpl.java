package org.cardanofoundation.ledgersync.service.impl.block;

import com.bloxbean.cardano.yaci.core.model.byron.ByronMainBlock;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTx;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxIn;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxOut;
import com.bloxbean.cardano.yaci.core.model.byron.payload.ByronTxPayload;
import com.bloxbean.cardano.yaci.store.events.ByronMainBlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.service.BlockAggregatorService;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.cardanofoundation.ledgersync.service.SlotLeaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ByronMainAggregatorServiceImpl extends BlockAggregatorService<ByronMainBlockEvent> {
    private int protocolMagic;

    public ByronMainAggregatorServiceImpl(
            SlotLeaderService slotLeaderService,
            BlockDataService blockDataService,
            @Value("${store.cardano.protocol-magic}") int protocolMagic
    ) {
        super(slotLeaderService, blockDataService);
        this.protocolMagic = protocolMagic;
    }

    @Override
    public AggregatedBlock aggregateBlock(ByronMainBlockEvent blockEvent) {
        return mapBlockCddlToAggregatedBlock(blockEvent.getMetadata(), blockEvent.getByronMainBlock());
    }

    private AggregatedBlock mapBlockCddlToAggregatedBlock(EventMetadata metadata, ByronMainBlock blockCddl) {
        var blockHash = metadata.getBlockHash();
        var slotId = blockCddl.getHeader().getConsensusData().getSlotId();
        var epochNo = (int) metadata.getEpochNumber();
        var slotNo = metadata.getSlot();
        var epochSlotNo = metadata.getEpochSlot();
        var blockNo = metadata.getBlock();
        var prevHash = blockCddl.getHeader().getPrevBlock();
        var slotLeader = slotLeaderService.getSlotLeaderHashAndPrefix(blockCddl);
        //var blockSize = blockCddl.getCborSize(); //TODO Get block size
        var blockSize = 0;
        var blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
                metadata.getBlockTime(), 0, ZoneOffset.ofHours(0)));
        var txCount = (long) blockCddl.getBody().getTxPayload().size();

        var blockVersion = blockCddl.getHeader().getExtraData().getBlockVersion();
        var protoMajor = (int) blockVersion.getMajor();
        var protoMinor = (int) blockVersion.getMinor();

        List<AggregatedTx> txList = mapCddlTxToAggregatedTx(metadata, blockCddl);
        return AggregatedBlock.builder()
                .era(Era.valueOf(metadata.getEra().name().toUpperCase()))
                .network(protocolMagic)
                .hash(blockHash)
                .epochNo(epochNo)
                .epochSlotNo((int) epochSlotNo)
                .slotNo(slotNo)
                .blockNo(blockNo)
                .prevBlockHash(prevHash)
                .slotLeader(slotLeader)
                .blockSize(blockSize)
                .blockTime(blockTime)
                .txCount(txCount)
                .protoMajor(protoMajor)
                .protoMinor(protoMinor)
                .txList(txList)
                .auxiliaryDataMap(Collections.emptyMap())
                .isGenesis(Boolean.FALSE)
                .build();
    }

    /**
     * This method transforms CDDL tx data to aggregated tx objects, which
     * will be used later by block processing and transactions handling
     * services
     *
     * @param blockCddl transformed block data from CDDL, containing tx data
     * @return list of aggregated tx objects
     */
    private List<AggregatedTx> mapCddlTxToAggregatedTx(EventMetadata metadata, ByronMainBlock blockCddl) {
        List<ByronTxPayload> txPayloads = blockCddl.getBody().getTxPayload();
        return IntStream.range(0, txPayloads.size()).mapToObj(txIdx -> {
            ByronTxPayload byronTxPayload = txPayloads.get(txIdx);
            return txToAggregatedTx(metadata.getBlockHash(), txIdx, byronTxPayload);
        }).collect(Collectors.toList());
    }

    /**
     * This method transforms a single CDDL tx data to aggregated tx object
     *
     * @param blockHash block hash where the currently processing tx lies in
     * @param idx       currently processing tx's index within a block
     * @param txPayload transformed CDDL tx data
     * @return aggregated tx object
     */
    private AggregatedTx txToAggregatedTx(String blockHash, int idx, ByronTxPayload txPayload) {
        ByronTx byronTx = txPayload.getTransaction();
        // Handle basic tx data
        var txHash = byronTx.getTxHash();

        // Converts CDDL tx ins data to aggregated tx ins
        var inputs = byronTx.getInputs();

        // Converts CDDL tx outs/collateral return data to aggregated tx outs
        var outputs = byronTx.getOutputs();
        var aggregatedTxOuts = txOutsToAggregatedTxOuts(outputs);

        var outSum = calculateByronOutSum(outputs);

        AggregatedTx aggregatedTx = AggregatedTx.builder()
                .hash(txHash)
                .blockHash(blockHash)
                .blockIndex(idx)
                .validContract(true)
                .deposit(0)
                .txInputs(txInsToAggregatedTxIns(inputs))
                .txOutputs(aggregatedTxOuts)
                .outSum(outSum)
                .fee(BigInteger.ZERO)
                .byronTxWitnesses(txPayload.getWitnesses())
                .build();

        return aggregatedTx;
    }

    private Set<AggregatedTxIn> txInsToAggregatedTxIns(List<ByronTxIn> txInputs) {
        if (CollectionUtils.isEmpty(txInputs)) {
            return Collections.emptySet();
        }

        return txInputs.stream().map(AggregatedTxIn::of).collect(Collectors.toSet());
    }

    private List<AggregatedTxOut> txOutsToAggregatedTxOuts(List<ByronTxOut> txOutputs) {
        if (CollectionUtils.isEmpty(txOutputs)) {
            return Collections.emptyList();
        }

        return IntStream.range(0, txOutputs.size())
                .mapToObj(txOutIdx -> AggregatedTxOut.from(txOutputs.get(txOutIdx), txOutIdx))
                .collect(Collectors.toList());
    }

    private static BigInteger calculateByronOutSum(List<ByronTxOut> txOuts) {
        return txOuts.stream()
                .map(ByronTxOut::getAmount)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
