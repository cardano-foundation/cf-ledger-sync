package org.cardanofoundation.ledgersync.projection;

import java.math.BigInteger;

public interface MaTxMintProjection {
    Long getIdentId();

    BigInteger getQuantity();
}
