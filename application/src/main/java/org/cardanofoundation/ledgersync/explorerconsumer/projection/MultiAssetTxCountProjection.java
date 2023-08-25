package org.cardanofoundation.ledgersync.explorerconsumer.projection;

public interface MultiAssetTxCountProjection {

    Long getIdentId();

    Long getTxCount();
}
