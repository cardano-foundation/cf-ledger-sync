package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.AddressToken;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressTokenRepository extends JpaRepository<AddressToken, Long> {

    List<AddressToken> findAllByTxIn(Collection<Tx> txs);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
