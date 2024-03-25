package org.cardanofoundation.ledgersync.healthcheck.model;

import lombok.Getter;

@Getter
public enum Message {
    IS_GOOD("IS_GOOD", "The last time received a block event is within the threshold"),
    IS_BAD("IS_BAD", "The last time received a block event is beyond the threshold"),
    STOP_SLOT_HAS_REACHED("STOP_SLOT_HAS_REACHED", "Stop slot has been reached"),
    BLOCK_HAS_REACHED_TIP("BLOCK_HAS_REACHED_TIP", "The latest block is the tip or near the tip");

    private final String code;
    private final String desc;

    Message(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
