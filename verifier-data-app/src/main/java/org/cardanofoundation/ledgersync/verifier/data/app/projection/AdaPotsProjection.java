package org.cardanofoundation.ledgersync.verifier.data.app.projection;

import lombok.*;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AdaPotsProjection {
  Long slotNo;
  Long epochNo;
  BigInteger treasury;
  BigInteger reserves;
  BigInteger rewards;
}
