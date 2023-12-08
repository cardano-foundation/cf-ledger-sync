package org.cardanofoundation.ledgersync.aggregate.account.repository;

import java.math.BigInteger;

public interface MultiAssetTotalVolumeProjection {

    Long getIdentId();

    BigInteger getTotalVolume();
}
