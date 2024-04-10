package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.Block;
import com.bloxbean.cardano.yaci.core.model.byron.ByronMainBlock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolHash;
import org.cardanofoundation.ledgersync.consumercommon.entity.SlotLeader;
import org.cardanofoundation.ledgersync.common.util.SlotLeaderUtils;
import org.cardanofoundation.ledgersync.aggregate.AggregatedSlotLeader;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.repository.SlotLeaderRepository;
import org.cardanofoundation.ledgersync.service.SlotLeaderService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotLeaderServiceImpl implements SlotLeaderService {

    public static final int HASH_LENGTH = 16;
    public static final String DELIMITER = "-";
    SlotLeaderRepository slotLeaderRepository;
    PoolHashRepository poolHashRepository;


    @Override
    public AggregatedSlotLeader getSlotLeaderHashAndPrefix(Block blockCddl) {
        String issuerVkey = blockCddl.getHeader().getHeaderBody().getIssuerVkey();
        String hashRaw = SlotLeaderUtils.getAfterByronSlotLeader(issuerVkey);
        return new AggregatedSlotLeader(hashRaw, ConsumerConstant.SHELLEY_SLOT_LEADER_PREFIX);
    }

    @Override
    public AggregatedSlotLeader getSlotLeaderHashAndPrefix(ByronMainBlock blockCddl) {
        String pubKey = blockCddl.getHeader().getConsensusData().getPubKey();
        String hashRaw = SlotLeaderUtils.getByronSlotLeader(pubKey);
        return new AggregatedSlotLeader(hashRaw, ConsumerConstant.BYRON_SLOT_LEADER_PREFIX);
    }

    @Override
    public SlotLeader getSlotLeader(String hashRaw, String prefix) {
        Optional<SlotLeader> slotLeaderOptional = slotLeaderRepository.findSlotLeaderByHash(hashRaw);

        if (slotLeaderOptional.isEmpty()) {
            Optional<PoolHash> poolHashOptional = poolHashRepository.findPoolHashByHashRaw(hashRaw);

            if (poolHashOptional.isEmpty()) {
                SlotLeader slotLeader = buildSlotLeader(hashRaw, prefix, null);
                slotLeaderRepository.save(slotLeader);
                return slotLeader;
            }

            SlotLeader slotLeader = buildSlotLeader(hashRaw,
                    ConsumerConstant.POOL_HASH_PREFIX,
                    poolHashOptional.get());
            slotLeaderRepository.save(slotLeader);
            return slotLeader;
        }

        return slotLeaderOptional.get();
    }

    private static SlotLeader buildSlotLeader(String hashRaw, String prefix, PoolHash poolHash) {
        return SlotLeader.builder()
                .poolHash(poolHash)
                .hash(hashRaw)
                .description(String.join
                        (DELIMITER,
                                prefix,
                                hashRaw.substring(BigInteger.ZERO.intValue(),
                                        HASH_LENGTH)))
                .build();
    }
}
