package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.PoolRetire;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PoolRetireRepository extends JpaRepository<PoolRetire, Long> {

    @Modifying
    void deleteAllByAnnouncedTxIn(Collection<Tx> announcedTxs);
}
