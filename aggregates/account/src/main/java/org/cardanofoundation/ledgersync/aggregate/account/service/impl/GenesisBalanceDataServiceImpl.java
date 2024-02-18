package org.cardanofoundation.ledgersync.aggregate.account.service.impl;

import com.bloxbean.cardano.yaci.store.events.GenesisBalance;
import com.bloxbean.cardano.yaci.store.events.GenesisBlockEvent;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.model.Address;
import org.cardanofoundation.ledgersync.aggregate.account.repository.model.AddressTxBalance;
import org.cardanofoundation.ledgersync.aggregate.account.service.GenesisBalanceDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenesisBalanceDataServiceImpl implements GenesisBalanceDataService {
    private final AddressRepository addressRepository;
    private final AddressTxBalanceRepository addressTxBalanceRepository;

    @Override
    @Transactional
    public void handleGenesisBalance(GenesisBlockEvent genesisBlockEvent) {
        final List<GenesisBalance> genesisBalances = genesisBlockEvent.getGenesisBalances();

        Map<String, Address> addressMap = new HashMap<>();
        Map<String, AddressTxBalance> addressTxBalanceMap = new HashMap<>();

        genesisBalances.forEach(genesisBalance -> {
            var address = genesisBalance.getAddress();
            var addressEntity = addressMap.getOrDefault(address, new Address());

            addressEntity.setAddress(address);
            addressEntity.setAddressHasScript(Boolean.FALSE);
            if (addressEntity.getTxCount() != null) {
                addressEntity.setTxCount(addressEntity.getTxCount() + 1);
            } else {
                addressEntity.setTxCount(1L);
            }

            if (addressEntity.getBalance() != null) {
                addressEntity.setBalance(addressEntity.getBalance().add(genesisBalance.getBalance()));
            } else {
                addressEntity.setBalance(genesisBalance.getBalance());
            }

            addressMap.put(address, addressEntity);
            addressTxBalanceMap.put(address, AddressTxBalance.builder()
                    .balance(genesisBalance.getBalance())
                    .txHash(genesisBalance.getTxnHash())
                    .slot(genesisBlockEvent.getSlot())
                    .blockNumber(genesisBlockEvent.getBlock())
                    .blockTime(Timestamp.valueOf(LocalDateTime.ofEpochSecond(
                            genesisBlockEvent.getBlockTime(), 0, ZoneOffset.ofHours(0))))
                    .build());
        });

        addressRepository.saveAll(addressMap.values());
        addressTxBalanceMap.forEach((address, addressTxBalance) -> addressTxBalance.setAddress(addressMap.get(address)));
        addressTxBalanceRepository.saveAll(addressTxBalanceMap.values());
    }

}
