package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.StakeDeregistration;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StakeDeregistrationRepository extends JpaRepository<StakeDeregistration, Long> {

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
