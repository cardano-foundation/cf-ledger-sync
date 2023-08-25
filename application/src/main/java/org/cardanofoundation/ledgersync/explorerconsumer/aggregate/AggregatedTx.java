package org.cardanofoundation.ledgersync.explorerconsumer.aggregate;

import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.Update;
import com.bloxbean.cardano.yaci.core.model.Witnesses;
import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AggregatedTx {

    String hash;
    String blockHash;
    long blockIndex;
    BigInteger outSum;
    BigInteger fee;
    boolean validContract;
    long deposit;
    Set<AggregatedTxIn> txInputs;
    Set<AggregatedTxIn> collateralInputs;
    Set<AggregatedTxIn> referenceInputs;
    List<AggregatedTxOut> txOutputs;
    AggregatedTxOut collateralReturn;
    List<Certificate> certificates;
    Map<String, BigInteger> withdrawals;
    Update update;
    List<Amount> mint;
    Set<String> requiredSigners;
    Witnesses witnesses;
    String auxiliaryDataHash;

    public void setFee(BigInteger fee) {
        this.fee = fee;
    }

    public void setAuxiliaryDataHash(String auxiliaryDataHash) {
        this.auxiliaryDataHash = auxiliaryDataHash;
    }
}
