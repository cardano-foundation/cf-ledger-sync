package org.cardanofoundation.ledgersync.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word64TypeValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Word64TypeValidatorTest {

  Word64TypeValidator validator = new Word64TypeValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number, null));
    number = ValidationConstant.getMaxLoveLace();
    Assertions.assertTrue(validator.isValid(number, null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number, null));
  }
}
