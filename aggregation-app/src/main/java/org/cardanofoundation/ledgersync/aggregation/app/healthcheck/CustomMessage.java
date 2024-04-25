package org.cardanofoundation.ledgersync.aggregation.app.healthcheck;

import lombok.Getter;

@Getter
public enum CustomMessage {
    IS_SCHEDULED_TO_STOP("IS_SCHEDULED_TO_STOP", "Service is scheduled to stop");

    private final String code;
    private final String desc;

    CustomMessage(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
