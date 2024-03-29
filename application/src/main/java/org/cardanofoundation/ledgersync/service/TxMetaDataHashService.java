package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.TxMetadataHash;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.Map;

public interface TxMetaDataHashService {

    /**
     * Insert transactions metadata hash or auxiliary data hash for transactions
     *
     * @param aggregatedTxes transaction have been aggregated contain success and fail transaction
     * @return Map of Tx metadata hash with key are transaction hash and value are metadata hash
     */
    Map<String, TxMetadataHash> handleMetaDataHash(Collection<AggregatedTx> aggregatedTxes);
}
