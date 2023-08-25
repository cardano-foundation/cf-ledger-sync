package org.cardanofoundation.ledgersync.common.common.byron;

public interface ByronHead<T, S, U> {

  long getProtocolMagic();

  String getBlockHash();

  String getPrevBlock();

  T getBodyProof();

  S getConsensusData();

  U getExtraData();
}
