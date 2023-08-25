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
public class StakeRegistration extends Certificate {

  private static final CertType type  = CertType.STAKE_REGISTRATION;
  private StakeCredential stakeCredential;

  @Override
  public CertType getCertType() {
    return type;
  }
}
