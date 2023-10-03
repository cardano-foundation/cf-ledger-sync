package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.cardanofoundation.explorer.consumercommon.entity.Address;
import org.cardanofoundation.explorer.consumercommon.entity.Address.AddressBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.AddressToken;
import org.cardanofoundation.explorer.consumercommon.entity.AddressTokenBalance;
import org.cardanofoundation.explorer.consumercommon.entity.AddressTxBalance;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.MultiAsset;
import org.cardanofoundation.explorer.consumercommon.entity.StakeAddress;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddress;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.AddressTxBalanceProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressTokenBalanceRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressTokenRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.StakeAddressRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.AggregatedDataCachingService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MultiAssetService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AddressBalanceServiceImplTest {

  @Mock
  MultiAssetRepository multiAssetRepository;

  @Mock
  AddressRepository addressRepository;

  @Mock
  AddressTokenRepository addressTokenRepository;

  @Mock
  AddressTxBalanceRepository addressTxBalanceRepository;

  @Mock
  AddressTokenBalanceRepository addressTokenBalanceRepository;

  @Mock
  StakeAddressRepository stakeAddressRepository;

  @Mock
  MultiAssetService multiAssetService;

  @Mock
  BlockDataService blockDataService;

  @Mock
  AggregatedDataCachingService aggregatedDataCachingService;

  @Captor
  ArgumentCaptor<Collection<Address>> addressesCaptor;

  @Captor
  ArgumentCaptor<List<AddressTxBalance>> addressTxBalancesCaptor;

  @Captor
  ArgumentCaptor<Collection<AddressToken>> addressTokensCaptor;

  @Captor
  ArgumentCaptor<Collection<AddressTokenBalance>> addressTokenBalancesCaptor;

  @Captor
  ArgumentCaptor<Collection<StakeAddress>> stakeAddressesCaptor;

  @Captor
  ArgumentCaptor<Collection<MultiAsset>> multiAssetsCaptor;

  AddressBalanceServiceImpl victim;

  @BeforeEach
  void setUp() {
    victim = new AddressBalanceServiceImpl(
        multiAssetRepository, addressRepository, addressTokenRepository,
        addressTxBalanceRepository, addressTokenBalanceRepository, stakeAddressRepository,
        multiAssetService, blockDataService, aggregatedDataCachingService
    );
  }

  @Test
  @DisplayName("Should skip processing on no address balance records in batch")
  void shouldSkipAddressBalanceProcessingTest() {
    Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap = Collections.emptyMap();
    Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();
    Map<String, Tx> txMap = Collections.emptyMap();

    Assertions.assertDoesNotThrow(() -> victim.handleAddressBalance(
        aggregatedAddressBalanceMap, stakeAddressMap, txMap));
    Mockito.verifyNoInteractions(multiAssetRepository);
    Mockito.verifyNoInteractions(addressRepository);
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressTxBalanceRepository);
    Mockito.verifyNoInteractions(addressTokenBalanceRepository);
    Mockito.verifyNoInteractions(stakeAddressRepository);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verifyNoInteractions(aggregatedDataCachingService);
  }

  @Test
  @DisplayName("Should process address balance without stake keys successfully")
  void shouldProcessAddressBalanceWithoutStakeKeySuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithoutStakeKey();
    Map<String, StakeAddress> stakeAddressMap = Collections.emptyMap();
    Map<String, Tx> txMap = Map.of(
        "5526b1373acfc774794a62122f95583ff17febb2ca8a0fe948d097e29cf99099", Mockito.mock(Tx.class),
        "b75ec46c406113372efeb1e57d9880856c240c9b531e3c680c1c4d8bf2253625", Mockito.mock(Tx.class),
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758", Mockito.mock(Tx.class)
    );
    txMap.forEach((txHash, tx) -> {
      Block block = Mockito.mock(Block.class);
      Mockito.when(tx.getHash()).thenReturn(txHash);
      Mockito.when(block.getTime())
          .thenReturn(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
      Mockito.when(block.getEpochNo()).thenReturn(2);
      Mockito.when(tx.getBlock()).thenReturn(block);
    });

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Assertions.assertDoesNotThrow(() ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));

    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);
    Mockito.verify(addressRepository, Mockito.times(1)).findAllByAddressIn(Mockito.anyCollection());
    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .saveAll(addressTxBalancesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTxBalanceRepository);
    Mockito.verifyNoInteractions(multiAssetRepository);
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressTokenBalanceRepository);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .addAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      BigInteger nativeBalance = address.getBalance();
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          nativeBalance);
    });

    List<AddressTxBalance> addressTxBalances = addressTxBalancesCaptor.getValue();
    addressTxBalances.forEach(addressTxBalance -> {
      String address = addressTxBalance.getAddress().getAddress();
      String txHash = addressTxBalance.getTx().getHash();
      Map<String, AtomicReference<BigInteger>> txNativeBalanceMap =
          addressBalanceMap.get(address).getTxNativeBalance();
      Assertions.assertEquals(txNativeBalanceMap.get(txHash).get(), addressTxBalance.getBalance());
    });
  }

  @Test
  @DisplayName("Should process address balance with stake keys successfully")
  void shouldProcessAddressBalanceWithStakeKeySuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKey();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, false);
    Map<String, Tx> txMap = Map.of(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758", Mockito.mock(Tx.class),
        "97e30ab8fa07780e0d78e45739c3f35142eb2f2494cdf23aff0e77d68afd0339", Mockito.mock(Tx.class)
    );
    txMap.forEach((txHash, tx) -> {
      Block block = Mockito.mock(Block.class);
      Mockito.when(tx.getHash()).thenReturn(txHash);
      Mockito.when(block.getTime())
          .thenReturn(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
      Mockito.when(block.getEpochNo()).thenReturn(2);
      Mockito.when(tx.getBlock()).thenReturn(block);
    });

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Assertions.assertDoesNotThrow(() ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));

    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);
    Mockito.verify(addressRepository, Mockito.times(1)).findAllByAddressIn(Mockito.anyCollection());
    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .saveAll(addressTxBalancesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTxBalanceRepository);
    Mockito.verifyNoInteractions(multiAssetRepository);
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressTokenBalanceRepository);
    Mockito.verifyNoInteractions(multiAssetService);
    Mockito.verifyNoInteractions(blockDataService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .addAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      BigInteger nativeBalance = address.getBalance();
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          nativeBalance);
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          address.getStakeAddress().getBalance());
    });

    List<AddressTxBalance> addressTxBalances = addressTxBalancesCaptor.getValue();
    addressTxBalances.forEach(addressTxBalance -> {
      String address = addressTxBalance.getAddress().getAddress();
      String txHash = addressTxBalance.getTx().getHash();
      Map<String, AtomicReference<BigInteger>> txNativeBalanceMap =
          addressBalanceMap.get(address).getTxNativeBalance();
      Assertions.assertEquals(txNativeBalanceMap.get(txHash).get(), addressTxBalance.getBalance());

      StakeAddress stakeAddress = addressTxBalance.getStakeAddress();
      Assertions.assertNotNull(stakeAddress);
      Assertions.assertEquals(stakeAddressMap.get(stakeAddress.getHashRaw()), stakeAddress);
    });
  }

  @Test
  @DisplayName("Should process address balance with stake keys and tokens successfully")
  void shouldProcessAddressBalanceWithStakeKeysAndTokensSuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKeyAndToken();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, false);
    Map<String, Tx> txMap = Map.of(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1", Mockito.mock(Tx.class),
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8", Mockito.mock(Tx.class),
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be", Mockito.mock(Tx.class),
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9", Mockito.mock(Tx.class)
    );
    txMap.forEach((txHash, tx) -> {
      Block block = Mockito.mock(Block.class);
      Mockito.when(tx.getHash()).thenReturn(txHash);
      Mockito.when(block.getTime())
          .thenReturn(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
      Mockito.when(block.getEpochNo()).thenReturn(2);
      Mockito.when(tx.getBlock()).thenReturn(block);
    });

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressFingerprintPairIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(multiAssetService.findMultiAssetsByFingerprintIn(Mockito.anySet()))
        .thenReturn(givenMultiAssetMap().values());
    Mockito.when(
            blockDataService.isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE);

    Assertions.assertDoesNotThrow(() ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));

    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);
    Mockito.verify(addressRepository, Mockito.times(1)).findAllByAddressIn(Mockito.anyCollection());
    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .saveAll(addressTxBalancesCaptor.capture());
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findMultiAssetsByFingerprintIn(Mockito.anySet());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(addressTokenRepository, Mockito.times(1)).saveAll(addressTokensCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1)).saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(1))
        .findAllByAddressFingerprintPairIn(Mockito.anyCollection());
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(2))
        .saveAll(addressTokenBalancesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenBalanceRepository);
    Mockito.verify(blockDataService, Mockito.times(2))
        .isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .addAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      BigInteger nativeBalance = address.getBalance();
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          nativeBalance);
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          address.getStakeAddress().getBalance());
    });

    List<AddressTxBalance> addressTxBalances = addressTxBalancesCaptor.getValue();
    addressTxBalances.forEach(addressTxBalance -> {
      String address = addressTxBalance.getAddress().getAddress();
      String txHash = addressTxBalance.getTx().getHash();
      Map<String, AtomicReference<BigInteger>> txNativeBalanceMap =
          addressBalanceMap.get(address).getTxNativeBalance();
      Assertions.assertEquals(txNativeBalanceMap.get(txHash).get(), addressTxBalance.getBalance());

      StakeAddress stakeAddress = addressTxBalance.getStakeAddress();
      Assertions.assertNotNull(stakeAddress);
      Assertions.assertEquals(stakeAddressMap.get(stakeAddress.getHashRaw()), stakeAddress);
    });

    Collection<AddressToken> addressTokens = addressTokensCaptor.getValue();
    addressTokens.forEach(addressToken -> {
      String address = addressToken.getAddress().getAddress();
      Map<Pair<String, String>, AtomicReference<BigInteger>> maBalances =
          addressBalanceMap.get(address).getMaBalances();
      String fingerprint = addressToken.getMultiAsset().getFingerprint();
      String txHash = addressToken.getTx().getHash();
      Pair<String, String> key = Pair.of(txHash, fingerprint);
      BigInteger balance = maBalances.get(key).get();
      Assertions.assertEquals(balance, addressToken.getBalance());
    });

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    multiAssets.forEach(
        multiAsset -> Assertions.assertEquals(BigInteger.ONE, multiAsset.getTotalVolume()));

    Collection<AddressTokenBalance> addressTokenBalances = addressTokenBalancesCaptor.getValue();
    addressTokenBalances.forEach(addressTokenBalance -> {
      String address = addressTokenBalance.getAddress().getAddress();
      Map<Pair<String, String>, AtomicReference<BigInteger>> maBalances =
          addressBalanceMap.get(address).getMaBalances();
      String fingerprint = addressTokenBalance.getMultiAsset().getFingerprint();
      BigInteger totalBalance = maBalances.entrySet().stream()
          .filter(entry -> entry.getKey().getSecond().equals(fingerprint))
          .map(entry -> entry.getValue().get())
          .reduce(BigInteger.ZERO, BigInteger::add);
      Assertions.assertEquals(totalBalance, addressTokenBalance.getBalance());

      StakeAddress stakeAddress = addressTokenBalance.getStakeAddress();
      Assertions.assertNotNull(stakeAddress);
      Assertions.assertEquals(stakeAddressMap.get(stakeAddress.getHashRaw()), stakeAddress);
    });
  }

  @Test
  @DisplayName("Should process address balance with existing address and tokens data successfully")
  void shouldProcessAddressBalanceWithExistingAddressAndTokensDataSuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKeyAndToken();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, false);

    // Take given address balance objects with only transaction inputs into account
    Map<String, AggregatedAddressBalance> addressBalanceMapWithInputOnly = givenAddressBalanceMapWithStakeKeyOnlyInput();
    addressBalanceMap.putAll(addressBalanceMapWithInputOnly);
    Map<String, StakeAddress> stakeAddressMapWithInputOnly = givenStakeAddressMap(addressBalanceMapWithInputOnly, true, false);
    Map<String, StakeAddress> stakeAddressMapIncludingInputOnly = new ConcurrentHashMap<>(stakeAddressMap);
    stakeAddressMapIncludingInputOnly.putAll(stakeAddressMapWithInputOnly);

    Map<String, Tx> txMap = Map.of(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1", Mockito.mock(Tx.class),
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8", Mockito.mock(Tx.class),
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be", Mockito.mock(Tx.class),
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9", Mockito.mock(Tx.class)
    );
    txMap.forEach((txHash, tx) -> {
      Block block = Mockito.mock(Block.class);
      Mockito.when(tx.getHash()).thenReturn(txHash);
      Mockito.when(block.getTime())
          .thenReturn(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
      Mockito.when(block.getEpochNo()).thenReturn(2);
      Mockito.when(tx.getBlock()).thenReturn(block);
    });
    Map<String, Address> addressMap = new LinkedHashMap<>();
    addressBalanceMap.forEach((address, aggregatedAddressBalance) -> {
      AddressBuilder<?, ?> addressBuilder = Address.builder();
      String stakeAddress = aggregatedAddressBalance.getAddress().getStakeAddress();
      if (StringUtils.hasText(stakeAddress)) {
        addressBuilder.stakeAddress(stakeAddressMapIncludingInputOnly.get(stakeAddress));
      }

      addressBuilder.address(address);
      addressBuilder.balance(BigInteger.ZERO);
      addressBuilder.txCount(0L);
      addressBuilder.addressHasScript(false);
      addressBuilder.id(new Random().nextLong());
      addressMap.put(address, addressBuilder.build());
    });
    Map<String, MultiAsset> givenMultiAssetMap = givenMultiAssetMap();
    List<AddressTokenBalance> givenAddressTokenBalances =
        givenAddressTokenBalance(addressBalanceMap, addressMap, givenMultiAssetMap, false);

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(new ArrayList<>(addressMap.values()));
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressFingerprintPairIn(Mockito.anyCollection()))
        .thenReturn(givenAddressTokenBalances);
    Mockito.when(multiAssetService.findMultiAssetsByFingerprintIn(Mockito.anySet()))
        .thenReturn(givenMultiAssetMap.values());
    Mockito.when(
            blockDataService.isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE);

    Assertions.assertDoesNotThrow(() ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));

    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);
    Mockito.verify(addressRepository, Mockito.times(1)).findAllByAddressIn(Mockito.anyCollection());
    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .saveAll(addressTxBalancesCaptor.capture());
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findMultiAssetsByFingerprintIn(Mockito.anySet());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(addressTokenRepository, Mockito.times(1)).saveAll(addressTokensCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1)).saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(1))
        .findAllByAddressFingerprintPairIn(Mockito.anyCollection());
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(2))
        .saveAll(addressTokenBalancesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenBalanceRepository);
    Mockito.verify(blockDataService, Mockito.times(2))
        .isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(6))
        .addAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      BigInteger nativeBalance = address.getBalance();
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          nativeBalance);
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          address.getStakeAddress().getBalance());
    });

    List<AddressTxBalance> addressTxBalances = addressTxBalancesCaptor.getValue();
    addressTxBalances.forEach(addressTxBalance -> {
      String address = addressTxBalance.getAddress().getAddress();
      String txHash = addressTxBalance.getTx().getHash();
      Map<String, AtomicReference<BigInteger>> txNativeBalanceMap =
          addressBalanceMap.get(address).getTxNativeBalance();
      Assertions.assertEquals(txNativeBalanceMap.get(txHash).get(), addressTxBalance.getBalance());

      StakeAddress stakeAddress = addressTxBalance.getStakeAddress();
      Assertions.assertNotNull(stakeAddress);
      Assertions.assertEquals(stakeAddressMapIncludingInputOnly.get(stakeAddress.getHashRaw()), stakeAddress);
    });

    Collection<AddressToken> addressTokens = addressTokensCaptor.getValue();
    addressTokens.forEach(addressToken -> {
      String address = addressToken.getAddress().getAddress();
      Map<Pair<String, String>, AtomicReference<BigInteger>> maBalances =
          addressBalanceMap.get(address).getMaBalances();
      String fingerprint = addressToken.getMultiAsset().getFingerprint();
      String txHash = addressToken.getTx().getHash();
      Pair<String, String> key = Pair.of(txHash, fingerprint);
      BigInteger balance = maBalances.get(key).get();
      Assertions.assertEquals(balance, addressToken.getBalance());
    });

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    multiAssets.forEach(
        multiAsset -> Assertions.assertEquals(BigInteger.ONE, multiAsset.getTotalVolume()));

    Collection<AddressTokenBalance> addressTokenBalances = addressTokenBalancesCaptor.getValue();
    addressTokenBalances.forEach(addressTokenBalance -> {
      String address = addressTokenBalance.getAddress().getAddress();
      Map<Pair<String, String>, AtomicReference<BigInteger>> maBalances =
          addressBalanceMap.get(address).getMaBalances();
      String fingerprint = addressTokenBalance.getMultiAsset().getFingerprint();
      BigInteger totalBalance = maBalances.entrySet().stream()
          .filter(entry -> entry.getKey().getSecond().equals(fingerprint))
          .map(entry -> entry.getValue().get())
          .reduce(BigInteger.ZERO, BigInteger::add);
      Assertions.assertEquals(totalBalance, addressTokenBalance.getBalance());

      StakeAddress stakeAddress = addressTokenBalance.getStakeAddress();
      Assertions.assertNotNull(stakeAddress);
      Assertions.assertEquals(stakeAddressMapIncludingInputOnly.get(stakeAddress.getHashRaw()), stakeAddress);
    });
  }

  @Test
  @DisplayName("Should skip address token balance processing on not minted asset")
  void shouldSkipAddressTokenBalanceProcessingOnNotMintedAssetTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKeyAndToken();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, false);
    Map<String, Tx> txMap = Map.of(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1", Mockito.mock(Tx.class),
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8", Mockito.mock(Tx.class),
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be", Mockito.mock(Tx.class),
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9", Mockito.mock(Tx.class)
    );
    txMap.forEach((txHash, tx) -> {
      Block block = Mockito.mock(Block.class);
      Mockito.when(tx.getHash()).thenReturn(txHash);
      Mockito.when(block.getTime())
          .thenReturn(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
      Mockito.when(block.getEpochNo()).thenReturn(2);
      Mockito.when(tx.getBlock()).thenReturn(block);
    });

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressFingerprintPairIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(multiAssetService.findMultiAssetsByFingerprintIn(Mockito.anySet()))
        .thenReturn(givenMultiAssetMap().values());
    Mockito.when(
            blockDataService.isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.TRUE);

    Assertions.assertDoesNotThrow(() ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));

    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    Mockito.verifyNoMoreInteractions(stakeAddressRepository);
    Mockito.verify(addressRepository, Mockito.times(1)).findAllByAddressIn(Mockito.anyCollection());
    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressRepository);
    Mockito.verify(addressTxBalanceRepository, Mockito.times(1))
        .saveAll(addressTxBalancesCaptor.capture());
    Mockito.verify(multiAssetService, Mockito.times(1))
        .findMultiAssetsByFingerprintIn(Mockito.anySet());
    Mockito.verifyNoMoreInteractions(multiAssetService);
    Mockito.verify(addressTokenRepository, Mockito.times(1)).saveAll(addressTokensCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenRepository);
    Mockito.verify(multiAssetRepository, Mockito.times(1)).saveAll(multiAssetsCaptor.capture());
    Mockito.verifyNoMoreInteractions(multiAssetRepository);
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(1))
        .findAllByAddressFingerprintPairIn(Mockito.anyCollection());
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(2))
        .saveAll(addressTokenBalancesCaptor.capture());
    Mockito.verifyNoMoreInteractions(addressTokenBalanceRepository);
    Mockito.verify(blockDataService, Mockito.times(2))
        .isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString());
    Mockito.verifyNoMoreInteractions(blockDataService);
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .addAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      BigInteger nativeBalance = address.getBalance();
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          nativeBalance);
      Assertions.assertEquals(
          addressBalanceMap.get(address.getAddress()).getTotalNativeBalance(),
          address.getStakeAddress().getBalance());
    });

    List<AddressTxBalance> addressTxBalances = addressTxBalancesCaptor.getValue();
    addressTxBalances.forEach(addressTxBalance -> {
      String address = addressTxBalance.getAddress().getAddress();
      String txHash = addressTxBalance.getTx().getHash();
      Map<String, AtomicReference<BigInteger>> txNativeBalanceMap =
          addressBalanceMap.get(address).getTxNativeBalance();
      Assertions.assertEquals(txNativeBalanceMap.get(txHash).get(), addressTxBalance.getBalance());
    });

    Collection<AddressToken> addressTokens = addressTokensCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(addressTokens));

    Collection<MultiAsset> multiAssets = multiAssetsCaptor.getValue();
    multiAssets.forEach(
        multiAsset -> Assertions.assertEquals(BigInteger.ZERO, multiAsset.getTotalVolume()));

    Collection<AddressTokenBalance> addressTokenBalances = addressTokenBalancesCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(addressTokenBalances));
  }

  @Test
  @DisplayName("Should fail if multi asset not found on address token balance update")
  void shouldFailIfMultiAssetNotFoundOnAddressTokenBalanceUpdateTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKeyAndToken();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, false, false);
    Map<String, Tx> txMap = Map.of(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1", Mockito.mock(Tx.class),
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8", Mockito.mock(Tx.class),
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be", Mockito.mock(Tx.class),
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9", Mockito.mock(Tx.class)
    );

    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressFingerprintPairIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(multiAssetService.findMultiAssetsByFingerprintIn(Mockito.anySet()))
        .thenReturn(Collections.emptyList());
    Mockito.when(
            blockDataService.isAssetFingerprintNotMintedInTx(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE);

    Assertions.assertThrows(IllegalStateException.class, () ->
        victim.handleAddressBalance(addressBalanceMap, stakeAddressMap, txMap));
  }

  @Test
  @DisplayName("Should skip rollback if no txs were supplied")
  void shouldSkipRollbackIfNoTxsSuppliedTest() {
    Assertions.assertDoesNotThrow(() ->
        victim.rollbackAddressBalances(Collections.emptyList()));

    Mockito.verifyNoInteractions(addressTxBalanceRepository);
    Mockito.verifyNoInteractions(addressTokenRepository);
    Mockito.verifyNoInteractions(addressRepository);
    Mockito.verifyNoInteractions(addressTokenBalanceRepository);
    Mockito.verifyNoInteractions(addressTokenBalanceRepository);
    Mockito.verifyNoInteractions(stakeAddressRepository);
  }

  @Test
  @DisplayName("Should rollback txs with no tokens successfully")
  void shouldRollbackTxsWithNoTokensSuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKey();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, true);
    Map<String, Tx> txMap = Map.of(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758", Mockito.mock(Tx.class),
        "97e30ab8fa07780e0d78e45739c3f35142eb2f2494cdf23aff0e77d68afd0339", Mockito.mock(Tx.class)
    );
    Map<String, Address> addressMap = new LinkedHashMap<>();
    addressBalanceMap.forEach((address, addressBalance) -> {
      AddressBuilder<?, ?> addressBuilder = Address.builder();
      String stakeKeyHash = addressBalance.getAddress().getStakeAddress();
      if (StringUtils.hasText(stakeKeyHash)) {
        StakeAddress stakeAddress = stakeAddressMap.get(stakeKeyHash);
        addressBuilder.stakeAddress(stakeAddress);
        addressBuilder.stakeAddressId(stakeAddress.getId());
      }

      addressBuilder.address(address);
      addressBuilder.balance(addressBalance.getTotalNativeBalance());
      addressBuilder.txCount((long) addressBalance.getTxCount());
      addressBuilder.addressHasScript(false);
      addressBuilder.id(new Random().nextLong());
      addressMap.put(address, addressBuilder.build());
    });
    List<AddressTxBalanceProjection> addressTxBalances =
        givenAddressTxBalanceProjection(addressBalanceMap);

    Mockito.when(addressTxBalanceRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(addressTxBalances);
    Mockito.when(addressTokenRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());
    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(new ArrayList<>(addressMap.values()));
    Mockito.when(addressRepository.findAllStakeAddressByAddressIn(Mockito.anyCollection()))
        .thenReturn(new ArrayList<>(stakeAddressMap.values()));
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressMultiAssetIdPairIn(Mockito.anyCollection()))
        .thenReturn(Collections.emptyList());

    Assertions.assertDoesNotThrow(() -> victim.rollbackAddressBalances(txMap.values()));

    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(1)).saveAll(addressTokenBalancesCaptor.capture());
    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(stakeAddressesCaptor.capture());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .subtractAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      Assertions.assertEquals(BigInteger.ZERO, address.getBalance());
      Assertions.assertEquals(0L, address.getTxCount());
    });

    Collection<AddressTokenBalance> addressTokenBalances = addressTokenBalancesCaptor.getValue();
    Assertions.assertTrue(CollectionUtils.isEmpty(addressTokenBalances));

    Collection<StakeAddress> stakeAddresses = stakeAddressesCaptor.getValue();
    stakeAddresses.forEach(stakeAddress ->
        Assertions.assertEquals(BigInteger.ZERO, stakeAddress.getBalance()));
  }

  @Test
  @DisplayName("Should rollback txs with tokens successfully")
  void shouldRollbackTxsWithTokensSuccessfullyTest() {
    Map<String, AggregatedAddressBalance> addressBalanceMap = givenAddressBalanceMapWithStakeKeyAndToken();
    Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap(addressBalanceMap, true, true);
    Map<String, Tx> txMap = Map.of(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1", Mockito.mock(Tx.class),
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8", Mockito.mock(Tx.class),
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be", Mockito.mock(Tx.class),
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9", Mockito.mock(Tx.class)
    );
    Map<String, Address> addressMap = new LinkedHashMap<>();
    addressBalanceMap.forEach((address, addressBalance) -> {
      AddressBuilder<?, ?> addressBuilder = Address.builder();
      String stakeKeyHash = addressBalance.getAddress().getStakeAddress();
      if (StringUtils.hasText(stakeKeyHash)) {
        StakeAddress stakeAddress = stakeAddressMap.get(stakeKeyHash);
        addressBuilder.stakeAddress(stakeAddress);
        addressBuilder.stakeAddressId(stakeAddress.getId());
      }

      addressBuilder.address(address);
      addressBuilder.balance(addressBalance.getTotalNativeBalance());
      addressBuilder.txCount((long) addressBalance.getTxCount());
      addressBuilder.addressHasScript(false);
      addressBuilder.id(new Random().nextLong());
      addressMap.put(address, addressBuilder.build());
    });
    List<AddressTxBalanceProjection> addressTxBalances =
        givenAddressTxBalanceProjection(addressBalanceMap);
    Map<String, MultiAsset> givenMultiAssetsMap = givenMultiAssetMap();
    List<AddressToken> addressTokens =
        givenAddressTokens(addressBalanceMap, addressMap, givenMultiAssetsMap);
    List<AddressTokenBalance> givenAddressTokenBalances =
        givenAddressTokenBalance(addressBalanceMap, addressMap, givenMultiAssetsMap, true);

    Mockito.when(addressTxBalanceRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(addressTxBalances);
    Mockito.when(addressTokenRepository.findAllByTxIn(Mockito.anyCollection()))
        .thenReturn(addressTokens);
    Mockito.when(addressRepository.findAllByAddressIn(Mockito.anyCollection()))
        .thenReturn(new ArrayList<>(addressMap.values()));
    Mockito.when(addressRepository.findAllStakeAddressByAddressIn(Mockito.anyCollection()))
        .thenReturn(new ArrayList<>(stakeAddressMap.values()));
    Mockito.when(addressTokenBalanceRepository
            .findAllByAddressMultiAssetIdPairIn(Mockito.anyCollection()))
        .thenReturn(givenAddressTokenBalances);

    Assertions.assertDoesNotThrow(() -> victim.rollbackAddressBalances(txMap.values()));

    Mockito.verify(addressRepository, Mockito.times(1)).saveAll(addressesCaptor.capture());
    Mockito.verify(addressTokenBalanceRepository, Mockito.times(1)).saveAll(addressTokenBalancesCaptor.capture());
    Mockito.verify(stakeAddressRepository, Mockito.times(1)).saveAll(stakeAddressesCaptor.capture());
    Mockito.verify(aggregatedDataCachingService, Mockito.times(4))
        .subtractAccountTxCountAtEpoch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
    Mockito.verifyNoMoreInteractions(aggregatedDataCachingService);

    Collection<Address> addresses = addressesCaptor.getValue();
    addresses.forEach(address -> {
      Assertions.assertEquals(BigInteger.ZERO, address.getBalance());
      Assertions.assertEquals(0L, address.getTxCount());
    });

    Collection<AddressTokenBalance> addressTokenBalances = addressTokenBalancesCaptor.getValue();
    addressTokenBalances.forEach(addressTokenBalance ->
        Assertions.assertEquals(BigInteger.ZERO, addressTokenBalance.getBalance()));

    Collection<StakeAddress> stakeAddresses = stakeAddressesCaptor.getValue();
    stakeAddresses.forEach(stakeAddress ->
        Assertions.assertEquals(BigInteger.ZERO, stakeAddress.getBalance()));
  }

  private static Map<String, AggregatedAddressBalance> givenAddressBalanceMapWithoutStakeKey() {
    // Preprod addresses
    List<String> addresses = List.of(
        "FHnt4NL7yPXuYUxBF33VX5dZMBDAab2kvSNLRzCskvuKNCSDknzrQvKeQhGUw5a", // byron address
        "addr_test1vz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclgmzkket"  // with balance
    );

    Map<String, AggregatedAddressBalance> addressBalanceMap = Map.of(
        addresses.get(0), AggregatedAddressBalance.from(addresses.get(0)),
        addresses.get(1), AggregatedAddressBalance.from(addresses.get(1))
    );

    addressBalanceMap.get(addresses.get(0)).addNativeBalance(
        "5526b1373acfc774794a62122f95583ff17febb2ca8a0fe948d097e29cf99099",
        BigInteger.valueOf(30000000000000000L)
    );
    addressBalanceMap.get(addresses.get(0)).subtractNativeBalance(
        "b75ec46c406113372efeb1e57d9880856c240c9b531e3c680c1c4d8bf2253625",
        BigInteger.valueOf(30000000000000000L)
    );
    addressBalanceMap.get(addresses.get(1)).addNativeBalance(
        "b75ec46c406113372efeb1e57d9880856c240c9b531e3c680c1c4d8bf2253625",
        BigInteger.valueOf(29999999999800000L)
    );
    addressBalanceMap.get(addresses.get(1)).subtractNativeBalance(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758",
        BigInteger.valueOf(300001506238057L)
    );

    return addressBalanceMap;
  }

  private static Map<String, AggregatedAddressBalance> givenAddressBalanceMapWithStakeKey() {
    // Preprod addresses
    List<String> addresses = List.of(
        "addr_test1qz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclvk35gzr67hz78plv88jemfs2p9e2780xm98cfrf4vvu0rq83pdz2",
        "addr_test1qz09v9yfxguvlp0zsnrpa3tdtm7el8xufp3m5lsm7qxzclfe9t57q689t694cfavck9sh2uw545vp2hz7m7yn03r57kslz5rjf"
    );

    Map<String, AggregatedAddressBalance> addressBalanceMap = Map.of(
        addresses.get(0), AggregatedAddressBalance.from(addresses.get(0)),
        addresses.get(1), AggregatedAddressBalance.from(addresses.get(1))
    );

    addressBalanceMap.get(addresses.get(0)).addNativeBalance(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758",
        BigInteger.valueOf(100000000000000L)
    );
    addressBalanceMap.get(addresses.get(0)).subtractNativeBalance(
        "97e30ab8fa07780e0d78e45739c3f35142eb2f2494cdf23aff0e77d68afd0339",
        BigInteger.valueOf(99000000000000L)
    );
    addressBalanceMap.get(addresses.get(1)).addNativeBalance(
        "a3d6f2627a56fe7921eeda546abfe164321881d41549b7f2fbf09ea0b718d758",
        BigInteger.valueOf(100000000000000L)
    );
    addressBalanceMap.get(addresses.get(1)).subtractNativeBalance(
        "97e30ab8fa07780e0d78e45739c3f35142eb2f2494cdf23aff0e77d68afd0339",
        BigInteger.valueOf(99000000000000L)
    );

    return addressBalanceMap;
  }

  private static Map<String, AggregatedAddressBalance> givenAddressBalanceMapWithStakeKeyAndToken() {
    // Preprod addresses
    List<String> addresses = List.of(
        "addr_test1qrccuem8w7gcda93vj5kw3fw2p8mw3ycyvqqtdyk03re5z58rkpk0zslj8xr9zgnehq4s30qz473j4fu9uesw3erefusecjrtn",
        "addr_test1qrjslwck9d3zp6hex3l0sg2cn4ff898aqzt78g9vrrmz09qmjupxkrr3z4e0fgepm5623d00lt8fzd9f4chtvpzf244qa25aks"
    );

    Map<String, AggregatedAddressBalance> addressBalanceMap = new ConcurrentHashMap<>();
    addressBalanceMap.put(addresses.get(0), AggregatedAddressBalance.from(addresses.get(0)));
    addressBalanceMap.put(addresses.get(1), AggregatedAddressBalance.from(addresses.get(1)));

    addressBalanceMap.get(addresses.get(0)).addNativeBalance(
        "b2b4f1bdab7d416e0a038151580c14565d0d3db6dd7b2b334f6360ddb822b2d1",
        BigInteger.valueOf(100000000L)
    );
    addressBalanceMap.get(addresses.get(0)).subtractNativeBalance(
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8",
        BigInteger.valueOf(50214693L)
    );
    addressBalanceMap.get(addresses.get(0)).addAssetBalance(
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8",
        "asset13h4774mjsw2aw9z462tmf2tw0pvfe4szfpl3tu",
        BigInteger.ONE
    );
    addressBalanceMap.get(addresses.get(1)).addNativeBalance(
        "310381fcb16dae9b53498db01bf6c089ef1c6aa6397c8775e2334de9ef3700be",
        BigInteger.valueOf(100000000L)
    );
    addressBalanceMap.get(addresses.get(1)).subtractNativeBalance(
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9",
        BigInteger.valueOf(50214517L)
    );
    addressBalanceMap.get(addresses.get(1)).addAssetBalance(
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9",
        "asset195eume52n7urc3ry82v7jrf7e75vn9d9ukkd67",
        BigInteger.ONE
    );

    return addressBalanceMap;
  }

  private static Map<String, AggregatedAddressBalance> givenAddressBalanceMapWithStakeKeyOnlyInput() {
    // Preprod addresses
    List<String> addresses = List.of(
        "addr_test1qrww9c58aq8lj5d8jz45nlj44sgtpud8nvs0sxztrtpwustfnwacd970adn8ppg4dlm8cuggnpd8rnclftj0ruvrfw4sfmj7y4",
        "addr_test1qzz038klmra5j4jjgnj6xdlft7uk9v46ghu9xlz8qjuuelhkzpnnjv8vf98m20lhqdzl60mcftq7r2lc4xtcsv0w6xjsqzpcqs"
    );

    Map<String, AggregatedAddressBalance> addressBalanceMap = Map.of(
        addresses.get(0), AggregatedAddressBalance.from(addresses.get(0)),
        addresses.get(1), AggregatedAddressBalance.from(addresses.get(1))
    );

    addressBalanceMap.get(addresses.get(0)).subtractNativeBalance(
        "62cfe7b507986aeddd258b1387e1b98c55a325896f1ba7cc8ee54902793e7cd8",
        BigInteger.valueOf(50214693L)
    );
    addressBalanceMap.get(addresses.get(1)).subtractNativeBalance(
        "4a020b1cdf83488bfeae3d294f3190349ca32db1810096c9410171427cce6cf9",
        BigInteger.valueOf(50214517L)
    );

    return addressBalanceMap;
  }

  private static Map<String, MultiAsset> givenMultiAssetMap() {
    return Map.of(
        "asset13h4774mjsw2aw9z462tmf2tw0pvfe4szfpl3tu",
        MultiAsset.builder()
            .id(1L)
            .fingerprint("asset13h4774mjsw2aw9z462tmf2tw0pvfe4szfpl3tu")
            .totalVolume(BigInteger.ZERO)
            .txCount(0L)
            .build(),
        "asset195eume52n7urc3ry82v7jrf7e75vn9d9ukkd67",
        MultiAsset.builder()
            .id(2L)
            .fingerprint("asset195eume52n7urc3ry82v7jrf7e75vn9d9ukkd67")
            .totalVolume(BigInteger.ZERO)
            .txCount(0L)
            .build()
    );
  }

  private static List<AddressTokenBalance> givenAddressTokenBalance(
      Map<String, AggregatedAddressBalance> addressBalanceMap, Map<String, Address> addressMap,
      Map<String, MultiAsset> givenMultiAssetsMap, boolean shouldAddBalance) {
    return addressBalanceMap.entrySet().stream()
        .flatMap(entry -> {
          String address = entry.getKey();
          Map<Pair<String, String>, AtomicReference<BigInteger>> maBalances =
              entry.getValue().getMaBalances();
          return maBalances.keySet()
              .stream()
              .map(pair -> {
                AddressTokenBalance addressTokenBalance = new AddressTokenBalance();
                addressTokenBalance.setId(new Random().nextLong());
                addressTokenBalance.setAddressId(addressMap.get(address).getId());
                addressTokenBalance.setBalance(BigInteger.ZERO);
                String fingerprint = pair.getSecond();
                MultiAsset multiAsset = givenMultiAssetsMap.get(fingerprint);
                if (Objects.nonNull(multiAsset)) {
                  addressTokenBalance.setMultiAssetId(multiAsset.getId());
                  if (shouldAddBalance) {
                    BigInteger balance = maBalances.get(pair).get();
                    addressTokenBalance.setBalance(balance);
                  }
                }
                return addressTokenBalance;
              });
        }).toList();
  }

  private static List<AddressTxBalanceProjection> givenAddressTxBalanceProjection(
      Map<String, AggregatedAddressBalance> addressBalanceMap) {
    return addressBalanceMap.values().stream()
        .flatMap(addressBalance -> {
          var txNativeBalance = addressBalance.getTxNativeBalance();
          return txNativeBalance.entrySet().stream()
              .map(entry -> {
                String txHash = entry.getKey();
                BigInteger balance = entry.getValue().get();
                AddressTxBalanceProjection addressTxBalance =
                    Mockito.mock(AddressTxBalanceProjection.class);

                Mockito.when(addressTxBalance.getAddress())
                    .thenReturn(addressBalance.getAddress().getAddress());
                Mockito.when(addressTxBalance.getBalance()).thenReturn(balance);
                Mockito.when(addressTxBalance.getTxHash()).thenReturn(txHash);
                Mockito.when(addressTxBalance.getEpochNo()).thenReturn(2);

                return addressTxBalance;
              });
        })
        .toList();
  }

  private static Map<String, StakeAddress> givenStakeAddressMap(
      Map<String, AggregatedAddressBalance> addressBalanceMap,
      boolean idRequired, boolean balanceRequired) {
    Map<String, StakeAddress> stakeAddressMap = new LinkedHashMap<>();

    addressBalanceMap.forEach((address, aggregatedAddressBalance) -> {
      BigInteger totalNativeBalance = aggregatedAddressBalance.getTotalNativeBalance();
      String stakeKeyHash = aggregatedAddressBalance.getAddress().getStakeAddress();
      StakeAddress stakeAddress = stakeAddressMap.computeIfAbsent(
          stakeKeyHash, AddressBalanceServiceImplTest::buildStakeAddress);
      if (idRequired && Objects.isNull(stakeAddress.getId())) {
        stakeAddress.setId(new Random().nextLong());
      }

      if (balanceRequired) {
        stakeAddress.setBalance(stakeAddress.getBalance().add(totalNativeBalance));
      }
    });

    return stakeAddressMap;
  }

  private static StakeAddress buildStakeAddress(String stakeKeyHash) {
    ShelleyAddress shelleyAddress = new ShelleyAddress(HexUtil.decodeHexString(stakeKeyHash));
    String stakeReference = HexUtil.encodeHexString(shelleyAddress.getStakeReference());
    return StakeAddress.builder()
        .hashRaw(stakeReference)
        .balance(BigInteger.ZERO)
        .build();
  }

  private static List<AddressToken> givenAddressTokens(
      Map<String, AggregatedAddressBalance> addressBalanceMap,
      Map<String, Address> addressMap, Map<String, MultiAsset> multiAssetMap) {
    List<AddressToken> addressTokens = new ArrayList<>();

    addressBalanceMap.forEach((address, aggregatedAddressBalance) -> {
      Address addresEntity = addressMap.get(address);
      aggregatedAddressBalance.getMaBalances().forEach((txHashFingerprintPair, balance) -> {
        String fingerprint = txHashFingerprintPair.getSecond();
        MultiAsset multiAsset = multiAssetMap.get(fingerprint);
        AddressToken addressToken = AddressToken.builder()
            .address(addresEntity)
            .addressId(addresEntity.getId())
            .multiAsset(multiAsset)
            .multiAssetId(multiAsset.getId())
            .balance(balance.get())
            .build();
        addressTokens.add(addressToken);
      });
    });

    return addressTokens;
  }
}
