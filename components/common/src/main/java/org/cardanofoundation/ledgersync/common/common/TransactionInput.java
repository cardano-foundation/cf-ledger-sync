package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TransactionInput {

  private String transactionId;
  private int index;
}
