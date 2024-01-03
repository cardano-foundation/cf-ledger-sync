package org.cardanofoundation.ledgersync.aggregate.account.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTokenRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.aggregate.account.service.RollbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RollbackServiceImpl implements RollbackService {

    AddressTokenRepository addressTokenRepository;
    AddressTxBalanceRepository addressTxBalanceRepository;
    AddressBalanceService addressBalanceService;

    @Override
    @Transactional
    public void rollBackFrom(long blockNo) {
        log.info("---------------------------------------------------------------------");
        log.warn("Roll back from block no {}", blockNo);
        startRollback(blockNo);
        log.info("---------------------------------------------------------------------");
    }

    private void startRollback(long blockNo) {
        addressBalanceService.rollbackAddressBalances(blockNo);
        addressTokenRepository.deleteAllByBlockNumberGreaterThan(blockNo);
        addressTxBalanceRepository.deleteAllByBlockNumberGreaterThan(blockNo);
    }
}
