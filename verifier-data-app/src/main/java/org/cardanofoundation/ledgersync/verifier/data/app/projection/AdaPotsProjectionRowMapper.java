package org.cardanofoundation.ledgersync.verifier.data.app.projection;

import org.springframework.jdbc.core.RowMapper;

import jakarta.validation.constraints.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdaPotsProjectionRowMapper implements RowMapper<AdaPotsProjection> {
  @Override
  public AdaPotsProjection mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
    AdaPotsProjection adaPotsProjection = new AdaPotsProjection();
    adaPotsProjection.setSlotNo(rs.getLong("slot_no"));
    adaPotsProjection.setEpochNo(rs.getLong("epoch_no"));
    adaPotsProjection.setTreasury(rs.getBigDecimal("treasury").toBigInteger());
    adaPotsProjection.setReserves(rs.getBigDecimal("reserves").toBigInteger());
    adaPotsProjection.setRewards(rs.getBigDecimal("rewards").toBigInteger());

    return adaPotsProjection;
  }
}
