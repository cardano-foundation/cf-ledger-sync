package org.cardanofoundation.explorer.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.explorer.consumercommon.validation.TxIndexValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TxIndexValidatorTest {

  TxIndexValidator validator = new TxIndexValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number.shortValue(), null));
    number = BigInteger.valueOf(255);
    Assertions.assertTrue(validator.isValid(number.shortValue(), null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number.shortValue(), null));
  }
}
