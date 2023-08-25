package org.cardanofoundation.ledgersync.explorerconsumer.service;

import org.cardanofoundation.explorer.consumercommon.entity.MaTxOut;
import org.cardanofoundation.explorer.consumercommon.entity.MultiAsset;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MultiAssetService {

    /**
     * Handle all asset mints within a collection of success txs
     *
     * @param successTxs success txs collection
     * @param txMap      a map with key is tx hash and value is the respective tx entity
     */
    void handleMultiAssetMint(Collection<AggregatedTx> successTxs, Map<String, Tx> txMap);

    /**
     * Update multi asset tx outs' idents (ident is the associated multi asset entity)
     * The returning MaTxOut collection may have lesser records than all original lists
     * combined, because some MaTxOuts do not have their assets minted before
     *
     * @param maTxOuts a multivalued map with key is asset fingerprints, and value is a
     *                 list of associated asset tx outputs
     * @return collection of asset tx outputs with updated idents
     */
    Collection<MaTxOut> updateIdentMaTxOuts(MultiValueMap<String, MaTxOut> maTxOuts);

    /**
     * Find all multi asset records by fingerprints
     *
     * @param fingerprints multi asset fingerprints
     * @return a collection of multi assets found
     */
    Collection<MultiAsset> findMultiAssetsByFingerprintIn(Set<String> fingerprints);

    /**
     * Build multivalued map of asset outputs
     *
     * @param txOutput aggregated tx out containing all asset outputs
     * @param txOut    target tx out entity associates with the asset outputs
     * @return a multivalued map with key is asset fingerprints, and value is a
     * list of associated asset tx outputs
     */
    MultiValueMap<String, MaTxOut> buildMaTxOut(AggregatedTxOut txOutput, TxOut txOut);

    /**
     * Find all multi asset tx outs by their associated tx outs
     *
     * @param txOuts tx outs to find multi asset tx outs
     * @return a collection of multi asset tx outs found
     */
    Collection<MaTxOut> findAllByTxOutIn(Collection<TxOut> txOuts);

    /**
     * Rollback multi assets supply stats or delete them if supply is zero
     *
     * @param txs collection of txs being rolled back
     */
    void rollbackMultiAssets(Collection<Tx> txs);
}
