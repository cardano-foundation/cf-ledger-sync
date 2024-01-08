package org.cardanofoundation.ledgersync.aggregate.account.service.impl;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.aggregate.account.model.Address;
import org.cardanofoundation.ledgersync.aggregate.account.model.AddressToken;
import org.cardanofoundation.ledgersync.aggregate.account.model.AddressTokenBalance;
import org.cardanofoundation.ledgersync.aggregate.account.model.AddressTxBalance;
import org.cardanofoundation.ledgersync.aggregate.account.projection.AddressTxBalanceProjection;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTokenBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTokenRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.custom.CustomAddressTokenBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockDataService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.cardanofoundation.ledgersync.aggregate.account.constant.Constants.ADDRESS_TOKEN_BALANCE_BATCH_QUERY_SIZE;
import static org.cardanofoundation.ledgersync.aggregate.account.constant.Constants.BATCH_QUERY_SIZE;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressBalanceServiceImpl implements AddressBalanceService {

    AddressRepository addressRepository;
    AddressTokenRepository addressTokenRepository;
    AddressTxBalanceRepository addressTxBalanceRepository;
    AddressTokenBalanceRepository addressTokenBalanceRepository;
    CustomAddressTokenBalanceRepository customAddressTokenBalanceRepository;
    BlockDataService blockDataService;

    @Override
    public void handleAddressBalance(
            Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap,
            Map<String, String> stakeAddressMap) {
        if (CollectionUtils.isEmpty(aggregatedAddressBalanceMap)) {
            return;
        }

        // Find all existing address records first
        var addressMap = getAddressMapFromAggregatedAddressBalance(aggregatedAddressBalanceMap);

        /*
         * For each aggregated address balance object, if an address record
         * exists, update its balance and tx count, otherwise create a new
         * record and set its balance/tx count
         */
        aggregatedAddressBalanceMap.forEach((address, aggregatedAddressBalance) -> {
            String hexStakeAddress = aggregatedAddressBalance.getAddress().getStakeAddress();
            String stakeAddress = StringUtils.hasText(hexStakeAddress) ?
                    stakeAddressMap.get(hexStakeAddress) : null;
            Address addressEntity = addressMap.get(address);
            if (Objects.isNull(addressEntity)) {
                addressEntity = buildNewAddressEntity(aggregatedAddressBalance, stakeAddress);
                addressMap.put(address, addressEntity);
            }

            // Try to get the stake address again and save it in stake address map, in case the
            // current address record comes from transaction input, which has not been accounted
            // yet in the stake address map
            if (Objects.isNull(stakeAddress)) {
                var stakeAddressView = addressEntity.getStakeAddress();

                // If the address record has stake address, save it in stake address map
                if (Objects.nonNull(stakeAddressView)) {
                    stakeAddressMap.put(hexStakeAddress, stakeAddressView);
                }
            }
            var txCount = (long) aggregatedAddressBalance.getTxCount();
            var balance = aggregatedAddressBalance.getTotalNativeBalance();
            addressEntity.setTxCount(addressEntity.getTxCount() + txCount);

            // Add balance to current address
            addressEntity.setBalance(addressEntity.getBalance().add(balance));

            // Add balance to current address's stake key (if the address has stake key, which the
            // addresses from Byron era don't have)
//            if (Objects.nonNull(stakeAddress)) {
//                BigInteger currentStakeAddressBalance = stakeAddress.getBalance();
//                stakeAddress.setBalance(currentStakeAddressBalance.add(balance));
//            }
        });

//        stakeAddressRepository.saveAll(stakeAddressMap.values());
        addressRepository.saveAll(addressMap.values());

        // Handle addresses' token balances
        handleAddressToken(addressMap, stakeAddressMap, aggregatedAddressBalanceMap.values());

        // Handle addresses' native balances
        handleAddressTxBalance(addressMap, stakeAddressMap,
                aggregatedAddressBalanceMap.values());
    }

    private Map<String, Address> getAddressMapFromAggregatedAddressBalance(
            Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap) {
        Collection<Address> addressesFound = new ConcurrentLinkedQueue<>();

        Set<String> addresses = aggregatedAddressBalanceMap.keySet();
        var queryBatches = Lists.partition(new ArrayList<>(addresses), BATCH_QUERY_SIZE);
        queryBatches.parallelStream().forEach(batch -> addressRepository
                .findAllByAddressIn(batch)
                .parallelStream()
                .forEach(addressesFound::add));

        return addressesFound.parallelStream()
                .collect(Collectors.toConcurrentMap(Address::getAddress, Function.identity()));
    }

    @Override
    public void rollbackAddressBalances(Long blockNo) {
//        if (CollectionUtils.isEmpty(txs)) {
//            log.info("No txs were found, skipping address balance rollback");
//            return;
//        }

        /*
         * Find all balance records associated to txs. This list will be used
         * to subtract an address's total balance record
         */
        var addressTxBalances = addressTxBalanceRepository.findAllByBlockNumberGreaterThan(blockNo);
        var addressSet = addressTxBalances.stream()
                .map(AddressTxBalanceProjection::getAddress)
                .collect(Collectors.toSet());

        /*
         * Find all address token balance records associated to txs. This list will be used
         * to subtract an address's total token balance record
         */
        var addressTokens = addressTokenRepository.findAllByBlockNumberGreaterThan(blockNo);
        var addressMultiAssetIdPairs = addressTokens.stream()
                .map(addressToken -> Pair.of(addressToken.getAddressId(), addressToken.getFingerprint()))
                .collect(Collectors.toSet());

        // Find all address records
        var addressMap = addressRepository.findAllByAddressIn(addressSet)
                .stream()
                .collect(Collectors.toMap(Address::getAddress, Function.identity()));

        // Find all stake address records based on address
//        var stakeAddressMap = addressRepository.findAllStakeAddressByAddressIn(addressMap.values())
//                .stream()
//                .collect(Collectors.toMap(StakeAddress::getView, Function.identity()));

        // Find all address token balance records
        var addressTokenBalanceMap = customAddressTokenBalanceRepository
                .findAllByAddressMultiAssetIdPairIn(addressMultiAssetIdPairs)
                .stream()
                .collect(Collectors.toMap(
                        addressTokenBalance ->
                                Pair.of(addressTokenBalance.getAddressId(),
                                        addressTokenBalance.getFingerprint()),
                        Function.identity()
                ));

        // Rollback address native balances
        rollbackAddressNativeBalances(addressTxBalances, addressMap);

        // Rollback address token balances
        rollbackAddressTokenBalances(addressTokens, addressTokenBalanceMap);

        addressRepository.saveAll(addressMap.values());
        addressTokenBalanceRepository.saveAll(addressTokenBalanceMap.values());
//        stakeAddressRepository.saveAll(stakeAddressMap.values());
        log.info("Address balances rollback finished");
    }

    private void rollbackAddressNativeBalances(
            List<AddressTxBalanceProjection> addressTxBalances,
            Map<String, Address> addressMap) {
        AtomicInteger rolledBackStakeAddressesCount = new AtomicInteger(0);
        AtomicInteger rolledBackAddressesCount = new AtomicInteger(0);

        /*
         * Create a map with key is address string (Base58 or Bech32 form) with its associated
         * tx hashes. This map helps keeping track of how many associated tx records of an address
         */
        Map<String, Set<String>> addressTxHashesMap = new HashMap<>();
        addressTxBalances.forEach(addressTxBalance -> {
            String addressStr = addressTxBalance.getAddress();
            Address address = addressMap.get(addressStr);

            // Rollback address balance
            BigInteger balance = addressTxBalance.getBalance();
            address.setBalance(address.getBalance().subtract(balance));
            rolledBackAddressesCount.incrementAndGet();

            // Rollback stake key balance (if the address has stake key, which the addresses from Byron
            // era don't have)
//            String stakeAddressView = address.getStakeAddress();
//            if (Objects.nonNull(stakeAddressView)) {
//                StakeAddress stakeAddress = stakeAddressMap.get(stakeAddressView);
//                BigInteger currentStakeAddressBalance = stakeAddress.getBalance();
//                stakeAddress.setBalance(currentStakeAddressBalance.subtract(balance));
//                rolledBackStakeAddressesCount.incrementAndGet();
//            }

            Set<String> txHashes = addressTxHashesMap.computeIfAbsent(addressStr, s -> new HashSet<>());
            String txHash = addressTxBalance.getTxHash();
            if (!txHashes.contains(txHash)) {
                txHashes.add(txHash);
                address.setTxCount(address.getTxCount() - 1);
            }
        });

        log.info("Native address balance rollback finished: {} addresses rolled back, "
                        + "{} stake addresses rolled back",
                rolledBackAddressesCount.get(),
                rolledBackStakeAddressesCount.get());
    }

    private void rollbackAddressTokenBalances(List<AddressToken> addressTokens,
                                              Map<Pair<Long, String>, AddressTokenBalance> addressTokenBalanceMap) {
        addressTokens.forEach(addressToken -> {
            Long addressId = addressToken.getAddressId();
            String fingerprint = addressToken.getFingerprint();
            Pair<Long, String> addressMultiAssetIdPair = Pair.of(addressId, fingerprint);
            AddressTokenBalance addressTokenBalance = addressTokenBalanceMap.get(addressMultiAssetIdPair);

            // Address token record consists of an address's transferred token balance in a tx, hence
            // the address's current token balance is subtracted with this transferred balance
            BigInteger currentBalance = addressTokenBalance.getBalance();
            BigInteger balanceInTx = addressToken.getBalance();
            addressTokenBalance.setBalance(currentBalance.subtract(balanceInTx));
        });
    }

    private void handleAddressTxBalance(Map<String, Address> addressMap,
                                        Map<String, String> stakeAddressMap,
                                        Collection<AggregatedAddressBalance> addressBalances) {
        List<AddressTxBalance> addressTxBalances = addressBalances.stream()
                .flatMap(addressBalance -> {
                    var address = addressMap.get(addressBalance.getAddress().getAddress());
                    var stakeKeyHash = addressBalance.getAddress().getStakeAddress();
                    var stakeAddress = StringUtils.hasText(stakeKeyHash) ?
                            stakeAddressMap.get(stakeKeyHash) : null;
                    var txNativeBalance = addressBalance.getTxNativeBalance();
                    return buildAddressTxBalances(address, stakeAddress, txNativeBalance);
                })
                .toList();

        addressTxBalanceRepository.saveAll(addressTxBalances);
    }

    private Stream<AddressTxBalance> buildAddressTxBalances(Address address,
                                                            String stakeAddress,
                                                            Map<String, AtomicReference<BigInteger>> txNativeBalance) {
        return txNativeBalance.entrySet().stream()
                .map(txNativeBalanceEntry -> {
                    String txHash = txNativeBalanceEntry.getKey();
                    BigInteger balance = txNativeBalanceEntry.getValue().get();
//                    Tx tx = txMap.get(txHash);

//                    if (tx == null)
//                        throw new IllegalStateException("Tx not found for hash " + txHash);
                    // Increment this account (address)'s tx count
//                    Integer epochNo = tx.getBlock().getEpochNo();
//                    String account = Objects.isNull(address.getStakeAddress()) ?
//                            address.getAddress() : address.getStakeAddress().getId().toString();

//                    if (Objects.nonNull(epochNo)) { // check for genesis data
//                        aggregatedDataCachingService.addAccountTxCountAtEpoch(epochNo, account, 1);
//                    }
                    return buildAddressTxBalance(address, stakeAddress, balance, txHash);
                });
    }

    private AddressTxBalance buildAddressTxBalance(Address address,
                                                   String stakeAddress,
                                                   BigInteger balance, String txHash) {
        var blockInfo = blockDataService.getBlockInfoOfTx(txHash);

        return AddressTxBalance.builder()
                .address(address)
                .stakeAddress(stakeAddress)
                .balance(balance)
                .blockTime(Timestamp.valueOf(LocalDateTime.ofEpochSecond(
                        blockInfo.getBlockTime(), 0, ZoneOffset.ofHours(0))))
                .slot(blockInfo.getSlot())
                .blockNumber(blockInfo.getBlockNumber())
                .txHash(txHash)
                .build();
    }

    private void handleAddressToken(Map<String, Address> addressMap,
                                    Map<String, String> stakeAddressMap,
                                    Collection<AggregatedAddressBalance> aggregatedAddressBalances) {
        // Get all asset fingerprints from all aggregated address balance objects
        Set<String> fingerprints = getMaFingerprintsFromAddressBalance(aggregatedAddressBalances);
        if (CollectionUtils.isEmpty(fingerprints)) {
            return;
        }

        // Get all address fingerprint pairs from all aggregated address balance objects
        Set<Pair<String, String>> addressFingerprintPairs =
                getAddressFingerprintPairsFromAddressBalance(aggregatedAddressBalances);

        // Find all existing multi assets by fingerprints
//        Map<String, MultiAsset> multiAssetMap = findMultiAssetsByFingerprintIn(fingerprints);

        // Find all existing address token (multi asset) balance records, with key is a pair
        // of associated address and multi asset entity id
        Map<Pair<Long, String>, AddressTokenBalance> addressTokenBalanceMap =
                getAddressTokenBalanceMap(addressFingerprintPairs);

        // Create a new map that keeps track of new address token balance records
        // in case associated Address and MultiAsset entities are new
        Map<Pair<String, String>, AddressTokenBalance> newAddressTokenBalanceMap = new HashMap<>();

        /*
         * Create a map of ass-et fingerprint with its associated tx hashes
         * It helps keeping track of how many txs an asset associates with
         */
        Map<String, Set<String>> fingerprintTxHashesMap = new HashMap<>();

        /*
         * Process all asset balances of aggregated address balances and map them to
         * address token and address token balance records
         */
        List<AddressToken> addressTokens = new ArrayList<>();
        processAssetBalances(addressMap, aggregatedAddressBalances,
                addressTokenBalanceMap, newAddressTokenBalanceMap,
                fingerprintTxHashesMap, stakeAddressMap, addressTokens);

        // Re-calculate amount of tx that an asset is associated with
//        fingerprintTxHashesMap.forEach((fingerprint, txHashes) -> {
//            MultiAsset multiAsset = multiAssetMap.get(fingerprint);
//
//            // If it's null, it's not minted
//            if (Objects.nonNull(multiAsset)) {
//                multiAsset.setTxCount(multiAsset.getTxCount() + txHashes.size());
//            }
//        });

        addressTokenRepository.saveAll(addressTokens);
//        multiAssetRepository.saveAll(multiAssetMap.values());
        addressTokenBalanceRepository.saveAll(addressTokenBalanceMap.values());
        addressTokenBalanceRepository.saveAll(newAddressTokenBalanceMap.values());
    }

    private Map<Pair<Long, String>, AddressTokenBalance> getAddressTokenBalanceMap(
            Set<Pair<String, String>> addressFingerprintPairs) {
        // A map with key is a pair of address entity id and multi asset entity id, and value is
        // the referenced address token balance entity
        Map<Pair<Long, String>, AddressTokenBalance> addressTokenBalanceMap = new ConcurrentHashMap<>();

        var queryBatches = Lists.partition(
                new ArrayList<>(addressFingerprintPairs), ADDRESS_TOKEN_BALANCE_BATCH_QUERY_SIZE);

        queryBatches.parallelStream().forEach(batch -> customAddressTokenBalanceRepository
                .findAllByAddressFingerprintPairIn(batch)
                .parallelStream()
                .forEach(addressTokenBalance -> {
                    Pair<Long, String> key = Pair.of(
                            addressTokenBalance.getAddressId(),
                            addressTokenBalance.getFingerprint());
                    addressTokenBalanceMap.put(key, addressTokenBalance);
                }));
        return addressTokenBalanceMap;
    }

//    private Map<String, MultiAsset> findMultiAssetsByFingerprintIn(Set<String> fingerprints) {
//        return multiAssetService.findMultiAssetsByFingerprintIn(fingerprints)
//                .stream()
//                .collect(Collectors.toMap(MultiAsset::getFingerprint, Function.identity()));
//    }

    private void processAssetBalances(Map<String, Address> addressMap,
                                      Collection<AggregatedAddressBalance> aggregatedAddressBalances,
                                      Map<Pair<Long, String>, AddressTokenBalance> addressTokenBalanceMap,
                                      Map<Pair<String, String>, AddressTokenBalance> newAddressTokenBalanceMap,
                                      Map<String, Set<String>> fingerprintTxHashesMap,
                                      Map<String, String> stakeAddressMap,
                                      List<AddressToken> addressTokens) {
        aggregatedAddressBalances.forEach(addressBalance -> {
            Address address = addressMap.get(addressBalance.getAddress().getAddress());
            String stakeKeyHash = addressBalance.getAddress().getStakeAddress();
            addressBalance.getMaBalances().forEach((txHashFingerprintPair, maBalance) -> {
                /*
                 * Get all required objects, including this address balance's associated
                 * tx hash and asset fingerprint, subtracted or added balance, associated
                 * asset entity
                 */
                String txHash = txHashFingerprintPair.getFirst();
                String fingerprint = txHashFingerprintPair.getSecond();
                BigInteger balance = maBalance.get();
//                MultiAsset multiAsset = multiAssetMap.get(fingerprint);
                String stakeAddress = StringUtils.hasText(stakeKeyHash) ?
                        stakeAddressMap.get(stakeKeyHash) : null;
                /*
                 * If there is no associated asset entity, it means that the asset has not
                 * been minted before and no balance is subtracted/added
                 */
                if (blockDataService.isAssetFingerprintNotMintedInTx(fingerprint, txHash)) {
                    log.warn(
                            "Skip asset with fingerprint {} because it has not been minted before",
                            fingerprint);
                    return;
                }

//                if (Objects.isNull(multiAsset)) {
//                    throw new IllegalStateException(
//                            String.format("Asset with fingerprint %s is null!", fingerprint)
//                    );
//                }

                Set<String> txHashes = fingerprintTxHashesMap
                        .computeIfAbsent(fingerprint, s -> new HashSet<>());
                txHashes.add(txHash);

                AddressToken addressToken =
                        buildAddressToken(address, balance, null,
                                null,
                                fingerprint,
                                txHash);

                addressTokens.add(addressToken);
                updateAddressTokenBalance(
                        addressTokenBalanceMap, newAddressTokenBalanceMap,
                        address, stakeAddress, fingerprint, balance);
//                updateTokenTotalVolume(multiAsset, balance);
            });
        });
    }

    private void updateAddressTokenBalance(
            Map<Pair<Long, String>, AddressTokenBalance> addressTokenBalanceMap,
            Map<Pair<String, String>, AddressTokenBalance> newAddressTokenBalanceMap,
            Address address, String stakeAddress, String fingerprint, BigInteger balance) {
        Long addressId = address.getId();
//        String fingerprint = multiAsset.getFingerprint();
        AddressTokenBalance addressTokenBalance;

        if (Objects.nonNull(addressId) && Objects.nonNull(fingerprint)) {
            Pair<Long, String> addressFingerprintIdPair = Pair.of(addressId, fingerprint);
            addressTokenBalance = addressTokenBalanceMap.computeIfAbsent(addressFingerprintIdPair,
                    unused -> buildAddressTokenBalance(address, null, null,
                            fingerprint,
                            stakeAddress));
        } else {
            String addressString = address.getAddress();
            Pair<String, String> addressFingerprintPair = Pair.of(addressString, fingerprint);
            addressTokenBalance = newAddressTokenBalanceMap.computeIfAbsent(addressFingerprintPair,
                    unused -> buildAddressTokenBalance(address, null, null,
                            fingerprint,
                            stakeAddress));
        }

        BigInteger currentBalance = addressTokenBalance.getBalance();
        BigInteger finalBalance = currentBalance.add(balance);
        addressTokenBalance.setBalance(finalBalance);
    }

//    private void updateTokenTotalVolume(MultiAsset multiAsset, BigInteger balance) {
//        BigInteger currentTotalVolume = multiAsset.getTotalVolume();
//        if (balance.compareTo(BigInteger.ZERO) > 0) {
//            BigInteger totalVolume = currentTotalVolume.add(balance);
//            multiAsset.setTotalVolume(totalVolume);
//        }
//    }

    private Set<String> getMaFingerprintsFromAddressBalance(
            Collection<AggregatedAddressBalance> aggregatedAddressBalances) {
        return aggregatedAddressBalances.stream()
                .map(AggregatedAddressBalance::getMaBalances)
                .flatMap(maBalanceMap -> maBalanceMap.keySet().stream())
                .map(Pair::getSecond)
                .collect(Collectors.toSet());
    }

    private Set<Pair<String, String>> getAddressFingerprintPairsFromAddressBalance(
            Collection<AggregatedAddressBalance> aggregatedAddressBalances) {
        return aggregatedAddressBalances.stream()
                .flatMap(addressBalance -> {
                    String address = addressBalance.getAddress().getAddress();
                    return addressBalance.getMaBalances().keySet()
                            .stream()
                            .map(txHashFingerprintPair -> Pair.of(address, txHashFingerprintPair.getSecond()));
                }).collect(Collectors.toSet());
    }

    /**
     * Build a new address token balance entity
     *
     * @param address      address string (Base58 or Bech32 form)
     *                     //     * @param multiAsset   associated asset entity
     * @param stakeAddress address's associated stake address entity
     * @return new address token balance entity
     */
    private AddressTokenBalance buildAddressTokenBalance(Address address,
                                                         String assetName,
                                                         String policy,
                                                         String fingerprint,
                                                         String stakeAddress) {

        return AddressTokenBalance.builder()
                .address(address)
                .assetName(assetName)
                .policy(policy)
                .fingerprint(fingerprint)
                .balance(BigInteger.ZERO)
                .stakeAddress(stakeAddress)
                .build();
    }

    /**
     * Build a new address token entity
     *
     * @param address address string (Base58 or Bech32 form)
     * @param balance amount of subtracted or added balance of this address's asset in a tx
     *                //     * @param multiAsset associated asset entity
     *                //     * @param tx         associated tx entity
     * @return new address token entity
     */
    private AddressToken buildAddressToken(Address address,
                                           BigInteger balance,
                                           String assetName,
                                           String policy,
                                           String fingerprint,
                                           String txHash) {
        var blockInfo = blockDataService.getBlockInfoOfTx(txHash);

        return AddressToken.builder()
                .address(address)
                .balance(balance)
                .txHash(txHash)
                .assetName(assetName)
                .policy(policy)
                .fingerprint(fingerprint)
                .blockNumber(blockInfo.getBlockNumber())
                .slot(blockInfo.getSlot())
                .build();
    }

    /**
     * Build a new address entity
     *
     * @param aggregatedAddressBalance aggregated address balance object
     * @return new address entity
     */
    private Address buildNewAddressEntity(
            AggregatedAddressBalance aggregatedAddressBalance, String stakeAddress) {
        var address = aggregatedAddressBalance.getAddress().getAddress();
        var addressHasScript = aggregatedAddressBalance.isAddressHasScript();
        var paymentCred = aggregatedAddressBalance.getAddress().getPaymentCred();

        return Address.builder()
                .address(address)
                .txCount(0L)
                .balance(BigInteger.ZERO)
                .addressHasScript(addressHasScript)
                .stakeAddress(stakeAddress)
                .paymentCred(paymentCred)
                .build();
    }
}
