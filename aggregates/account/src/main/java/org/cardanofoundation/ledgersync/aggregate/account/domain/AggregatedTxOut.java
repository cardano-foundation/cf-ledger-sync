package org.cardanofoundation.ledgersync.aggregate.account.domain;

import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.Datum;
import com.bloxbean.cardano.yaci.core.model.TransactionOutput;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxOut;
import com.bloxbean.cardano.yaci.core.util.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;

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

    @SneakyThrows
    public static AggregatedTxOut from(TransactionOutput transactionOutput) {
        if (Objects.isNull(transactionOutput)) {
            return null;
        }

        BigInteger nativeAmount = calculateOutSum(List.of(transactionOutput));
        Datum inlineDatum = null;
        if (transactionOutput.getInlineDatum() != null) {
            var inlineDatumDI = CborSerializationUtil.deserialize(HexUtil.decodeHexString(transactionOutput.getInlineDatum()));
            inlineDatum = Datum.from(inlineDatumDI);
        }

        return AggregatedTxOut.builder()
                .address(AggregatedAddress.from(transactionOutput.getAddress()))
                .nativeAmount(nativeAmount)
                .amounts(transactionOutput.getAmounts())
                .datumHash(transactionOutput.getDatumHash())
                .inlineDatum(inlineDatum) //TODO refactor convert to json
                .scriptRef(transactionOutput.getScriptRef())
                .build();
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
