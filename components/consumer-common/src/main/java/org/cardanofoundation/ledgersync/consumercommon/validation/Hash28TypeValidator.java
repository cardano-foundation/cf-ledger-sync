package org.cardanofoundation.ledgersync.consumercommon.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;

public class Hash28TypeValidator implements ConstraintValidator<Hash28Type, String> {


  /**
   * Checking if input string length equal to 56 or not . if equal return true  else false
   *
   * @param string                     hash string
   * @param constraintValidatorContext
   * @return boolean
   */
  @Override
  public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
    return string.length() == ValidationConstant.HASH_28;
  }
}
