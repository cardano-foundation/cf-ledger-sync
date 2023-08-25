package org.cardanofoundation.ledgersync.common.common.plutus;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Special;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPlutusData implements PlutusData {

  @Builder.Default
  @JsonProperty("list")
  private List<PlutusData> list = new ArrayList<>();

  @JsonIgnore
  @Builder.Default
  private boolean isChunked = true;

  public static ListPlutusData of(
      PlutusData... plutusDataList) {
    ListPlutusData listPlutusData = new ListPlutusData();
    Arrays.stream(plutusDataList).forEach(listPlutusData::add);

    return listPlutusData;
  }

  public static ListPlutusData deserialize(
      Array arrayDI) throws CborDeserializationException {
    if (arrayDI == null) {
      return null;
    }

    boolean isChunked = false;
    ListPlutusData listPlutusData = new ListPlutusData();
    for (DataItem di : arrayDI.getDataItems()) {
      if (di == Special.BREAK) {
        isChunked = true;
        break;
      }

      PlutusData plutusData = PlutusData.deserialize(di);
      if (plutusData == null) {
        throw new CborDeserializationException(
            "Null value found during PlutusData de-serialization");
      }

      listPlutusData.add(plutusData);
    }

    listPlutusData.isChunked = isChunked;

    return listPlutusData;
  }

  public void add(PlutusData plutusData) {
    if (Objects.isNull(this.list)) {
      this.list = new ArrayList<>();
    }

    this.list.add(plutusData);
  }

  @Override
  public DataItem serialize() throws CborSerializationException {
    if (Objects.isNull(list)) {
      return null;
    }

    Array plutusDataArray = new Array();

    if (list.isEmpty()) {
      return plutusDataArray;
    }

    if (isChunked) {
      plutusDataArray.setChunked(true);
    }

    for (PlutusData plutusData : list) {
      DataItem di = plutusData.serialize();
      if (di == null) {
        throw new CborSerializationException(
            "Cbor Serialization failed for plutus data. NULL serialized value found in the list");
      }

      plutusDataArray.add(di);
    }

    if (isChunked) {
      plutusDataArray.add(Special.BREAK);
    }

    return plutusDataArray;
  }

  @JsonIgnore
  boolean getChunked(){
    return this.isChunked;
  }

  public String toString() {
    return JsonUtil.getPrettyJson(this.getList()).replace("\n","");
  }
}