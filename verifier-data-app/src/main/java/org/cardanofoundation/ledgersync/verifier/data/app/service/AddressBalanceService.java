package org.cardanofoundation.ledgersync.verifier.data.app.service;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;

import java.util.Map;
import java.util.Set;

public interface AddressBalanceService {
    Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses);
}
