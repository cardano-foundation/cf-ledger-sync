package org.cardanofoundation.explorer.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.explorer.consumercommon.validation.Word31TypeValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Word31TypeValidatorTest {

  Word31TypeValidator validator = new Word31TypeValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number.intValue(), null));
    number = BigInteger.valueOf(255);
    Assertions.assertTrue(validator.isValid(number.intValue(), null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number.intValue(), null));
  }
}
