package org.cardanofoundation.ledgersync.explorerconsumer.projection;

public interface UniqueAccountTxCountProjection {

    String getAccount();

    Integer getTxCount();
}
