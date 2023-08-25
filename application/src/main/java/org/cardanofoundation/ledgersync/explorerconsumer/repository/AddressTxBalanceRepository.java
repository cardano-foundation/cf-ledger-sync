package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.AddressTxBalance;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.AddressTxBalanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressTxBalanceRepository extends JpaRepository<AddressTxBalance, Long> {

    @Query("SELECT atb.address.address as address, "
            + "atb.balance as balance, "
            + "atb.tx.hash as txHash, "
            + "atb.tx.block.epochNo as epochNo "
            + "FROM AddressTxBalance atb WHERE atb.tx in (:txs)")
    List<AddressTxBalanceProjection> findAllByTxIn(@Param("txs") Collection<Tx> txs);

    @Modifying
    void deleteAllByTxIn(Collection<Tx> txs);
}
