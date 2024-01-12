package org.cardanofoundation.ledgersync.aggregate.account.repository;


import org.cardanofoundation.ledgersync.aggregate.account.repository.model.AddressToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressTokenRepository extends JpaRepository<AddressToken, Long> {

    List<AddressToken> findAllByBlockNumberGreaterThan(Long blockNo);

    void deleteAllBySlotGreaterThan(Long slot);
}
