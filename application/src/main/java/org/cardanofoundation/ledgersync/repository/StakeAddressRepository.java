package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface StakeAddressRepository extends JpaRepository<StakeAddress, Long> {

    @Transactional(readOnly = true)
    List<StakeAddress> findByHashRawIn(Collection<String> hashRaw);
}
