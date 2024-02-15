package org.cardanofoundation.ledgersync.constant;

import org.cardanofoundation.ledgersync.common.common.constant.Constant;

import java.util.List;

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

    private static final List<Integer> networkNotStartWithByron = List.of(Constant.PREVIEW_TESTNET, Constant.SANCHONET);

    public static List<Integer> getNetworkNotStartWithByron() {
        return networkNotStartWithByron;
    }
}
