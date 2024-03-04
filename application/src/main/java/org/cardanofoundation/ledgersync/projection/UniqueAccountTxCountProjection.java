package org.cardanofoundation.ledgersync.projection;

public interface UniqueAccountTxCountProjection {

    String getAccount();

    Integer getTxCount();
}
