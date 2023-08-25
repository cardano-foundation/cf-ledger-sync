package org.cardanofoundation.ledgersync.explorerconsumer.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.MaTxOut;
import org.cardanofoundation.explorer.consumercommon.entity.TxOut;
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
