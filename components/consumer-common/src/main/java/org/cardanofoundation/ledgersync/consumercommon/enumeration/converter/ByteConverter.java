package org.cardanofoundation.ledgersync.consumercommon.enumeration.converter;


import com.bloxbean.cardano.client.util.HexUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ByteConverter implements AttributeConverter<String, byte[]> {

  @Override
  public byte[] convertToDatabaseColumn(String data) {
    return HexUtil.decodeHexString(data);
  }

  @Override
  public String convertToEntityAttribute(byte[] bytes) {
    return HexUtil.encodeHexString(bytes);
  }
}
