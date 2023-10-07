package org.cardanofoundation.explorer.consumercommon.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum EraType {
  BYRON_EBB(0),
  BYRON(1),
  SHELLEY(2),
  ALLEGRA(3),
  MARY(4),
  ALONZO(5),
  BABBAGE(6),
  CONWAY(7);

  int value;

  public static EraType valueOf(int era) {
    switch (era) {
      case 1:
        return BYRON;
      case 2:
        return SHELLEY;
      case 3:
        return ALLEGRA;
      case 4:
        return MARY;
      case 5:
        return ALONZO;
      case 6:
        return BABBAGE;
      case 7:
        return CONWAY;
      case 0:
        return BYRON;
      default:
        throw new RuntimeException(
            String.format("Era %d is not supported. Please pick a value between 0 and 6.", era));
    }
  }
}
