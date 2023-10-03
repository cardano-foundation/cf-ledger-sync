package org.cardanofoundation.ledgersync.common.common.byron;

import java.util.List;
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
public class ByronSecretProof {
  private String extraGen;
  private String proof;
  private String parallelProofs;
  private List<String> commitments;
}
