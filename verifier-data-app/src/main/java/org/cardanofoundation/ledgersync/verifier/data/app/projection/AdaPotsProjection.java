package org.cardanofoundation.ledgersync.verifier.data.app.projection;

import java.math.BigInteger;
import java.sql.SQLException;

public interface AdaPotsProjection {
  Long getSlotNo() throws SQLException;

  Long getEpochNo() throws SQLException;

  BigInteger getTreasury() throws SQLException;

  BigInteger getReserves() throws SQLException;

  BigInteger getRewards() throws SQLException;
}
