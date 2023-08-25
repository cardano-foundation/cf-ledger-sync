package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.Datum;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;

import java.util.Collection;
import java.util.Map;

public interface DatumService {

    /**
     * Handle raw CDDL "datum" data relate to.
     * <br>
     * A piece of information that can be associated with a UTXO and is used to carry the script state such as its owner or the timing details of when the UTXO can be spent.
     *
     * @param aggregatedTxs aggregated tx batch
     * @param txMap         transaction entity map
     * @return datum map key is hash, value datum
     * @see <a href="https://cips.cardano.org/cips/cip32/">[CIP32]</a>
     */
    Map<String, Datum> handleDatum(Collection<AggregatedTx> aggregatedTxs, Map<String, Tx> txMap);
}
