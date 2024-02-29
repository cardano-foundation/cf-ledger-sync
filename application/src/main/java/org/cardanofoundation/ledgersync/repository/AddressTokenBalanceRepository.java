package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.AddressTokenBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressTokenBalanceRepository
        extends JpaRepository<AddressTokenBalance, Long>, CustomAddressTokenBalanceRepository {

}
