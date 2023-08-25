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
public class GenesisKeyDelegation extends Certificate {

  private static final CertType type = CertType.GENESIS_KEY_DELEGATION;

  private String genesisHash;
  private String genesisDelegateHash;
  private String vrfKeyHash;

  @Override
  public CertType getCertType() {
    return type;
  }
}