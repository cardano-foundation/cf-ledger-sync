package org.cardanofoundation.ledgersync.consumercommon;

import org.cardanofoundation.ledgersync.consumercommon.validation.Asset32TypeValidator;

import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Asset32TypeValidatorTest {

  Asset32TypeValidator validator = new Asset32TypeValidator();

  @Test
  void isValid() {
    byte[] bytesLength32 = new byte[ValidationConstant.ASSET_MAX_BYTES];
    Assertions.assertTrue(validator.isValid(bytesLength32, null));
  }

  @Test
  void isNotValidLengthSmaller() {
    byte[] bytesLength32 = new byte[0];
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(bytesLength32, null));
  }

  @Test
  void isNotValidLengthGetter() {
    byte[] bytesLength32 = new byte[59];
    Assertions.assertEquals(Boolean.FALSE, validator.isValid(bytesLength32, null));
  }
}
