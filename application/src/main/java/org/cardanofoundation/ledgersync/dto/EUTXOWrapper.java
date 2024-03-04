package org.cardanofoundation.ledgersync.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.consumercommon.entity.MaTxOut;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EUTXOWrapper {

    Collection<TxOut> txOuts;
    Collection<MaTxOut> maTxOuts;

    public void addAll(EUTXOWrapper wrapper) {
        if (!CollectionUtils.isEmpty(wrapper.getTxOuts())) {
            txOuts.addAll(wrapper.getTxOuts());
        }

        if (!CollectionUtils.isEmpty(wrapper.getMaTxOuts())) {
            maTxOuts.addAll(wrapper.getMaTxOuts());
        }
    }
}
