package org.cardanofoundation.explorer.consumercommon.enumeration;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public enum TokenType {
  NATIVE_TOKEN(0),
  TOKEN(1),
  ALL_TOKEN_TYPE(2);

  int value;

  private static final Map<Integer, TokenType> tokenTypeMap = new HashMap<>();

  static {
    for (TokenType type : TokenType.values()) {
      tokenTypeMap.put(type.value,type);
    }
  }

  public static TokenType fromValue(Integer value) {
    return tokenTypeMap.get(value);
  }
}
