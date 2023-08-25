package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.yaci.core.model.Amount;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.common.util.StringUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.MaTxMintProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.MultiAssetTotalVolumeProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.MultiAssetTxCountProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MaTxMintRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MultiAssetService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.ConsumerAssetUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bloxbean.cardano.yaci.core.util.Constants.LOVELACE;
import static org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant.BATCH_QUERY_SIZE;
import static org.cardanofoundation.ledgersync.explorerconsumer.util.ConsumerAssetUtil.assetNameToBytes;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class MultiAssetServiceImpl implements MultiAssetService {

    private static final long INITIAL_TX_COUNT = 0L;
    private static final String EMPTY_STRING = "";

    MaTxMintRepository maTxMintRepository;
    MultiAssetRepository multiAssetRepository;
    MultiAssetTxOutRepository multiAssetTxOutRepository;

    BlockDataService blockDataService;
    AggregatedDataCachingService aggregatedDataCachingService;

    @Override
    public void handleMultiAssetMint(Collection<AggregatedTx> successTxs, Map<String, Tx> txMap) {
        List<AggregatedTx> txWithMintAssetsList = successTxs.stream()
                .filter(aggregatedTx -> !CollectionUtils.isEmpty(aggregatedTx.getMint()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(txWithMintAssetsList)) {
            return;
        }

        // Get all asset fingerprints
        Set<String> fingerprints = txWithMintAssetsList.stream()
                .flatMap(aggregatedTx -> aggregatedTx.getMint().stream())
                .map(amount -> ConsumerAssetUtil.getFingerPrint(assetNameToBytes(amount.getAssetName()), amount.getPolicyId()))
                .collect(Collectors.toSet());

        /*
         * Get all existing minted assets, creating a map with key is a pair of asset's name
         * and policy id, and key is the associated asset entity
         */
        Map<Pair<String, String>, MultiAsset> mintAssetsExists =
                findMultiAssetsByFingerprintIn(fingerprints)
                        .stream()
                        .collect(Collectors.toMap(
                                multiAsset -> Pair.of(multiAsset.getName(), multiAsset.getPolicy()),
                                Function.identity()));

        List<MaTxMint> maTxMints = new ArrayList<>();

        // Iterate between all aggregated txs with mint assets
        txWithMintAssetsList.forEach(txWithMintAssets -> {
            Tx tx = txMap.get(txWithMintAssets.getHash());
            List<Amount> mintAssets = txWithMintAssets.getMint();

            mintAssets.forEach(amount -> {
                //B
                String assetName = HexUtil.encodeHexString(assetNameToBytes(amount.getAssetName()));

                // Get asset entity from minted existing asset map. If not exists, create a new one
                var ma = getMultiAssetByPolicyAndNameFromList(tx, mintAssetsExists, amount.getPolicyId(),
                        assetNameToBytes(amount.getAssetName()), Objects.isNull(assetName) ? EMPTY_STRING : assetName);
                var supply = ma.getSupply();
                var quantity = amount.getQuantity();
                ma.setSupply(supply.add(quantity));

                // Build a new asset mint entity
                MaTxMint maTxMint = MaTxMint.builder()
                        .tx(tx)
                        .ident(ma)
                        .quantity(amount.getQuantity())
                        .build();

                // If this asset is new, add it to existing mint assets map for future searches
                mintAssetsExists.put(Pair.of(ma.getName(), ma.getPolicy()), ma);
                maTxMints.add(maTxMint);
            });
        });

        Collection<MultiAsset> multiAssets = mintAssetsExists.values();
        long newTokenCount = multiAssets.stream()
                .map(BaseEntity::getId)
                .filter(Objects::isNull)
                .count();
        aggregatedDataCachingService.addTokenCount((int) newTokenCount);
        multiAssetRepository.saveAll(multiAssets);
        maTxMintRepository.saveAll(maTxMints);
    }

    @Override
    public Collection<MultiAsset> findMultiAssetsByFingerprintIn(Set<String> fingerprints) {
        Collection<MultiAsset> multiAssets = new ConcurrentLinkedQueue<>();

        var queryBatches = Lists.partition(new ArrayList<>(fingerprints), BATCH_QUERY_SIZE);
        queryBatches.forEach(fingerprintBatch ->
                multiAssets.addAll(multiAssetRepository.findMultiAssetsByFingerprintIn(fingerprintBatch)));

        return multiAssets;
    }

    private MultiAsset getMultiAssetByPolicyAndNameFromList(Tx tx,
                                                            Map<Pair<String, String>, MultiAsset> multiAssetMap,
                                                            String policy, byte[] assetNameBytes,
                                                            String name) {
        MultiAsset multiAsset = multiAssetMap.get(Pair.of(name, policy));
        if (Objects.isNull(multiAsset)) {
            /*
             * This asset has not been minted before so mark its first appearance at current tx's
             * index and block number
             */
            String fingerPrint = ConsumerAssetUtil.getFingerPrint(HexUtil.decodeHexString(name), policy);
            blockDataService.setFingerprintFirstAppearedBlockNoAndTxIdx(
                    fingerPrint, tx.getBlock().getBlockNo(), tx.getBlockIndex());

            // Trim null character, as it is not supported on PostgreSQL text field
            String nameView = StringUtil.isUtf8(assetNameBytes)
                    ? new String(assetNameBytes, StandardCharsets.UTF_8)
                    .replace(ConsumerConstant.BYTE_NULL, EMPTY_STRING)
                    : null;

            return MultiAsset.builder()
                    .policy(policy)
                    .name(name)
                    .nameView(nameView)
                    .fingerprint(fingerPrint)
                    .supply(BigInteger.ZERO)
                    .totalVolume(BigInteger.ZERO)
                    .txCount(INITIAL_TX_COUNT)
                    .time(tx.getBlock().getTime())
                    .build();
        }

        return multiAsset;
    }

    @Override
    public MultiValueMap<String, MaTxOut> buildMaTxOut(AggregatedTxOut txOutput, TxOut txOut) {
        MultiValueMap<String, MaTxOut> maTxOutMap = new LinkedMultiValueMap<>();

        txOutput.getAmounts().stream()
                .filter(amount -> !LOVELACE.equals(amount.getAssetName()))
                .forEach(amount -> {
                    String fingerprint = ConsumerAssetUtil
                            .getFingerPrint(assetNameToBytes(amount.getAssetName()), amount.getPolicyId());
                    MaTxOut maTxOut = MaTxOut.builder()
                            .txOut(txOut)
                            .quantity(amount.getQuantity())
                            .build();
                    maTxOutMap.add(fingerprint, maTxOut);
                });

        return maTxOutMap;
    }

    @Override
    public Collection<MaTxOut> updateIdentMaTxOuts(MultiValueMap<String, MaTxOut> maTxOuts) {
        if (CollectionUtils.isEmpty(maTxOuts)) {
            return Collections.emptyList();
        }

        Set<String> fingerPrints = maTxOuts.keySet();
        Map<String, MultiAsset> fingerprintMaMap = findMultiAssetsByFingerprintIn(fingerPrints)
                .parallelStream()
                .collect(Collectors.toConcurrentMap(MultiAsset::getFingerprint, Function.identity()));

        Queue<MaTxOut> result = new ConcurrentLinkedQueue<>();
        maTxOuts.entrySet().parallelStream().forEach(entry -> {
            String fingerprint = entry.getKey();
            Pair<Long, Long> firstAppearedBlockNoAndTxIdx = blockDataService
                    .getFingerprintFirstAppearedBlockNoAndTxIdx(fingerprint);
            MultiAsset ident = fingerprintMaMap.get(fingerprint);
            List<MaTxOut> maTxOutList = entry.getValue();

            maTxOutList.parallelStream().forEach(maTxOut -> {
                Tx tx = maTxOut.getTxOut().getTx();
                /*
                 * If the asset's first appeared block no and tx index is null, it had been minted in other
                 * transaction not in current block batch
                 *
                 * If the first appeared block no and tx index is not null, check if the current block
                 * number is the higher than this asset's first appeared block no, or block no equals
                 * and tx index is higher than the asset's first appeared tx index. If both conditions
                 * do not meet, skip this asset output
                 */
                boolean assetHasBeenMintedBefore = Objects.nonNull(ident)
                        && (Objects.isNull(firstAppearedBlockNoAndTxIdx)
                        || firstAppearedBlockNoAndTxIdx.getFirst() < tx.getBlock().getBlockNo()
                        || (firstAppearedBlockNoAndTxIdx.getFirst().equals(tx.getBlock().getBlockNo())
                        && firstAppearedBlockNoAndTxIdx.getSecond() <= tx.getBlockIndex()));
                if (!assetHasBeenMintedBefore) {
                    log.warn(
                            "TxHash {}, Index {}, Finger print {} multi asset has not been minted before",
                            tx.getHash(), maTxOut.getTxOut().getIndex(), fingerprint);
                    blockDataService.saveAssetFingerprintNotMintedAtTx(fingerprint, tx.getHash());
                    //System.exit(1);//TODO dev check only
                } else {
                    maTxOut.setIdent(ident);
                    result.add(maTxOut);
                }
            });
        });

        return result;
    }

    @Override
    public Collection<MaTxOut> findAllByTxOutIn(Collection<TxOut> txOuts) {
        Collection<MaTxOut> maTxOuts = new ConcurrentLinkedQueue<>();

        Set<Long> txOutIds = txOuts.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        var queryBatches = Lists.partition(new ArrayList<>(txOutIds), BATCH_QUERY_SIZE);
        queryBatches.parallelStream().forEach(txOutIdBatch ->
                multiAssetTxOutRepository
                        .findAllByTxOutIdsIn(txOutIdBatch)
                        .parallelStream()
                        .forEach(maTxOutProjection -> {
                            TxOut txOut = TxOut.builder().id(maTxOutProjection.getTxOutId()).build();

                            MultiAsset multiAsset = MultiAsset.builder()
                                    .fingerprint(maTxOutProjection.getFingerprint())
                                    .build();

                            MaTxOut maTxOut = MaTxOut.builder()
                                    .txOut(txOut)
                                    .ident(multiAsset)
                                    .quantity(maTxOutProjection.getQuantity())
                                    .build();
                            maTxOuts.add(maTxOut);
                        }));

        return maTxOuts;
    }

    @Override
    @Transactional
    public void rollbackMultiAssets(Collection<Tx> txs) {
        // Find all multi asset mints
        List<MaTxMintProjection> maTxMints = maTxMintRepository.findAllByTxIn(txs);

        // Find all multi asset ids (ident ids) as well as their associated tx count
        List<MultiAssetTxCountProjection> multiAssetsTxCounts =
                multiAssetRepository.getMultiAssetTxCountByTxs(txs);
        if (CollectionUtils.isEmpty(maTxMints) && CollectionUtils.isEmpty(multiAssetsTxCounts)) {
            log.info("No assets and asset mints were found, skipping multi asset rollback");
            return;
        }

        Set<Long> maIds = maTxMints.stream()
                .map(MaTxMintProjection::getIdentId)
                .collect(Collectors.toSet());
        maIds.addAll(multiAssetsTxCounts
                .stream()
                .map(MultiAssetTxCountProjection::getIdentId)
                .collect(Collectors.toSet()));

        // Find multi asset entities for rollback
        Map<Long, MultiAsset> multiAssetMap = multiAssetRepository.findAllById(maIds)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        // Find multi asset total volume in txs for rollback
        List<MultiAssetTotalVolumeProjection> multiAssetTotalVolumes =
                multiAssetRepository.getMultiAssetTotalVolumeByTxs(txs);

        // Rollback asset mints
        maTxMints.forEach(maTxMint -> {
            Long identId = maTxMint.getIdentId();
            MultiAsset multiAsset = multiAssetMap.get(identId);
            multiAsset.setSupply(multiAsset.getSupply().subtract(maTxMint.getQuantity()));
        });

        // Rollback asset tx count
        multiAssetsTxCounts.forEach(multiAssetTxCount -> {
            MultiAsset multiAsset = multiAssetMap.get(multiAssetTxCount.getIdentId());
            multiAsset.setTxCount(multiAsset.getTxCount() - multiAssetTxCount.getTxCount());
        });

        // Rollback asset total volume
        multiAssetTotalVolumes.forEach(multiAssetTotalVolume -> {
            MultiAsset multiAsset = multiAssetMap.get(multiAssetTotalVolume.getIdentId());
            BigInteger newTotalVolume = multiAsset.getTotalVolume()
                    .subtract(multiAssetTotalVolume.getTotalVolume());
            multiAsset.setTotalVolume(newTotalVolume);
        });

        if (!CollectionUtils.isEmpty(multiAssetMap)) {
            multiAssetRepository.saveAll(multiAssetMap.values());
        }

        log.info("Multi asset rollback finished: {} multi assets updated", multiAssetMap.size());
    }
}
