package org.cardanofoundation.ledgersync.explorerconsumer.aggregate;

//import org.cardanofoundation.ledgersync.common.common.Amount;
//import org.cardanofoundation.ledgersync.common.common.Datum;
//import org.cardanofoundation.ledgersync.common.common.TransactionOutput;
//import org.cardanofoundation.ledgersync.common.common.byron.ByronTxOut;
//import org.cardanofoundation.ledgersync.common.common.constant.Constant;

import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.Datum;
import com.bloxbean.cardano.yaci.core.model.TransactionOutput;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxOut;
import com.bloxbean.cardano.yaci.core.util.Constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AggregatedTxOut {
    Integer index;
    AggregatedAddress address;
    BigInteger nativeAmount;
    List<Amount> amounts;
    String datumHash;
    Datum inlineDatum;
    String scriptRef;

    public static AggregatedTxOut from(TransactionOutput transactionOutput) {
        if (Objects.isNull(transactionOutput)) {
            return null;
        }

        BigInteger nativeAmount = calculateOutSum(List.of(transactionOutput));
        Datum inlineDatum = null;
        if (transactionOutput.getInlineDatum() != null) {
            inlineDatum = new Datum(transactionOutput.getInlineDatum(), ""); //TODO refactor convert to json
        }

        return AggregatedTxOut.builder()
                .address(AggregatedAddress.from(transactionOutput.getAddress()))
                .nativeAmount(nativeAmount)
                .amounts(transactionOutput.getAmounts())
                .datumHash(transactionOutput.getDatumHash())
                .inlineDatum(inlineDatum) //TODO refactor convert to json
                .scriptRef(transactionOutput.getScriptRef())
                .build();

//    return new AggregatedTxOut(
//        transactionOutput.getIndex(),
//        AggregatedAddress.from(transactionOutput.getAddress()),
//        nativeAmount,
//        transactionOutput.getAmounts(),
//        transactionOutput.getDatumHash(),
//        transactionOutput.getInlineDatum(),
//        transactionOutput.getScriptRef()
//    );
    }

    public static AggregatedTxOut from(ByronTxOut byronTxOut, int idx) {
        if (Objects.isNull(byronTxOut)) {
            return null;
        }

        return new AggregatedTxOut(
                idx,
                AggregatedAddress.from(byronTxOut.getAddress().getBase58Raw()),
                byronTxOut.getAmount(),
                Collections.emptyList(),
                null,
                null,
                null
        );
    }

    public static BigInteger calculateOutSum(List<TransactionOutput> txOuts) {
        var outSum = txOuts.stream()
                .flatMap(transactionOutput -> transactionOutput.getAmounts().stream())
                // .filter(amount -> Constant.isLoveLace(amount.getAssetName())) //TODO -- Sotatek is not checking policy id
                .filter(amount -> Constants.LOVELACE.equals(amount.getAssetName())
                        && (amount.getPolicyId() == null || amount.getPolicyId().isEmpty()))
                .map(Amount::getQuantity)
                .reduce(BigInteger.ZERO, BigInteger::add);

        return outSum;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
