package org.cardanofoundation.ledgersync.common.common.plutus;

import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class BytesPlutusData implements PlutusData {

  @JsonProperty("bytes")
  private String bytes;

  public static BytesPlutusData deserialize(
      ByteString valueDI) throws CborDeserializationException {
    if (Objects.isNull(valueDI)) {
      return null;
    }

    return new BytesPlutusData(HexUtil.encodeHexString(valueDI.getBytes()));
  }

  public static BytesPlutusData of(byte[] bytes) {
    return new BytesPlutusData(HexUtil.encodeHexString(bytes));
  }

  public static BytesPlutusData of(String str) {
    return new BytesPlutusData(HexUtil.encodeHexString(str.getBytes(
        StandardCharsets.UTF_8)));
  }

  @Override
  public DataItem serialize() throws CborSerializationException {
    DataItem di = null;
    if (Objects.nonNull(bytes)) {
      di = new ByteString(HexUtil.decodeHexString(bytes));
    }
    return di;
  }

}
