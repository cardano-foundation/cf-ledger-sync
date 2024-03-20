package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.StakeRegistration;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("LS_StakeRegistrationRepository")
public interface StakeRegistrationRepository extends JpaRepository<StakeRegistration, Long> {

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
