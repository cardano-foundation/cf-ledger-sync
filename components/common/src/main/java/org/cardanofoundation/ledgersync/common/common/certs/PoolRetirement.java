package org.cardanofoundation.ledgersync.common.common.certs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PoolRetirement extends Certificate {

  private static final CertType type = CertType.POOL_RETIREMENT;

  private String poolKeyHash;
  private long epoch;

  @Override
  public CertType getCertType() {
    return type;
  }
}
