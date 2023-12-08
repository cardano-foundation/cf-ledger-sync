package org.cardanofoundation.ledgersync.aggregate.account.repository;

import org.cardanofoundation.ledgersync.aggregate.account.model.AddressTxBalance;
import org.cardanofoundation.ledgersync.aggregate.account.projection.AddressTxBalanceProjection;
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
            + "atb.txHash as txHash "
            + "FROM AddressTxBalance atb WHERE atb.txHash in (:txs)")
    List<AddressTxBalanceProjection> findAllByTxIn(@Param("txs") Collection<String> txs);

    @Modifying
    void deleteAllByTxHashIn(Collection<String> txs);
}
