package org.cardanofoundation.ledgersync.verifier.data.app.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressBalanceComparison {
    AddressBalanceComparisonKey addressBalanceComparisonKey;
    BigInteger balance;
}
