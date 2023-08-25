package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.ExtraKeyWitness;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface ExtraKeyWitnessRepository extends JpaRepository<ExtraKeyWitness, Long> {


    @Query("SELECT e.hash FROM ExtraKeyWitness e WHERE e.hash IN :collection ")
    Set<String> findByHashIn(@Param("collection") Collection<String> collection);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
