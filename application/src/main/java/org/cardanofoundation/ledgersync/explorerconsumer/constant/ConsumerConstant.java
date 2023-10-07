package org.cardanofoundation.ledgersync.explorerconsumer.constant;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;

import java.util.List;
import java.util.Map;

public final class ConsumerConstant {

    private ConsumerConstant() {

    }

    public static final int TX_OUT_BATCH_QUERY_SIZE = 200;// Tx out will need to join with tx. The result of the sub table may be big. So we should set this value = 200
    public static final int ADDRESS_TOKEN_BALANCE_BATCH_QUERY_SIZE = 200;
    public static final int BATCH_QUERY_SIZE = 3000;
    public static final String POOL_HASH_PREFIX = "pool";
    public static final String VRF_KEY_PREFIX = "vrf_vk";
    public static final int BYRON_SLOT = 21600;
    public static final int FIVE_DAYS = 432000;
    public static final int ONE_DAYS = 86400;
    public static final String SHELLEY_SLOT_LEADER_PREFIX = "ShelleyGenesis";
    public static final String BYRON_SLOT_LEADER_PREFIX = "ByronGenesis";
    public static final String ADDR_PREFIX = "addr";
    public static final String BYTE_NULL = "\u0000";
    public static final String UNDERSCORE = "_";

    public static final Long MAINNET_BYRON_KNOWN_TIME = 1506203091L;
    public static final Long TESTNET_BYRON_KNOWN_TIME = 1564020236L;
    public static final Long PREPROD_BYRON_KNOWN_TIME = 1654041640L;
    public static final Long PREVIEW_SHELLEY_KNOWN_TIME = 1666656000L;

    private static final Map<Integer, Integer> shelleyEpochLength = Map.ofEntries(
            Map.entry(Constant.MAINNET, FIVE_DAYS),
            Map.entry(Constant.TESTNET, FIVE_DAYS),
            Map.entry(Constant.PREPROD_TESTNET, FIVE_DAYS),
            Map.entry(Constant.PREVIEW_TESTNET, ONE_DAYS),
            Map.entry(Constant.SANCHONET, ONE_DAYS)
    );

    public static Integer getShelleyEpochLength(int networkMagic) {
        return shelleyEpochLength.get(networkMagic);
    }

    private static final List<Integer> networkNotStartWithByron = List.of(Constant.PREVIEW_TESTNET, Constant.SANCHONET);

    private static final Map<Integer, Long> mByronKnownTime = Map.ofEntries(
            Map.entry(Constant.MAINNET, MAINNET_BYRON_KNOWN_TIME),
            Map.entry(Constant.TESTNET, TESTNET_BYRON_KNOWN_TIME),
            Map.entry(Constant.PREPROD_TESTNET, PREPROD_BYRON_KNOWN_TIME)
    );

    public static Long getByronKnownTime(int networkMagic) {
        return mByronKnownTime.get(networkMagic);
    }

    public static List<Integer> getNetworkNotStartWithByron() {
        return networkNotStartWithByron;
    }
}
