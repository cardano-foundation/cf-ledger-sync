package org.cardanofoundation.ledgersync.verifier.data.app.projection;

import java.math.BigInteger;

public interface AddressBalanceProjection {
    String getAddress();

    String getUnit();

    BigInteger getBalance();
}
