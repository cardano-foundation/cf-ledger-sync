package org.cardanofoundation.ledgersync.common.util;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Number;
import co.nstant.in.cbor.model.RationalNumber;
import co.nstant.in.cbor.model.UnicodeString;
import org.cardanofoundation.ledgersync.common.exception.CborRuntimeException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;


public final class CborSerializationUtil {

  private CborSerializationUtil() {

  }

  /**
   * Covert a CBOR DataItem to BigInteger
   *
   * @param valueItem cbor dataItem
   * @return BigInteger
   */
  public static BigInteger toBigInteger(DataItem valueItem) {
    BigInteger value = null;
    if (MajorType.UNSIGNED_INTEGER.equals(valueItem.getMajorType())
        || MajorType.NEGATIVE_INTEGER.equals(valueItem.getMajorType())) {
      value = ((Number) valueItem).getValue();
    } else if (MajorType.BYTE_STRING.equals(
        valueItem.getMajorType())) { //For BigNum. >  2 pow 64 Tag 2
      if (valueItem.getTag().getValue() == 2) { //positive
        value = new BigInteger(((ByteString) valueItem).getBytes());
      } else if (valueItem.getTag().getValue() == 3) { //Negative
        value = new BigInteger(((ByteString) valueItem).getBytes()).multiply(
            BigInteger.valueOf(-1));
      }
    }

    if (Objects.isNull(value)) {
      throw new NullPointerException("item can't be null");
    }

    return value;
  }

  /**
   * Convert a CBOR DataItem to long
   *
   * @param valueItem cbor dataItem
   * @return number in long
   */
  public static long toLong(DataItem valueItem) {
    return toBigInteger(valueItem).longValue();
  }

  /**
   * Convert a CBOR DataItem to int
   *
   * @param valueItem cbor dataItem
   * @return number in integer
   */
  public static int toInt(DataItem valueItem) {
    return toBigInteger(valueItem).intValue();
  }

  public static short toShort(DataItem valueItem) {
    return toBigInteger(valueItem).shortValue();
  }

  public static byte toByte(DataItem valueItem) {
    return toBigInteger(valueItem).byteValue();
  }

  public static String toHex(DataItem di) {
    return HexUtil.encodeHexString(((ByteString) di).getBytes());
  }

  public static byte[] toBytes(DataItem di) {
    return ((ByteString) di).getBytes();
  }

  public static String toUnicodeString(DataItem di) {
    return ((UnicodeString) di).getString();
  }

  //convert [1,50] to "1/50"
  public static String toRationalNumberStr(DataItem di) {
    RationalNumber rdi = (RationalNumber) di;
    return rdi.getNumerator() + "/" + rdi.getDenominator();
  }

  //convert [1, 50] to 0.02
  public static BigDecimal toRationalNumber(DataItem di) {
    RationalNumber rdi = (RationalNumber) di;
    Number numerator = rdi.getNumerator();
    Number denominator = rdi.getDenominator();

    return new BigDecimal(numerator.getValue()).divide(new BigDecimal(denominator.getValue()));
  }

  /**
   * Serialize CBOR DataItem as byte[]
   *
   * @param value cbor data item
   * @return byte array
   */
  public static byte[] serialize(DataItem value) {
    return serialize(new DataItem[]{value}, true); //By default Canonical = true
  }

  /**
   * Serialize CBOR DataItem as byte[]
   *
   * @param value     cbor data item
   * @param canonical if true sort Data Item in order
   * @return byte array
   */
  public static byte[] serialize(DataItem value, boolean canonical) {
    return serialize(new DataItem[]{value}, canonical);
  }

  /**
   * Serialize CBOR DataItems as byte[]
   *
   * @param values cbor data item
   * @return byte array
   */
  public static byte[] serialize(DataItem[] values) throws CborRuntimeException {
    return serialize(values, true); //By default Canonical = true
  }

  /**
   * Serialize CBOR DataItems as byte[]
   *
   * @param values    cbor data item
   * @param canonical if true sort Data Item in order
   * @return byte array
   */
  public static byte[] serialize(DataItem[] values, boolean canonical) throws CborRuntimeException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CborBuilder cborBuilder = new CborBuilder();

    for (DataItem value : values) {
      cborBuilder.add(value);
    }

    try {
      if (canonical) {
        new CborEncoder(baos).encode(cborBuilder.build());
      } else {
        new CborEncoder(baos).nonCanonical().encode(cborBuilder.build());
      }
    } catch (CborException e) {
      throw new CborRuntimeException("Cbor serialization error", e);
    }

    return baos.toByteArray();

  }

  /**
   * Deserialize bytes to DataItem
   *
   * @param bytes cbor in byte array
   * @return DataItem
   */
  public static DataItem deserialize(byte[] bytes) {
    try {
      return CborDecoder.decode(bytes).get(0);
    } catch (CborException e) {
      throw new CborRuntimeException("Cbor de-serialization error", e);
    }
  }
}

