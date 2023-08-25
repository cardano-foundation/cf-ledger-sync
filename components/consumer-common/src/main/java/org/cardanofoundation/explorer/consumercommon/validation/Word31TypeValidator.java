package org.cardanofoundation.explorer.consumercommon.validation;

import java.math.BigInteger;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Word31TypeValidator implements ConstraintValidator<Word31Type, Integer> {

  /**
   * Checking if input number greater or equal to 0 if not return true else false
   *
   * @param integer                    number
   * @param constraintValidatorContext
   * @return boolean
   */
  @Override
  public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
    return integer >= BigInteger.ZERO.intValue();
  }
}
