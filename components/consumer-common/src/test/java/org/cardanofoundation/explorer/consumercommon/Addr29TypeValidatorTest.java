package org.cardanofoundation.explorer.consumercommon;

import org.cardanofoundation.explorer.consumercommon.validation.Addr29TypeValidator;

import static org.cardanofoundation.explorer.consumercommon.constants.ValidationConstant.ADDRESS_MAX_BYTES;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Addr29TypeValidatorTest {

  Addr29TypeValidator validator = new Addr29TypeValidator();

  @Test
  void isValid() {
    char[] bytesLength58 = new char[ADDRESS_MAX_BYTES];
    Assertions.assertTrue(validator.isValid(String.valueOf(bytesLength58), null));
  }

  @Test
  void isNotValidLengthSmaller() {
    char[] bytesLength58 = new char[0];
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(String.valueOf(bytesLength58), null));
  }

  @Test
  void isNotValidLengthGetter() {
    char[] bytesLength58 = new char[59];
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(String.valueOf(bytesLength58), null));
  }
}
