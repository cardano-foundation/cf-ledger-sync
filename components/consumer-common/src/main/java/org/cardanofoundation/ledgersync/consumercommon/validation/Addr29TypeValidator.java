package org.cardanofoundation.ledgersync.consumercommon.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;

public class Addr29TypeValidator implements ConstraintValidator<Addr29Type, String> {

  @Override
  public boolean isValid(String bytes, ConstraintValidatorContext constraintValidatorContext) {
    return bytes.length() == ValidationConstant.ADDRESS_MAX_BYTES;
  }
}
