package org.cardanofoundation.ledgersync.common.common.cost.mdl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.bloxbean.cardano.client.plutus.spec.Language;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CostModels {
  private String hash;
  private Map<Language, List<BigInteger>>  languages;
}
