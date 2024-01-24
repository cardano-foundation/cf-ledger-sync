package org.cardanofoundation.ledgersync.consumercommon.enumeration.converter;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.RewardType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RewardTypeConverter implements AttributeConverter<RewardType, String> {

  @Override
  public String convertToDatabaseColumn(RewardType attribute) {
    return attribute.getValue();
  }

  @Override
  public RewardType convertToEntityAttribute(String dbData) {
    return RewardType.fromValue(dbData);
  }
}
