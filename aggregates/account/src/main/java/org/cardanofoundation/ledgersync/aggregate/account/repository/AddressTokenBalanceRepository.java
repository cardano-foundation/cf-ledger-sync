package org.cardanofoundation.ledgersync.aggregate.account.repository;


import org.cardanofoundation.ledgersync.aggregate.account.model.AddressTokenBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressTokenBalanceRepository
        extends JpaRepository<AddressTokenBalance, Long> {

}
