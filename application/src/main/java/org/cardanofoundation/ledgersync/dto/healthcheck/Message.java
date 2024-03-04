package org.cardanofoundation.ledgersync.dto.healthcheck;

import lombok.Getter;

@Getter
public enum Message {
    READY_TO_SERVE("READY_TO_SERVE", "Data is ready to serve"),
    IS_NOT_SYNCING("IS_NOT_SYNCING", "Connection to node is not healthy, data is not being synchronized"),
    SYNCING_BUT_NOT_READY("SYNCING_BUT_NOT_READY", "Data is being synchronized, but it isn't ready to serve yet"),
    CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY("CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY",
            "Connection to node is healthy, but the latest block insertion time has exceeded the threshold"),
    SYNCING_HAS_FINISHED("SYNCING_HAS_FINISHED",
            "Connection to node is healthy, but the latest block insertion time has exceeded the threshold");

    private final String code;
    private final String desc;

    Message(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
