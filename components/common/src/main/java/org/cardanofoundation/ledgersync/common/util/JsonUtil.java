package org.cardanofoundation.ledgersync.common.util;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Slf4j
public final class JsonUtil {

  private JsonUtil() {
    mapper.disable(SerializationFeature.INDENT_OUTPUT);
  }

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String getPrettyJson(Object obj) {
    if (obj == null) {
      return null;
    }
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Json processing error", e);
      return obj.toString();
    }
  }

  public static String getPrettyJson(String jsonStr) {
    if (jsonStr == null) {
      return null;
    }

    try {
      Object json = mapper.readValue(jsonStr, Object.class);
      return mapper.writeValueAsString(json);
    } catch (Exception e) {
      log.error("Json processing error", e);
      return jsonStr;
    }
  }

  public static JsonNode parseJson(String jsonContent) throws JsonProcessingException {
    return mapper.readTree(jsonContent);
  }
}
