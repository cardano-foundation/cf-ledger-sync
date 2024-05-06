package org.cardanofoundation.ledgersync.verifier.data.app.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdaPotsComparisonKey {
  Long slotNo;
  Long epochNo;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AdaPotsComparisonKey that = (AdaPotsComparisonKey) o;
    return slotNo.equals(that.slotNo) && epochNo.equals(that.epochNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotNo, epochNo);
  }
}
