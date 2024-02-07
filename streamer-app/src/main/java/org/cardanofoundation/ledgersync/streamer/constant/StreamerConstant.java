package org.cardanofoundation.ledgersync.streamer.constant;

public class StreamerConstant {
    private StreamerConstant() {
    }

    public static final String DATA_IS_CRAWLING = "Data is being crawled";
    public static final String DATA_IS_NOT_CRAWLING = "Data is not being crawled";
    public static final String STOP_SLOT_HAS_REACHED = "Stop slot has been reached";
    public static final String CONNECTION_HEALTHY_BUT_DATA_CRAWLING_NOT_HEALTHY = "Connection to node is healthy but data crawling is not healthy";
}
