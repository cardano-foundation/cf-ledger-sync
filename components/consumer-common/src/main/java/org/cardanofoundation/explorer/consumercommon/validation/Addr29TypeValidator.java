package org.cardanofoundation.explorer.consumercommon.validation;

import static org.cardanofoundation.explorer.consumercommon.constants.ValidationConstant.ADDRESS_MAX_BYTES;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Addr29TypeValidator implements ConstraintValidator<Addr29Type, String> {

  @Override
  public boolean isValid(String bytes, ConstraintValidatorContext constraintValidatorContext) {
    return bytes.length() == ADDRESS_MAX_BYTES;
  }
}
