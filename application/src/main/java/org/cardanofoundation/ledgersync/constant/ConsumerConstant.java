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
    public static final String DATA_IS_NOT_SYNCING = "Data is not being synchronized and connection to node is not healthy";
    public static final String SYNCING_HAS_FINISHED = "The data synchronization to a specific slot has finished";
    public static final String READY_TO_SERVE = "Data is ready to serve";
    public static final String SYNCING_BUT_NOT_READY = "Data is being synchronized, but it isn't ready to serve yet";
    public static final String CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY = "Connection to node is healthy, but the latest block insertion time has exceeded the threshold";

    private static final List<Integer> networkNotStartWithByron = List.of(Constant.PREVIEW_TESTNET, Constant.SANCHONET);

    public static List<Integer> getNetworkNotStartWithByron() {
        return networkNotStartWithByron;
    }
}
