package org.cardanofoundation.ledgersync.projection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxOutProjection {
    Long id;
    String txHash;
    Long txId;
    Short index;
    BigInteger value;
    Long stakeAddressId;
    String address;
    Boolean addressHasScript;
    String paymentCred;
}
