package org.cardanofoundation.ledgersync.scheduler.projection;

public interface PoolHashUrlProjection {
  Long getPoolId();

  String getUrl();

  Long getMetadataId();
}
