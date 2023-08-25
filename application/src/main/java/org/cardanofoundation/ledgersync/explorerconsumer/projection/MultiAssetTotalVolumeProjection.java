package org.cardanofoundation.ledgersync.explorerconsumer.projection;

import java.math.BigInteger;

public interface MultiAssetTotalVolumeProjection {

    Long getIdentId();

    BigInteger getTotalVolume();
}
