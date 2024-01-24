package org.cardanofoundation.ledgersync.repository.custom;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.consumercommon.entity.Address;
import org.cardanofoundation.ledgersync.consumercommon.entity.AddressTokenBalance;
import org.cardanofoundation.ledgersync.consumercommon.entity.MultiAsset;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static org.cardanofoundation.ledgersync.jooq.Tables.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
// TODO: add integration tests
public class CustomAddressTokenBalanceRepository {
    private final DSLContext dsl;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<AddressTokenBalance> findAllByAddressFingerprintPairIn(Collection<Pair<String, String>> addressFingerprintPairs) {

        Condition condition = null;
        for (Pair<String, String> addressFingerprintPair : addressFingerprintPairs) {
            String address = addressFingerprintPair.getFirst();
            String fingerprint = addressFingerprintPair.getSecond();
            Condition pairCondition = (ADDRESS.ADDRESS_.eq(address))
                    .and(MULTI_ASSET.FINGERPRINT.eq(fingerprint));
            condition = condition == null ? pairCondition : condition.or(pairCondition);
        }

        var query = dsl.select()
                .from(ADDRESS_TOKEN_BALANCE)
                .join(ADDRESS).on(ADDRESS_TOKEN_BALANCE.ADDRESS_ID.eq(ADDRESS.ID))
                .join(MULTI_ASSET).on(ADDRESS_TOKEN_BALANCE.IDENT.eq(MULTI_ASSET.ID))
                .where(condition);

        return query.fetch().map(this::mapToAddressTokenBalance);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<AddressTokenBalance> findAllByAddressMultiAssetIdPairIn(Collection<Pair<Long, Long>> addressMultiAssetIdPairs) {
        Condition condition = null;
        for (Pair<Long, Long> addressMultiAssetIdPair : addressMultiAssetIdPairs) {
            Long addressId = addressMultiAssetIdPair.getFirst();
            Long multiAssetId = addressMultiAssetIdPair.getSecond();
            Condition pairCondition = ADDRESS_TOKEN_BALANCE.ADDRESS_ID.eq(addressId)
                    .and(ADDRESS_TOKEN_BALANCE.IDENT.eq(multiAssetId));
            condition = condition == null ? pairCondition : condition.or(pairCondition);
        }

        var query = dsl.select()
                .from(ADDRESS_TOKEN_BALANCE)
                .join(MULTI_ASSET).on(ADDRESS_TOKEN_BALANCE.IDENT.eq(MULTI_ASSET.ID))
                .join(ADDRESS).on(ADDRESS_TOKEN_BALANCE.ADDRESS_ID.eq(ADDRESS.ID))
                .where(condition);

        return query.fetch().map(this::mapToAddressTokenBalance);
    }

    private AddressTokenBalance mapToAddressTokenBalance(Record record) {
        AddressTokenBalance addressTokenBalance = new AddressTokenBalance();

        addressTokenBalance.setAddress(
                Address.builder().id(record.get(ADDRESS.ID))
                        .stakeAddressId(record.get(ADDRESS.STAKE_ADDRESS_ID))
                        .balance(record.get(ADDRESS.BALANCE))
                        .address(record.get(ADDRESS.ADDRESS_))
                        .addressHasScript(record.get(ADDRESS.ADDRESS_HAS_SCRIPT))
                        .txCount(record.get(ADDRESS.TX_COUNT))
                        .verifiedContract(record.get(ADDRESS.VERIFIED_CONTRACT))
                        .build());

        addressTokenBalance.setMultiAsset(MultiAsset.builder()
                .id(record.get(MULTI_ASSET.ID))
                .policy(record.get(MULTI_ASSET.POLICY))
                .name(record.get(MULTI_ASSET.NAME_VIEW))
                .nameView(record.get(MULTI_ASSET.NAME_VIEW))
                .fingerprint(record.get(MULTI_ASSET.FINGERPRINT))
                .txCount(record.get(MULTI_ASSET.TX_COUNT))
                .supply(record.get(MULTI_ASSET.SUPPLY))
                .totalVolume(record.get(MULTI_ASSET.TOTAL_VOLUME))
                .time(Timestamp.valueOf(record.get(MULTI_ASSET.TIME)))
                .build());

        addressTokenBalance.setStakeAddressId(record.get(ADDRESS_TOKEN_BALANCE.STAKE_ADDRESS_ID));
        addressTokenBalance.setAddressId(record.get(ADDRESS_TOKEN_BALANCE.ADDRESS_ID));
        addressTokenBalance.setBalance(record.get(ADDRESS_TOKEN_BALANCE.BALANCE));
        addressTokenBalance.setMultiAssetId(record.get(ADDRESS_TOKEN_BALANCE.IDENT));
        addressTokenBalance.setId(record.get(ADDRESS_TOKEN_BALANCE.ID));

        return addressTokenBalance;
    }
}
