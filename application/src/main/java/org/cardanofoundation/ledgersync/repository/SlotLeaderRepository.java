package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.SlotLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SlotLeaderRepository extends JpaRepository<SlotLeader, Long> {

    @Transactional(readOnly = true)
    Optional<SlotLeader> findSlotLeaderByHash(String hashRaw);
}
