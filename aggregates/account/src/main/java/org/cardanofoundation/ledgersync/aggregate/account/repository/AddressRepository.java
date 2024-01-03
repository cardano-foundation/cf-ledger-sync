package org.cardanofoundation.ledgersync.aggregate.account.repository;

import org.cardanofoundation.ledgersync.aggregate.account.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

//    /*
//     * Selected entities' changes are not meant to be reflected immediately in database
//     * due to slow update time or any unexpected behaviour, hence this select query should
//     * be marked as read-only transaction, as the changes will be applied later through
//     * JPA save() or saveAll() methods
//     */
//    @EntityGraph(attributePaths = Address_.STAKE_ADDRESS)
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    List<Address> findAllByAddressIn(Collection<String> addresses);

    @Query("SELECT DISTINCT(a.stakeAddress) FROM Address a WHERE a IN (:addresses)")
    List<String> findAllStakeAddressByAddressIn(
            @Param("addresses") Collection<Address> addresses);
}
