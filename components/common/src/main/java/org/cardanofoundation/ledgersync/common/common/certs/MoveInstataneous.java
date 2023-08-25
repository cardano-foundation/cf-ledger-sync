package org.cardanofoundation.ledgersync.common.common.certs;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cardanofoundation.ledgersync.common.common.mapper.StakeCredentialDeserializer;
import java.math.BigInteger;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class MoveInstataneous extends Certificate {

  private static final CertType type = CertType.MOVE_INSTATANEOUS;

  @Override
  public CertType getCertType() {
    return type;
  }

  //determines where the funds are drawn from
  private boolean reserves;
  private boolean treasury;
  private BigInteger accountingPotCoin; //the funds are given to the other accounting pot
  @JsonDeserialize(keyUsing = StakeCredentialDeserializer.class)
  private Map<StakeCredential, BigInteger> stakeCredentialCoinMap; //funds are moved to stake credentials
}
