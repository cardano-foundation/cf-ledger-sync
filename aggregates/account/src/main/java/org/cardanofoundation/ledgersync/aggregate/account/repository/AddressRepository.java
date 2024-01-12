package org.cardanofoundation.ledgersync.aggregate.account.repository;

import org.cardanofoundation.ledgersync.aggregate.account.repository.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    List<Address> findAllByAddressIn(Collection<String> addresses);
}
