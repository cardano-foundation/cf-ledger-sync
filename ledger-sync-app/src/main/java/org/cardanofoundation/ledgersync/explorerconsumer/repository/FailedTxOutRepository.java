package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.FailedTxOut;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FailedTxOutRepository extends JpaRepository<FailedTxOut, Long> {

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
