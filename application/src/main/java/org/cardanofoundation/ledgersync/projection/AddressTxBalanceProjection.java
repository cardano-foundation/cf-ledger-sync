package org.cardanofoundation.ledgersync.projection;

import java.math.BigInteger;

public interface AddressTxBalanceProjection {

    String getAddress();

    BigInteger getBalance();

    String getTxHash();

    Integer getEpochNo();
}
