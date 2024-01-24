package org.cardanofoundation.ledgersync.projection;

import java.math.BigInteger;

public interface MultiAssetTotalVolumeProjection {

    Long getIdentId();

    BigInteger getTotalVolume();
}
