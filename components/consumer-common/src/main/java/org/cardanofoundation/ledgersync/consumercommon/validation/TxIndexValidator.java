package org.cardanofoundation.ledgersync.consumercommon.validation;

import java.math.BigInteger;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TxIndexValidator implements ConstraintValidator<TxIndex, Short> {

  @Override
  public boolean isValid(Short aShort, ConstraintValidatorContext constraintValidatorContext) {
    return aShort >= BigInteger.ZERO.shortValue();
  }
}
