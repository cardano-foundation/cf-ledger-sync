package org.cardanofoundation.ledgersync.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.ledgersync.consumercommon.validation.Int65TypeValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Int65TypeValidatorTest {

  Int65TypeValidator validator = new Int65TypeValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number, null));
    number = Int65TypeValidator.MIN_64_BYTES;
    Assertions.assertTrue(validator.isValid(number, null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1).add(Int65TypeValidator.MIN_64_BYTES);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number, null));

    number = BigInteger.ONE.add(Int65TypeValidator.MAX_64_BYTES);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number, null));
  }
}
