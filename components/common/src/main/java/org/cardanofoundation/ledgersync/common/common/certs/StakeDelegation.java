package org.cardanofoundation.ledgersync.common.common.certs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class StakeDelegation extends Certificate {

  private static final CertType type = CertType.STAKE_DELEGATION;

  private StakeCredential stakeCredential;
  private StakePoolId stakePoolId;

  @Override
  public CertType getCertType() {
    return type;
  }
}
