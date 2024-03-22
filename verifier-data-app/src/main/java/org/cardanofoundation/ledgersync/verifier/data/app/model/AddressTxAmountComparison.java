package org.cardanofoundation.ledgersync.verifier.data.app.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressTxAmountComparison {
    AddressTxAmountKey addressTxAmountKey;
    BigInteger quantity;
}
