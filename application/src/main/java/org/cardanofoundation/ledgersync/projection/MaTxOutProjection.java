package org.cardanofoundation.ledgersync.projection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaTxOutProjection {

    String fingerprint;
    Long txOutId;
    BigInteger quantity;
}
