package org.cardanofoundation.ledgersync.streamer.healthcheck;

import lombok.Getter;

@Getter
public enum Message {
    IS_CRAWLING("IS_CRAWLING", "Data is being crawled"),
    IS_NOT_CRAWLING("IS_NOT_CRAWLING", "Data is not being crawled"),
    STOP_SLOT_HAS_REACHED("STOP_SLOT_HAS_REACHED", "Stop slot has been reached"),
    CONNECTION_HEALTHY_BUT_DATA_CRAWLING_NOT_HEALTHY("CONNECTION_HEALTHY_BUT_DATA_CRAWLING_NOT_HEALTHY",
            "Connection to node is healthy but data crawling is not healthy");

    private final String code;
    private final String desc;

    Message(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
