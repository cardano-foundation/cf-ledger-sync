package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.UnconsumeTxIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Collection;

public interface UnconsumeTxInRepository extends JpaRepository<UnconsumeTxIn, Long> {

    @Modifying
    void deleteAllByTxInIn(Collection<Tx> txIns);
}
