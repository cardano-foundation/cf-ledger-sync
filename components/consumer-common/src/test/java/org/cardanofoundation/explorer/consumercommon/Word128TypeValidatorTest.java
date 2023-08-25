package org.cardanofoundation.explorer.consumercommon;

import java.math.BigInteger;

import org.cardanofoundation.explorer.consumercommon.constants.ValidationConstant;
import org.cardanofoundation.explorer.consumercommon.validation.Word64TypeValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Word128TypeValidatorTest {

  Word64TypeValidator validator = new Word64TypeValidator();

  @Test
  void isValid() {
    BigInteger number = BigInteger.ZERO;
    Assertions.assertTrue(validator.isValid(number, null));
    number = ValidationConstant.getMaxWord128();
    Assertions.assertTrue(validator.isValid(number, null));
  }

  @Test
  void isNotValidLengthSmaller() {
    BigInteger number = BigInteger.valueOf(-1);
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(number, null));
  }
}
