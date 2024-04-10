package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxWitness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Collection;

public interface TxWitnessRepository extends JpaRepository<TxWitness, Long> {

  @Modifying
  void deleteAllByTxIn(Collection<Tx> txs);
}
