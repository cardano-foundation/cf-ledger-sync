package org.cardanofoundation.ledgersync.explorerconsumer.service;

public interface GenesisFetching {

    /**
     * get json string from input url
     *
     * @return json String
     */
    String getContent(String url);
}
