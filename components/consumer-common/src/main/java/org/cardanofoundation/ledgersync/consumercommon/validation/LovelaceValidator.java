package org.cardanofoundation.ledgersync.consumercommon.validation;

import org.cardanofoundation.ledgersync.consumercommon.constants.ValidationConstant;
import java.math.BigInteger;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LovelaceValidator implements ConstraintValidator<Lovelace, BigInteger> {

  public static final BigInteger MAX_64_BYTES = ValidationConstant.getMaxLoveLace();

  /**
   * Checking if input lovelace is out of range 2^64 or not
   *
   * @param number                     lovelace
   * @param constraintValidatorContext
   * @return boolean
   */
  @Override
  public boolean isValid(BigInteger number, ConstraintValidatorContext constraintValidatorContext) {
    return number.compareTo(BigInteger.valueOf(BigInteger.ZERO.longValue()))
        >= BigInteger.ZERO.intValue()
        && number.compareTo(MAX_64_BYTES) <= BigInteger.ZERO.intValue();
  }
}
