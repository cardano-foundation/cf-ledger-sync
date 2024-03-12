package org.cardanofoundation.ledgersync.account.storage.impl;

import com.bloxbean.cardano.yaci.store.account.AccountStoreProperties;
import com.bloxbean.cardano.yaci.store.common.util.ListUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.account.domain.AddressTxAmount;
import org.cardanofoundation.ledgersync.account.storage.AddressTxAmountStorage;
import org.cardanofoundation.ledgersync.account.storage.impl.mapper.AggrMapper;
import org.cardanofoundation.ledgersync.account.storage.impl.model.AddressTxAmountEntity;
import org.cardanofoundation.ledgersync.account.storage.impl.repository.AddressTxAmountRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.cardanofoundation.ledgersync.account.jooq.Tables.ADDRESS_TX_AMOUNT;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressTxAmountStorageImpl implements AddressTxAmountStorage {
    private final AddressTxAmountRepository addressTxAmountRepository;
    private final DSLContext dsl;
    private final AccountStoreProperties accountStoreProperties;

    private final AggrMapper aggrMapper = AggrMapper.INSTANCE;

    @PostConstruct
    public void postConstruct() {
        this.dsl.settings().setBatchSize(accountStoreProperties.getJooqWriteBatchSize());
    }

    @Override
    @Transactional
    public void save(List<AddressTxAmount> addressTxAmount) {
        var addressTxAmtEntities = addressTxAmount.stream()
                .map(addressTxAmount1 -> aggrMapper.toAddressTxAmountEntity(addressTxAmount1))
                .toList();

        if (accountStoreProperties.isParallelWrite()
                && addressTxAmtEntities.size() > accountStoreProperties.getWriteThreadDefaultBatchSize()) {
            int partitionSize = getPartitionSize(addressTxAmtEntities.size());
            log.info("\tPartition address_tx_amount size : {}", partitionSize);
            ListUtil.partitionAndApplyInParallel(addressTxAmtEntities, partitionSize, this::saveBatch);
        } else {
            saveBatch(addressTxAmtEntities);
        }
    }

    private void saveBatch(List<AddressTxAmountEntity> addressTxAmountEntities) {
        var inserts = addressTxAmountEntities.stream()
                .map(addressTxAmount -> dsl.insertInto(ADDRESS_TX_AMOUNT)
                        .set(ADDRESS_TX_AMOUNT.ADDRESS, addressTxAmount.getAddress())
                        .set(ADDRESS_TX_AMOUNT.UNIT, addressTxAmount.getUnit())
                        .set(ADDRESS_TX_AMOUNT.TX_HASH, addressTxAmount.getTxHash())
                        .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                        .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                        .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                        .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                        .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                        .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                        .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                        .onDuplicateKeyUpdate()
                        .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                        .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                        .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                        .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                        .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                        .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                        .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())).toList();
        dsl.batch(inserts).execute();

        /**
        dsl.batched(c -> {
            for (var addressTxAmount : addressTxAmountEntities) {
                c.dsl().insertInto(ADDRESS_TX_AMOUNT)
                        .set(ADDRESS_TX_AMOUNT.ADDRESS, addressTxAmount.getAddress())
                        .set(ADDRESS_TX_AMOUNT.UNIT, addressTxAmount.getUnit())
                        .set(ADDRESS_TX_AMOUNT.TX_HASH, addressTxAmount.getTxHash())
                        .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                        .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                        .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                        .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                        .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                        .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                        .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                        .onDuplicateKeyUpdate()
                        .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                        .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                        .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                        .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                        .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                        .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                        .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                        .execute();
            }
        });
        **/
    }

    @Override
    public int deleteAddressBalanceBySlotGreaterThan(Long slot) {
        return addressTxAmountRepository.deleteAddressBalanceBySlotGreaterThan(slot);
    }

    private int getPartitionSize(int totalSize) {
        int partitionSize = totalSize;
        if (totalSize > accountStoreProperties.getWriteThreadDefaultBatchSize()) {
            partitionSize = totalSize / accountStoreProperties.getWriteThreadCount();
            log.info("\tAddress Tx Amt Partition size : {}", partitionSize);
        } else {
            log.info("\tAddress Tx Amt Partition size : {}", partitionSize);
        }
        return partitionSize;
    }
}
