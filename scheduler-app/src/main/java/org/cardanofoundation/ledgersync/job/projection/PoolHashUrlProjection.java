package org.cardanofoundation.ledgersync.job.projection;

public interface PoolHashUrlProjection {
  Long getPoolId();

  String getUrl();

  Long getMetadataId();
}
