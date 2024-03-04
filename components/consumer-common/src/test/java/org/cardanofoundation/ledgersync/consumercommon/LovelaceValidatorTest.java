package org.cardanofoundation.ledgersync.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.ledgersync.consumercommon.validation.LovelaceValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LovelaceValidatorTest {

  LovelaceValidator validator = new LovelaceValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number, null));
    number = LovelaceValidator.MAX_64_BYTES;
    Assertions.assertTrue(validator.isValid(number, null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number, null));
  }
}
