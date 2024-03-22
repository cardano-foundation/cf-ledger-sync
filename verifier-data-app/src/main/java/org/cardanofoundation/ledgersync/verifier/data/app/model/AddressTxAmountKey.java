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
public class AddressTxAmountKey {
    String address;
    String txHash;
    String unit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressTxAmountKey that = (AddressTxAmountKey) o;
        return Objects.equals(address, that.address) && Objects.equals(txHash, that.txHash) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, txHash, unit);
    }
}
