package org.cardanofoundation.ledgersync.explorerconsumer.projection;

import java.math.BigInteger;

public interface MaTxMintProjection {
    Long getIdentId();

    BigInteger getQuantity();
}
