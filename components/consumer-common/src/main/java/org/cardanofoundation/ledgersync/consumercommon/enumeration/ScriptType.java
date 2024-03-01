package org.cardanofoundation.ledgersync.consumercommon.enumeration;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public enum ScriptType {
  MULTISIG("multisig"),
  TIMELOCK("timelock"),
  PLUTUSV1("plutusv1"),
  PLUTUSV2("plutusv2"),
  PLUTUSV3("plutusv3");

  String value;

  private static final Map<String, ScriptType> rewardTypeMap = new HashMap<>();

  static {
    for (ScriptType type : ScriptType.values()) {
      rewardTypeMap.put(type.value, type);
    }
  }

  public static ScriptType fromValue(String value) {
    return rewardTypeMap.get(value);
  }
}