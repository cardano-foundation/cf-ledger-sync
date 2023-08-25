package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.ReferenceTxIn;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReferenceInputRepository extends JpaRepository<ReferenceTxIn, Long> {

    @Modifying
    void deleteAllByTxInIn(Collection<Tx> txIns);
}
