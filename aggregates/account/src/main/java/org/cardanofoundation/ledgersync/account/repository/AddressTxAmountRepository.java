package org.cardanofoundation.ledgersync.account.repository;

import org.cardanofoundation.ledgersync.account.model.AddressTxAmountEntity;
import org.cardanofoundation.ledgersync.account.model.AddressTxAmountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressTxAmountRepository extends JpaRepository<AddressTxAmountEntity, AddressTxAmountId> {
    int deleteAddressBalanceBySlotGreaterThan(long slot);
}
