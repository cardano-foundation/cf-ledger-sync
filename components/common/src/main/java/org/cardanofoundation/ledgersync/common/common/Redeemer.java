package org.cardanofoundation.ledgersync.common.common;

import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.plutus.spec.ExUnits;
import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Redeemer {

  private RedeemerTag tag;
  private BigInteger index;
  private Datum plutusData;
  private ExUnits exUnits;

  public static Redeemer deserialize(Array redeemerDI)
      throws CborDeserializationException, CborException {
    List<DataItem> redeemerDIList = redeemerDI.getDataItems();
    if (redeemerDIList == null || redeemerDIList.size() != 4) {
      throw new CborDeserializationException(
          "Redeemer deserialization error. Invalid no of DataItems");
    }

    DataItem tagDI = redeemerDIList.get(0);
    DataItem indexDI = redeemerDIList.get(1);
    DataItem dataDI = redeemerDIList.get(2);
    DataItem exUnitDI = redeemerDIList.get(3);

    Redeemer redeemer = new Redeemer();

    // Tag
    int tagValue = ((UnsignedInteger) tagDI).getValue().intValue();
    if (tagValue == 0) {
      redeemer.setTag(RedeemerTag.Spend);
    } else if (tagValue == 1) {
      redeemer.setTag(RedeemerTag.Mint);
    } else if (tagValue == 2) {
      redeemer.setTag(RedeemerTag.Cert);
    } else if (tagValue == 3) {
      redeemer.setTag(RedeemerTag.Reward);
    }

    // Index
    redeemer.setIndex(((UnsignedInteger) indexDI).getValue());

    // Redeemer data
    redeemer.setPlutusData(Datum.from(dataDI));

    // Redeemer resource usage (ExUnits)
    redeemer.setExUnits(ExUnits.deserialize((Array) exUnitDI));

    return redeemer;
  }
}
