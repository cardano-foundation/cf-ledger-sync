package org.cardanofoundation.ledgersync.common.common.plutus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapPlutusData implements PlutusData {

  @Builder.Default
  private java.util.Map<MapPlutusDataKey, MapPlutusDataValue> map = new LinkedHashMap<>();

  @JsonProperty("map")
  public List<MapPlutusElement> getMap() {
    return map.keySet()
        .stream().map(key ->
            MapPlutusElement.builder()
                .k(key.getKey())
                .v(map.get(key).getValue())
                .build()
        )
        .toList();
  }

  public static MapPlutusData deserialize(Map mapDI) throws CborDeserializationException {
    if (Objects.isNull(mapDI)) {
      return null;
    }

    MapPlutusData mapPlutusData = new MapPlutusData();
    for (DataItem keyDI : mapDI.getKeys()) {
      PlutusData key = PlutusData.deserialize(keyDI);
      PlutusData value = PlutusData.deserialize(mapDI.get(keyDI));

      mapPlutusData.put(MapPlutusDataKey.builder().key(key).build(),
          MapPlutusDataValue.builder().value(value).build());
    }

    return mapPlutusData;
  }

  public MapPlutusData put(
      MapPlutusDataKey key, MapPlutusDataValue value) {
    if (map == null) {
      map = new LinkedHashMap<>();
    }

    map.put(key, value);

    return this;
  }

  @Override
  public DataItem serialize() throws CborSerializationException {
    if (map == null) {
      return null;
    }

    Map plutusDataMap = new Map();
    for (java.util.Map.Entry<MapPlutusDataKey, MapPlutusDataValue> entry : map.entrySet()) {
      DataItem key = entry.getKey().getKey().serialize();
      DataItem value = entry.getValue().getValue().serialize();

      if (key == null) {
        throw new CborSerializationException(
            "Cbor serialization failed for PlutusData.  NULL serialized value found for key");
      }

      if (value == null) {
        throw new CborSerializationException(
            "Cbor serialization failed for PlutusData.  NULL serialized value found for value");
      }

      plutusDataMap.put(key, value);
    }

    return plutusDataMap;
  }

}