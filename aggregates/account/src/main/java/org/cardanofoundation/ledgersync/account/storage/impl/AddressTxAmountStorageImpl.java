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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.cardanofoundation.ledgersync.account.jooq.Tables.ADDRESS_TX_AMOUNT;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressTxAmountStorageImpl implements AddressTxAmountStorage {
    private final AddressTxAmountRepository addressTxAmountRepository;
    private final DSLContext dsl;
    private final AccountStoreProperties accountStoreProperties;
    private final PlatformTransactionManager transactionManager;

    private final AggrMapper aggrMapper = AggrMapper.INSTANCE;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void postConstruct() {
        this.dsl.settings().setBatchSize(accountStoreProperties.getJooqWriteBatchSize());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    @Transactional
    public void save(List<AddressTxAmount> addressTxAmount) {
        var addressTxAmtEntities = addressTxAmount.stream()
                .map(addressTxAmount1 -> aggrMapper.toAddressTxAmountEntity(addressTxAmount1))
                .toList();

        if (accountStoreProperties.isParallelWrite()) {
            transactionTemplate.execute(status -> {
                ListUtil.partitionAndApplyInParallel(addressTxAmtEntities, accountStoreProperties.getPerThreadBatchSize(), this::doSave);
                return null;
            });
        } else {
            doSave(addressTxAmtEntities);
        }
    }

    private void doSave(List<AddressTxAmountEntity> addressTxAmountEntities) {
        LocalDateTime localDateTime = LocalDateTime.now();

        var inserts = addressTxAmountEntities.stream()
                .map(addressTxAmount -> {
                    return dsl.insertInto(ADDRESS_TX_AMOUNT)
                            .set(ADDRESS_TX_AMOUNT.ADDRESS, addressTxAmount.getAddress())
                            .set(ADDRESS_TX_AMOUNT.UNIT, addressTxAmount.getUnit())
                            .set(ADDRESS_TX_AMOUNT.TX_HASH, addressTxAmount.getTxHash())
                            .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                            .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                            .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                            .set(ADDRESS_TX_AMOUNT.POLICY, addressTxAmount.getPolicy())
                            .set(ADDRESS_TX_AMOUNT.ASSET_NAME, addressTxAmount.getAssetName())
                            .set(ADDRESS_TX_AMOUNT.PAYMENT_CREDENTIAL, addressTxAmount.getPaymentCredential())
                            .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_HASH, addressTxAmount.getBlockHash())
                            .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                            .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                            .set(ADDRESS_TX_AMOUNT.UPDATE_DATETIME, localDateTime)
                            .onDuplicateKeyUpdate()
                            .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                            .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                            .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                            .set(ADDRESS_TX_AMOUNT.POLICY, addressTxAmount.getPolicy())
                            .set(ADDRESS_TX_AMOUNT.ASSET_NAME, addressTxAmount.getAssetName())
                            .set(ADDRESS_TX_AMOUNT.PAYMENT_CREDENTIAL, addressTxAmount.getPaymentCredential())
                            .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_HASH, addressTxAmount.getBlockHash())
                            .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                            .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                            .set(ADDRESS_TX_AMOUNT.UPDATE_DATETIME, localDateTime);
                }).toList();
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
                            .set(ADDRESS_TX_AMOUNT.POLICY, addressTxAmount.getPolicy())
                            .set(ADDRESS_TX_AMOUNT.ASSET_NAME, addressTxAmount.getAssetName())
                            .set(ADDRESS_TX_AMOUNT.PAYMENT_CREDENTIAL, addressTxAmount.getPaymentCredential())
                            .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_HASH, addressTxAmount.getBlockHash())
                            .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                            .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                            .set(ADDRESS_TX_AMOUNT.UPDATE_DATETIME, localDateTime)
                            .onDuplicateKeyUpdate()
                            .set(ADDRESS_TX_AMOUNT.SLOT, addressTxAmount.getSlot())
                            .set(ADDRESS_TX_AMOUNT.QUANTITY, addressTxAmount.getQuantity())
                            .set(ADDRESS_TX_AMOUNT.ADDR_FULL, addressTxAmount.getAddrFull())
                            .set(ADDRESS_TX_AMOUNT.POLICY, addressTxAmount.getPolicy())
                            .set(ADDRESS_TX_AMOUNT.ASSET_NAME, addressTxAmount.getAssetName())
                            .set(ADDRESS_TX_AMOUNT.PAYMENT_CREDENTIAL, addressTxAmount.getPaymentCredential())
                            .set(ADDRESS_TX_AMOUNT.STAKE_ADDRESS, addressTxAmount.getStakeAddress())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_HASH, addressTxAmount.getBlockHash())
                            .set(ADDRESS_TX_AMOUNT.BLOCK, addressTxAmount.getBlockNumber())
                            .set(ADDRESS_TX_AMOUNT.BLOCK_TIME, addressTxAmount.getBlockTime())
                            .set(ADDRESS_TX_AMOUNT.EPOCH, addressTxAmount.getEpoch())
                            .set(ADDRESS_TX_AMOUNT.UPDATE_DATETIME, localDateTime)
                            .execute();
                }
            });
         **/

    }

    @Override
    public int deleteAddressBalanceBySlotGreaterThan(Long slot) {
        return addressTxAmountRepository.deleteAddressBalanceBySlotGreaterThan(slot);
    }
}
