package org.cardanofoundation.ledgersync.aggregate.account.service.impl;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.constant.Constants;
import org.cardanofoundation.ledgersync.aggregate.account.model.StakeAddress;
import org.cardanofoundation.ledgersync.aggregate.account.repository.StakeAddressRepository;
import org.cardanofoundation.ledgersync.aggregate.account.service.StakeAddressService;
import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddress;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StakeAddressServiceImpl implements StakeAddressService {

    StakeAddressRepository stakeAddressRepository;

    @Override
    public Map<String, StakeAddress> handleStakeAddressesFromTxs(
            Map<String, String> stakeAddressTxHashMap) {
        Set<String> stakeAddressesHex = stakeAddressTxHashMap.keySet();
        Map<String, StakeAddress> stakeAddresses = findStakeAddressByHashRawIn(stakeAddressesHex)
                .parallelStream()
                .collect(Collectors.toConcurrentMap(StakeAddress::getHashRaw, Function.identity()));
        List<StakeAddress> newStakeAddresses = new ArrayList<>();

        /*
         * For each stake address and its first appeared tx hash, check if the stake address
         * was existed before. If not, create a new stake address record and save it
         */
        stakeAddressTxHashMap.forEach((stakeAddressHex, txHash) -> {
            StakeAddress stakeAddress = stakeAddresses.get(stakeAddressHex);
            if (Objects.isNull(stakeAddress)) {
                byte[] addressBytes = HexUtil.decodeHexString(stakeAddressHex);
                ShelleyAddress shelleyAddress = new ShelleyAddress(addressBytes);
                StakeAddress newStakeAddress = buildStakeAddress(shelleyAddress);
                stakeAddresses.put(stakeAddressHex, newStakeAddress);
                newStakeAddresses.add(newStakeAddress);
            }
        });

        // Save new stake address records
        stakeAddressRepository.saveAll(newStakeAddresses);
        return stakeAddresses;
    }

    @Override
    public Collection<StakeAddress> findStakeAddressByHashRawIn(Collection<String> hashRaw) {
        Collection<StakeAddress> stakeAddressCollection = new ConcurrentLinkedQueue<>();

        var queryBatches = Lists.partition(new ArrayList<>(hashRaw), Constants.BATCH_QUERY_SIZE);
        queryBatches.forEach(batch -> {
            List<StakeAddress> stakeAddresses = stakeAddressRepository.findByHashRawIn(batch);
            stakeAddresses.parallelStream().forEach(stakeAddressCollection::add);
        });
        return stakeAddressCollection;
    }

    private StakeAddress buildStakeAddress(ShelleyAddress address) {
        String stakeReference = HexUtil.encodeHexString(address.getStakeReference());

        StakeAddress.StakeAddressBuilder<?, ?> stakeAddressBuilder = StakeAddress.builder()
                //.tx(tx)
                .hashRaw(stakeReference)
                .view(address.getAddress());

        if (address.hasScriptHashReference()) {
            String scriptHash = stakeReference.substring(2);
            stakeAddressBuilder.scriptHash(scriptHash);
        }

        stakeAddressBuilder.balance(BigInteger.ZERO);
        stakeAddressBuilder.availableReward(BigInteger.ZERO);

        return stakeAddressBuilder.build();
    }
}
