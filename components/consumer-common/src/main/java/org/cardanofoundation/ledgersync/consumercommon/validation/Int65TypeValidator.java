package org.cardanofoundation.ledgersync.consumercommon.validation;

import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;
import java.math.BigInteger;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Int65TypeValidator implements ConstraintValidator<Int65Type, BigInteger> {

  public static final BigInteger MAX_64_BYTES = ValidationConstant.getMaxInt65();
  public static final BigInteger MIN_64_BYTES = ValidationConstant.getMinInt65();

  /**
   * Checking if input number greater than MAX_64_BYTES or smaller MIN_64_BYTES .
   * if not return true else false
   *
   * @param number                     number
   * @param constraintValidatorContext
   * @return boolean
   */
  @Override
  public boolean isValid(BigInteger number, ConstraintValidatorContext constraintValidatorContext) {
    return number.compareTo(MIN_64_BYTES) >= BigInteger.ZERO.intValue()
        && number.compareTo(MAX_64_BYTES) < BigInteger.ZERO.intValue();
  }
}
