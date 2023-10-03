package org.cardanofoundation.ledgersync.common.common.kafka;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.cardanofoundation.ledgersync.common.common.Block;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.common.common.byron.ByronEbBlock;
import org.cardanofoundation.ledgersync.common.common.byron.ByronMainBlock;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = CommonBlock.TYPE)
@JsonSubTypes({@JsonSubTypes.Type(value = Block.class, name = Block.TYPE),
    @JsonSubTypes.Type(value = ByronEbBlock.class, name = ByronEbBlock.TYPE),
    @JsonSubTypes.Type(value = ByronMainBlock.class, name = ByronMainBlock.TYPE),})
public abstract class CommonBlock {

  public static final String TYPE = "type";

  protected int cborSize;
  private Era eraType;
  private long blockTime;
  private int network;
  private boolean rollback;

  @JsonIgnore
  public abstract String getType();

  @JsonIgnore
  public abstract String getBlockHash();

  @JsonIgnore
  public abstract long getSlot();

  @JsonIgnore
  public abstract long getBlockNumber();

  @JsonIgnore
  public abstract String getPreviousHash();

  @JsonIgnore
  public abstract long getEpoch();
}
