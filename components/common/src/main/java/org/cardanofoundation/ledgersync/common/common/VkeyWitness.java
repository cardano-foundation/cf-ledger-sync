package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class VkeyWitness {

  private String key;
  private String signature;
}
