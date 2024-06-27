package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AddressBalanceComparisonMapperLS;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AddressBalanceProjection;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressBalanceService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AddressBalanceServiceImpl implements AddressBalanceService {
    JdbcTemplate jdbcTemplate;

    @Override
    public Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses) {
        if(addresses == null || addresses.isEmpty()) {
            return Collections.emptyMap();
        }
        String inSql = String.join(",", Collections.nCopies(addresses.size(), "?"));
        String query = String.format("SELECT ab.address, ab.unit, ab.quantity from address_balance ab where ab.unit = 'lovelace' and ab.address in (%s)", inSql);
        List<AddressBalanceProjection> addressBalancePojoLSList = jdbcTemplate.query(query,
                addresses.toArray(),
                (rs, _rowNum) -> {
                    String address = rs.getString("address");
                    String unit = rs.getString("unit");
                    BigInteger balance = new BigInteger(rs.getString("quantity"));
                    return new AddressBalanceProjection() {
                        @Override
                        public String getAddress() {
                            return address;
                        }

                        @Override
                        public String getUnit() {
                            return unit;
                        }

                        @Override
                        public BigInteger getBalance() {
                            return balance;
                        }
                    };
                }
        );
        return new AddressBalanceComparisonMapperLS().buildMap(addressBalancePojoLSList);
    }
}
