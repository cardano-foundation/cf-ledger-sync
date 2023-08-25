package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.RedeemerData;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface RedeemerDataRepository extends JpaRepository<RedeemerData, Long> {

    @Transactional(readOnly = true)
    List<RedeemerData> findAllByHashIn(Collection<String> hashes);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
