package org.cardanofoundation.ledgersync.aggregate.account.repository;


import org.cardanofoundation.ledgersync.aggregate.account.model.AddressToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressTokenRepository extends JpaRepository<AddressToken, Long> {

    List<AddressToken> findAllByBlockNumberGreaterThan(Long blockNo);
    @Modifying
    void deleteAllByTxHashIn(Collection<String> txs);

    void deleteAllBySlotGreaterThan(Long slot);
}
