package org.cardanofoundation.ledgersync.verifier.data.app.service;

import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AdaPotsComparisonMapperImpl;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AdaPotsProjection;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AdaPotsProjectionRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public abstract class AdaPotsService {
  private final JdbcTemplate jdbcTemplate;

  public AdaPotsService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Long> getRandomEpochNos(int maxAdaPots) {
    List<Long> epochNos =
        jdbcTemplate.query(
            "SELECT epoch_no FROM ada_pots ORDER BY RANDOM() LIMIT ?",
            new Object[] {maxAdaPots},
            (rs, rowNum) -> rs.getLong("epoch_no"));
    return epochNos;
  }

  public List<AdaPotsProjection> getAdaPotsByEpochNos(List<Long> epochNos) {
    Map<String, Object> params = Collections.singletonMap("epochNos", epochNos);
    AdaPotsProjectionRowMapper rowMapper = new AdaPotsProjectionRowMapper();
    return new NamedParameterJdbcTemplate(jdbcTemplate)
        .query(
            "SELECT slot_no, epoch_no, treasury, reserves, rewards FROM ada_pots WHERE epoch_no IN (:epochNos)",
            params,
            (rs, rowNum) -> rowMapper.mapRow(rs, rowNum));
  }

  public Map<AdaPotsComparisonKey, AdaPotsComparison> getMapAdaPotsFromEpochNos(List<Long> epochNos)
      throws SQLException {
    List<AdaPotsProjection> adaPotsProjections = getAdaPotsByEpochNos(epochNos);
    return new AdaPotsComparisonMapperImpl().buildMap(adaPotsProjections);
  }
}
