package org.cardanofoundation.explorer.consumercommon.enumeration.converter;

import org.cardanofoundation.explorer.consumercommon.enumeration.TokenType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TokenTypeConverter implements AttributeConverter<TokenType,Integer> {

  @Override
  public Integer convertToDatabaseColumn(TokenType tokenType) {
    return tokenType.getValue();
  }

  @Override
  public TokenType convertToEntityAttribute(Integer type) {
    return TokenType.fromValue(type);
  }
}
