package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class TransactionOutput {

  private Integer index;
  private String address;
  private List<Amount> amounts;
  private String datumHash;
  private Datum inlineDatum;
  private String scriptRef;

}
