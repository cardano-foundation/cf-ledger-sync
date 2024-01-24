package org.cardanofoundation.ledgersync.aggregate;

import com.bloxbean.cardano.yaci.core.model.TransactionInput;
import com.bloxbean.cardano.yaci.core.model.byron.ByronTxIn;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

//import org.cardanofoundation.ledgersync.common.common.TransactionInput;
//import org.cardanofoundation.ledgersync.common.common.byron.ByronTxIn;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AggregatedTxIn {

    int index;
    String txId;

    @Setter
    Integer redeemerPointerIdx;

    public static AggregatedTxIn of(TransactionInput transactionInput) {
        if (Objects.isNull(transactionInput)) {
            return null;
        }

        return new AggregatedTxIn(
                transactionInput.getIndex(),
                transactionInput.getTransactionId(),
                null);
    }

    public static AggregatedTxIn of(ByronTxIn byronTxIn) {
        if (Objects.isNull(byronTxIn)) {
            return null;
        }

        return new AggregatedTxIn(byronTxIn.getIndex(), byronTxIn.getTxId(), null);
    }
}
