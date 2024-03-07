package org.cardanofoundation.ledgersync.account.storage;

import org.cardanofoundation.ledgersync.account.domain.AddressTxAmount;

import java.util.List;

public interface AddressTxAmountStorage {
    void save(List<AddressTxAmount> addressTxAmount);
    int deleteAddressBalanceBySlotGreaterThan(Long slot);
}
