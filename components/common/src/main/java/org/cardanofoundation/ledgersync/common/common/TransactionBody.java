package org.cardanofoundation.ledgersync.common.common;

import org.cardanofoundation.ledgersync.common.common.certs.Certificate;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class TransactionBody {

  //Derived
  private String txHash;

  private Set<TransactionInput> inputs;
  private List<TransactionOutput> outputs;
  private BigInteger fee;
  private BigInteger ttl;

  @Builder.Default
  private List<Certificate> certificates = new ArrayList<>();
  private Map<String, BigInteger> withdrawals;
  private Update update;
  private String auxiliaryDataHash;
  private long validityIntervalStart;

  @Builder.Default
  private List<Amount> mint = new ArrayList<>();
  private String scriptDataHash;

  private Set<TransactionInput> collateralInputs;
  private Set<String> requiredSigners;

  private int networkId;
  private TransactionOutput collateralReturn;
  private BigInteger totalCollateral;
  private Set<TransactionInput> referenceInputs;

  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }
}
