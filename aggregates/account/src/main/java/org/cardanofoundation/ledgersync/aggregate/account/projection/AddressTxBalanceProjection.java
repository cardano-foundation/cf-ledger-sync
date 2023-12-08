package org.cardanofoundation.ledgersync.aggregate.account.projection;

import java.math.BigInteger;

public interface AddressTxBalanceProjection {

    String getAddress();

    BigInteger getBalance();

    String getTxHash();

//    Integer getEpochNo();
}
