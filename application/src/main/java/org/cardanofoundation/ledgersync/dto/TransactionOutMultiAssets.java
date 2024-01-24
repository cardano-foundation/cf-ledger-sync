package org.cardanofoundation.ledgersync.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.consumercommon.entity.MaTxOut;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.springframework.util.MultiValueMap;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@AllArgsConstructor
public class TransactionOutMultiAssets {
    TxOut txOut;
    MultiValueMap<String, MaTxOut> pMaTxOuts;
    String scriptRefer;
    String datumHash;
}
