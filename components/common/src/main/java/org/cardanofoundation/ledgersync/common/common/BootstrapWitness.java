package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BootstrapWitness {

  private String publicKey;
  private String signature;
  private String chainCode;
  private String attributes;
}
