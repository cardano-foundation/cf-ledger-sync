package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronSignedCommitment {
  private String publicKey;
  private ByronCommitment commitment;
  private String signature;
}
