package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("LS_WithdrawalRepository")
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
