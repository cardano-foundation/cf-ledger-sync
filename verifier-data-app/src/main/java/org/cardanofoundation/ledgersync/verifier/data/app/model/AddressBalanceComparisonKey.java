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
public class AddressBalanceComparisonKey {
    String address;
    String unit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressBalanceComparisonKey that = (AddressBalanceComparisonKey) o;
        return Objects.equals(address, that.address) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, unit);
    }
}
