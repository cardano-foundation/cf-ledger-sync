package org.cardanofoundation.ledgersync.explorerconsumer.repository.custom;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.Address;
import org.cardanofoundation.explorer.consumercommon.entity.AddressTokenBalance;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;


@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAddressTokenBalanceRepository {
    private final DSLContext dsl;
    private String schema;

    public CustomAddressTokenBalanceRepository(DSLContext dsl,
                                               @Value("${spring.jpa.properties.hibernate.default_schema}") String schema) {
        this.dsl = dsl;
        this.schema = schema;
    }

    public List<AddressTokenBalance> findAllByAddressFingerprintPairIn(Collection<Pair<String, String>> addressFingerprintPairs) {
        Table<?> addressTokenBalanceTable = DSL.table(schema + ".address_token_balance");
        Table<?> addressTable = DSL.table(schema + ".address");
        Table<?> multiAssetTable = DSL.table(schema + ".multi_asset");

        Condition condition = null;
        for (Pair<String, String> addressFingerprintPair : addressFingerprintPairs) {
            String address = addressFingerprintPair.getFirst();
            String fingerprint = addressFingerprintPair.getSecond();
            Condition pairCondition = (DSL.field("address.address").eq(address))
                    .and(DSL.field("multi_asset.fingerprint").eq(fingerprint));
            condition = condition == null ? pairCondition : condition.or(pairCondition);
        }

        Result<Record> result = dsl.select()
                .from(addressTokenBalanceTable)
                .join(addressTable).on(DSL.field("address_token_balance.address_id").eq(DSL.field("address.id")))
                .join(multiAssetTable).on(DSL.field("address_token_balance.ident").eq(DSL.field("multi_asset.id")))
                .where(condition)
                .fetch();

        return getAddressTokenBalances(result);
    }


    public List<AddressTokenBalance> findAllByAddressMultiAssetIdPairIn(Collection<Pair<Long, Long>> addressMultiAssetIdPairs) {
        Table<?> addressTokenBalanceTable = DSL.table(schema + ".address_token_balance");
        Table<?> addressTable = DSL.table(schema + ".address");
        Table<?> multiAssetTable = DSL.table(schema + ".multi_asset");

        Condition condition = null;
        for (Pair<Long, Long> addressMultiAssetIdPair : addressMultiAssetIdPairs) {
            Long addressId = addressMultiAssetIdPair.getFirst();
            Long multiAssetId = addressMultiAssetIdPair.getSecond();
            Condition pairCondition = DSL.field("address_token_balance.address_id").eq(addressId)
                    .and(DSL.field("address_token_balance.ident").eq(multiAssetId));
            condition = condition == null ? pairCondition : condition.or(pairCondition);
        }

        Result<Record> result = dsl.select()
                .from(addressTokenBalanceTable)
                .join(multiAssetTable).on(DSL.field("address_token_balance.ident").eq(DSL.field("multi_asset.id")))
                .join(addressTable).on(DSL.field("address_token_balance.address_id").eq(DSL.field("address.id")))
                .where(condition)
                .fetch();

        return getAddressTokenBalances(result);
    }

    private List<AddressTokenBalance> getAddressTokenBalances(Result<Record> result) {
        return result.map(record -> {

            AddressTokenBalance atb = new AddressTokenBalance();

            atb.setId(record.getValue(DSL.field(DSL.name("address_token_balance", "id")), Long.class));
            atb.setMultiAssetId(record.getValue(DSL.field(DSL.name("address_token_balance", "ident"), Long.class)));
            atb.setBalance(record.getValue(DSL.field(DSL.name("address_token_balance", "balance"), BigInteger.class)));
            atb.setAddressId(record.getValue(DSL.field(DSL.name("address_token_balance", "address_id"), Long.class)));
            return atb;
        });
    }
}
