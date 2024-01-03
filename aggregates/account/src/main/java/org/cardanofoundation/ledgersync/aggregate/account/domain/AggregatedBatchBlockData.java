package org.cardanofoundation.ledgersync.aggregate.account.domain;

import com.bloxbean.cardano.yaci.store.common.domain.BlockAwareDomain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AggregatedBatchBlockData {

    // Key is address (Bech32 or Base58 format)
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap;

    // Key is stake address hex, value is first appeared tx hash
    Map<String, String> stakeAddressTxHashMap;

    // Key is asset fingerprint, value is its first minted block no and tx index within that block
    Map<String, Pair<Long, Long>> fingerprintFirstAppearedMap;

    // A set of not minted asset fingerprint and associated tx hash pairs to skip asset operations
    Set<Pair<String, String>> notMintedAssetFingerprintTxHashSet;

    Map<String, AggregatedBlock> aggregatedBlockMap;

    Map<String, BlockAwareDomain> txBlockInfoMap;

    public AggregatedBatchBlockData() {
        aggregatedAddressBalanceMap = new ConcurrentHashMap<>();
        stakeAddressTxHashMap = new ConcurrentHashMap<>();
        notMintedAssetFingerprintTxHashSet = new LinkedHashSet<>();
        fingerprintFirstAppearedMap = new ConcurrentHashMap<>();
        aggregatedBlockMap = new LinkedHashMap<>();
        txBlockInfoMap = new LinkedHashMap<>();
    }

    // This method must be called every batch saving
    public void clear() {
        aggregatedAddressBalanceMap.clear();
        stakeAddressTxHashMap.clear();
        notMintedAssetFingerprintTxHashSet.clear();
        fingerprintFirstAppearedMap.clear();

        aggregatedBlockMap.clear();
        txBlockInfoMap.clear();
    }

}
