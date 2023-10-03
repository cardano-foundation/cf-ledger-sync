package org.cardanofoundation.ledgersync.common.common.byron;

import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;

public abstract class ByronBlock extends CommonBlock {

  abstract <T extends ByronHead> T getHeader(); //No Sonar

  abstract <T> T getBody();
}
