package org.cardanofoundation.ledgersync.aggregate;

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

    // Key is stake address hex, value is first appeared tx hash
    Map<String, String> stakeAddressTxHashMap;

    // Key is asset fingerprint, value is its first minted block no and tx index within that block
    Map<String, Pair<Long, Long>> fingerprintFirstAppearedMap;

    // A set of not minted asset fingerprint and associated tx hash pairs to skip asset operations
    Set<Pair<String, String>> notMintedAssetFingerprintTxHashSet;

    Map<String, AggregatedBlock> aggregatedBlockMap;

    public AggregatedBatchBlockData() {
        stakeAddressTxHashMap = new ConcurrentHashMap<>();
        notMintedAssetFingerprintTxHashSet = new LinkedHashSet<>();
        fingerprintFirstAppearedMap = new ConcurrentHashMap<>();

        aggregatedBlockMap = new LinkedHashMap<>();
    }

    // This method must be called every batch saving
    public void clear() {
        stakeAddressTxHashMap.clear();
        notMintedAssetFingerprintTxHashSet.clear();
        fingerprintFirstAppearedMap.clear();

        aggregatedBlockMap.clear();
    }
}
