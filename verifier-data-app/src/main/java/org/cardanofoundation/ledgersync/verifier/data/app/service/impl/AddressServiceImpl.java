package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AddressServiceImpl implements AddressService {
    JdbcTemplate jdbcTemplate;

    @Override
    public Set<String> getRandomAddresses(int maxNumOfAddresses) {
        Long totalAddresses = jdbcTemplate.queryForObject("select count(*) from address", Long.class);
        if (Objects.isNull(totalAddresses)) {
            return new HashSet<>();
        }
        int limit = maxNumOfAddresses * 5;
        long offset = new Random().nextLong(1, totalAddresses - limit);
        String query = "select address from address offset ? limit ?";
        return jdbcTemplate.query(query, new Object[]{offset, limit},
                (rs, _rowNum) -> rs.getString("address")).stream().filter(address -> address.startsWith("addr_")).limit(maxNumOfAddresses).collect(Collectors.toSet());
    }
}
