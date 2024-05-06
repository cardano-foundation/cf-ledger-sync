package org.cardanofoundation.ledgersync.verifier.data.app.projection;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.jetbrains.annotations.NotNull;

public class AdaPotsProjectionRowMapper implements RowMapper<AdaPotsProjection> {
  @Override
  public AdaPotsProjection mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
    Long slotNo = rs.getLong("slot_no");
    Long epochNo = rs.getLong("epoch_no");
    BigInteger treasury = rs.getBigDecimal("treasury").toBigInteger();
    BigInteger reserves = rs.getBigDecimal("reserves").toBigInteger();
    BigInteger rewards = rs.getBigDecimal("rewards").toBigInteger();

    return new AdaPotsProjection() {
      @Override
      public Long getSlotNo() {
        return slotNo;
      }

      @Override
      public Long getEpochNo() {
        return epochNo;
      }

      @Override
      public BigInteger getTreasury() {
        return treasury;
      }

      @Override
      public BigInteger getReserves() {
        return reserves;
      }

      @Override
      public BigInteger getRewards() {
        return rewards;
      }
    };
  }
}
