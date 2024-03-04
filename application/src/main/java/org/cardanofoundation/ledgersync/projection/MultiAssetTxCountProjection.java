package org.cardanofoundation.ledgersync.projection;

public interface MultiAssetTxCountProjection {

    Long getIdentId();

    Long getTxCount();
}
