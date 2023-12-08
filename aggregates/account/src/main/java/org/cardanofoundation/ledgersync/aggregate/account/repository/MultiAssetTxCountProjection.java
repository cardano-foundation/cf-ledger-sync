package org.cardanofoundation.ledgersync.aggregate.account.repository;

public interface MultiAssetTxCountProjection {

    Long getIdentId();

    Long getTxCount();
}
