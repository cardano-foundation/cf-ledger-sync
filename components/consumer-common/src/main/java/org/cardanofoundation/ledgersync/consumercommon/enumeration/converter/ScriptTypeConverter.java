package org.cardanofoundation.ledgersync.consumercommon.enumeration.converter;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ScriptTypeConverter implements AttributeConverter<ScriptType, String> {

  @Override
  public String convertToDatabaseColumn(ScriptType attribute) {
    return attribute.getValue();
  }

  @Override
  public ScriptType convertToEntityAttribute(String dbData) {
    return ScriptType.fromValue(dbData);
  }
}
