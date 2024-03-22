package org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl;

import org.cardanofoundation.ledgersync.verifier.data.app.constant.Unit;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.AddressTxAmountComparisonMapper;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountKey;
import rest.koios.client.backend.api.base.common.Asset;
import rest.koios.client.backend.api.transactions.model.TxIO;
import rest.koios.client.backend.api.transactions.model.TxInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the AddressTxAmountComparisonMapper interface for Koios transactions.
 */
public class AddressTxAmountComparisonMapperKoios implements AddressTxAmountComparisonMapper<TxInfo> {

    /**
     * Builds a map of address transaction amounts by processing the given list of transactions.
     *
     * @param txInfoList The list of transactions to process.
     * @return A map of address transaction amounts.
     */
    @Override
    public Map<AddressTxAmountKey, AddressTxAmountComparison> buildMap(List<TxInfo> txInfoList) {
        List<AddressTxAmountComparison> addressTxAmountComparisonList = new ArrayList<>();
        txInfoList.forEach(txInfo -> {
            String txHash = txInfo.getTxHash();
            addressTxAmountComparisonList.addAll(handleTxIo(txInfo.getInputs(), txHash, true));
            addressTxAmountComparisonList.addAll(handleTxIo(txInfo.getOutputs(), txHash, false));
        });
        Map<AddressTxAmountKey, AddressTxAmountComparison> map = new HashMap<>();
        addressTxAmountComparisonList.forEach(addressTxAmountComparison -> {
            AddressTxAmountKey addressTxAmountKey = AddressTxAmountKey.builder()
                    .address(addressTxAmountComparison.getAddressTxAmountKey().getAddress())
                    .txHash(addressTxAmountComparison.getAddressTxAmountKey().getTxHash())
                    .unit(addressTxAmountComparison.getAddressTxAmountKey().getUnit())
                    .build();
            if (map.containsKey(addressTxAmountKey)) {
                AddressTxAmountComparison value = map.get(addressTxAmountKey);
                value.setQuantity(value.getQuantity().add(addressTxAmountComparison.getQuantity()));
                map.put(addressTxAmountKey, value);
            } else {
                map.put(addressTxAmountKey, addressTxAmountComparison);
            }
        });
        map.entrySet().removeIf(entry -> entry.getValue().getQuantity().equals(BigInteger.ZERO));
        return map;
    }

    /**
     * Handles transaction inputs or outputs and converts them into a list of address transaction amounts.
     *
     * @param txIoInputs   The list of transaction inputs or outputs to process.
     * @param txHash       The hash of the transaction.
     * @param isNegative   Flag indicating whether the transaction amount is negative.
     * @return A list of address transaction amounts.
     */
    private List<AddressTxAmountComparison> handleTxIo(List<TxIO> txIoInputs, String txHash, boolean isNegative) {
        List<AddressTxAmountComparison> addressTxAmountComparisonList = new ArrayList<>();
        txIoInputs.forEach(txIO -> {
            AddressTxAmountComparison addressTxAmountComparison = AddressTxAmountComparison.builder()
                    .addressTxAmountKey(AddressTxAmountKey.builder()
                            .address(txIO.getPaymentAddr().getBech32())
                            .txHash(txHash)
                            .unit(Unit.LOVELACE)
                            .build())
                    .quantity(new BigInteger(isNegative ? ("-" + txIO.getValue()) : txIO.getValue()))
                    .build();
            addressTxAmountComparisonList.add(addressTxAmountComparison);
            addressTxAmountComparisonList.addAll(handleAssertList(txIO.getAssetList(), addressTxAmountComparison));
        });
        return addressTxAmountComparisonList;
    }

    /**
     * Handles asset lists and converts them into a list of address transaction amounts.
     *
     * @param assetList The list of assets to process.
     * @param parent    The parent address transaction amount.
     * @return A list of address transaction amounts.
     */
    private List<AddressTxAmountComparison> handleAssertList(List<Asset> assetList, AddressTxAmountComparison parent) {
        List<AddressTxAmountComparison> addressTxAmountComparisonList = new ArrayList<>();
        assetList.forEach(asset -> {
            AddressTxAmountComparison addressTxAmountComparison = AddressTxAmountComparison.builder()
                    .addressTxAmountKey(AddressTxAmountKey.builder()
                            .address(parent.getAddressTxAmountKey().getAddress())
                            .txHash(parent.getAddressTxAmountKey().getTxHash())
                            .unit(asset.getPolicyId() + asset.getAssetName())
                            .build())
                    .quantity(new BigInteger(parent.getQuantity().signum() < 0 ?
                            "-" + asset.getQuantity() :
                            asset.getQuantity()))
                    .build();
            addressTxAmountComparisonList.add(addressTxAmountComparison);
        });
        return addressTxAmountComparisonList;
    }
}
