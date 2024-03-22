package org.cardanofoundation.ledgersync.verifier.data.app.repository.ledgersync;

import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmount;
import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmountId;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AddressBalanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AddressTxAmountRepository extends JpaRepository<AddressTxAmount, AddressTxAmountId> {

    List<AddressTxAmount> findByTxHashIn(Set<String> txHashes);

    @Query("select " +
            "ata.address as address, " +
            "ata.unit as unit, " +
            "sum(ata.quantity) as balance " +
            "from AddressTxAmount ata " +
            "where ata.unit='lovelace' and ata.address in :addresses " +
            "group by ata.unit, ata.address")
    List<AddressBalanceProjection> getAddressBalanceFromAddresses(Set<String> addresses);
}
