package org.cardanofoundation.ledgersync.common.common.plutus;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.Number;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
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
public class BigIntPlutusData implements PlutusData {

  @JsonProperty("int")
  private BigInteger bigInteger;

  public static BigIntPlutusData deserialize(
      Number numberDI) throws CborDeserializationException {
    if (Objects.isNull(numberDI)) {
      return null;
    }

    return new BigIntPlutusData(numberDI.getValue());
  }

  public static BigIntPlutusData of(int i) {
    return new BigIntPlutusData(BigInteger.valueOf(i));
  }

  public static BigIntPlutusData of(long l) {
    return new BigIntPlutusData(BigInteger.valueOf(l));
  }

  public static BigIntPlutusData of(BigInteger b) {
    return new BigIntPlutusData(b);
  }

  @Override
  public DataItem serialize() throws CborSerializationException {
    DataItem di = null;
    if (Objects.nonNull(bigInteger)) {
      if (bigInteger.compareTo(BigInteger.ZERO) >= BigInteger.ZERO.intValue()) {
        di = new UnsignedInteger(bigInteger);
      } else {
        di = new NegativeInteger(bigInteger);
      }
    }

    return di;
  }


}