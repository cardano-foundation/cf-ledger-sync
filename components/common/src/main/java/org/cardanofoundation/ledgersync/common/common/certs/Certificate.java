package org.cardanofoundation.ledgersync.common.common.certs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = CommonBlock.TYPE)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GenesisKeyDelegation.class, name = "GENESIS_KEY_DELEGATION"),
    @JsonSubTypes.Type(value = MoveInstataneous.class, name = "MOVE_INSTATENEOUS_REWARDS_CERT"),
    @JsonSubTypes.Type(value = PoolRegistration.class, name = "POOL_REGISTRATION"),
    @JsonSubTypes.Type(value = PoolRetirement.class, name = "POOL_RETIREMENT"),
    @JsonSubTypes.Type(value = StakeDelegation.class, name = "STAKE_DELEGATION"),
    @JsonSubTypes.Type(value = StakeDeregistration.class, name = "STAKE_DEREGISTRATION"),
    @JsonSubTypes.Type(value = StakeRegistration.class, name = "STAKE_REGISTRATION")
})
@Getter
@Setter
public abstract class Certificate {
  protected int index = 0;
  public abstract CertType getCertType();
}
