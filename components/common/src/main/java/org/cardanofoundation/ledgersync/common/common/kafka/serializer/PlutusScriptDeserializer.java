package org.cardanofoundation.ledgersync.common.common.kafka.serializer;

import com.bloxbean.cardano.client.plutus.spec.PlutusScript;
import com.bloxbean.cardano.client.plutus.spec.PlutusV1Script;
import com.bloxbean.cardano.client.plutus.spec.PlutusV2Script;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

public class PlutusScriptDeserializer extends JsonDeserializer<PlutusScript> {

  private static final String CBOR_HEX = "cborHex";
  private static final String PLUTUS_SCRIPT_V_1 = "PlutusScriptV1";
  private static final String PLUTUS_SCRIPT_V_2 = "PlutusScriptV2";

  @SneakyThrows
  @Override
  public PlutusScript deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) {
    JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
    return deserialize(jsonNode);
  }

  private PlutusScript deserialize(JsonNode jsonNode) {
    String type = jsonNode.get("type").asText();
    switch (type) {
      case PLUTUS_SCRIPT_V_1:
        return deserializePlutusV1Script(jsonNode);
      case PLUTUS_SCRIPT_V_2:
        return deserializePlutusV2Script(jsonNode);
      default:
        throw new RuntimeException("Invalid Plutus script version");
    }
  }

  private PlutusV1Script deserializePlutusV1Script(JsonNode jsonNode) {
    PlutusV1Script plutusScript = new PlutusV1Script();
    String cborHex = jsonNode.get(CBOR_HEX).asText();
    plutusScript.setCborHex(cborHex);
    return plutusScript;
  }

  private PlutusV2Script deserializePlutusV2Script(JsonNode jsonNode) {
    PlutusV2Script plutusScript = new PlutusV2Script();
    String cborHex = jsonNode.get(CBOR_HEX).asText();
    plutusScript.setCborHex(cborHex);
    return plutusScript;
  }
}
