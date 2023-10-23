package org.cardanofoundation.ledgersync.explorerconsumer.service;


import com.bloxbean.cardano.client.api.util.AssetUtil;
import com.bloxbean.cardano.yaci.core.util.HexUtil;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedAddress;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.util.LedgerSyncAssetUtil;

import java.math.BigInteger;
import java.util.List;

import static com.bloxbean.cardano.yaci.core.util.Constants.LOVELACE;

@RequiredArgsConstructor
public abstract class BlockAggregatorService<T> // NOSONAR
        implements SyncServiceInstance<T> {


    protected final SlotLeaderService slotLeaderService;
    protected final BlockDataService blockDataService;

    /**
     * Convert CDDL block data to aggregated block data
     *
     * @param block CDDL block data
     * @return aggregated block object
     */
    public abstract AggregatedBlock aggregateBlock(T block);

    /**
     * This method iterates between all aggregated tx out and map it to
     * aggregated address balance data
     *
     * @param aggregatedTxOuts all aggregated tx outs within a tx
     * @param txHash           tx hash of tx where the aggregated tx outs associate with
     */
    public void mapAggregatedTxOutsToAddressBalanceMap(
            List<AggregatedTxOut> aggregatedTxOuts, String txHash) {
        // Iterate between all aggregated tx out
        aggregatedTxOuts.forEach(aggregatedTxOut -> {
            // Get aggregated block address
            AggregatedAddress aggregatedAddress = aggregatedTxOut.getAddress();

            // Get address string (Base58 or Bech32) from aggregated address
            String address = aggregatedAddress.getAddress();

            // Get address's native amount
            BigInteger nativeAmount = aggregatedTxOut.getNativeAmount();

            // Get aggregated address balance data
            AggregatedAddressBalance aggregatedAddressBalance =
                    blockDataService.getAggregatedAddressBalanceFromAddress(address);

            /*
             * Because the native amount in process is output, it is added to
             * existing balance record
             */
            aggregatedAddressBalance.addNativeBalance(txHash, nativeAmount);

            // Add multi-asset balances
            aggregatedTxOut.getAmounts().stream()
                    .filter(amount -> !LOVELACE.equals(amount.getAssetName()))
                    .forEach(amount -> {
                        //byte[] assetName = amount.getAssetName();
                        String assetName = amount.getAssetName();
                        String policyId = amount.getPolicyId();
                        String assetNameAsHex = HexUtil.encodeHexString(LedgerSyncAssetUtil.assetNameToBytes(assetName), true);
                        String fingerprint = AssetUtil.calculateFingerPrint(policyId, assetNameAsHex);
                        BigInteger quantity = amount.getQuantity();
                        aggregatedAddressBalance.addAssetBalance(txHash, fingerprint, quantity);
                    });
        });
    }
}
