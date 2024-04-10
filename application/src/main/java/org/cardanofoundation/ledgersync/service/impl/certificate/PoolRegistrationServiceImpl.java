package org.cardanofoundation.ledgersync.service.impl.certificate;

import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.yaci.core.model.PoolParams;
import com.bloxbean.cardano.yaci.core.model.Relay;
import com.bloxbean.cardano.yaci.core.model.certs.PoolRegistration;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.consumercommon.entity.PoolRelay.PoolRelayBuilder;
import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.service.CertificateSyncService;
import org.cardanofoundation.ledgersync.util.AddressUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PoolRegistrationServiceImpl extends CertificateSyncService<PoolRegistration> {

    BatchCertificateDataService batchCertificateDataService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       PoolRegistration certificate, int certificateIdx,
                       Tx tx, Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        var hexBytes = certificate.getPoolParams().getOperator();
        PoolHash poolHash = getOrInsertPoolHash(hexBytes, aggregatedBlock.getEpochNo());
        PoolMetadataRef poolMetadata = insertPoolMetadataRef(certificate.getPoolParams(), poolHash, tx);
        int epochActivationDelay = getEpochActivationDelay(poolHash);
        int activeEpochNo = epochActivationDelay + aggregatedBlock.getEpochNo();

        // Workaround for bug https://github.com/input-output-hk/cardano-db-sync/issues/546
        String rewardAccountHex = certificate.getPoolParams().getRewardAccount();
        byte[] rewardAccountBytes = HexUtil.decodeHexString(rewardAccountHex);
        int networkId = Constant.isTestnet(aggregatedBlock.getNetwork()) ? 0 : 1;
        byte header = rewardAccountBytes[0];
        if (((header & 0xff) & networkId) == 0) {
            rewardAccountBytes[0] = (byte) ((header & ~1) | networkId);
        }

        rewardAccountHex = HexUtil.encodeHexString(rewardAccountBytes);
        StakeAddress rewardAccount = stakeAddressMap.get(rewardAccountHex);
        PoolUpdate poolUpdate = insertPoolUpdate(certificate.getPoolParams(),
                activeEpochNo, certificateIdx, poolHash, rewardAccount, poolMetadata, tx);
        insertPoolOwners(aggregatedBlock.getNetwork(),
                certificate.getPoolParams(), poolUpdate, stakeAddressMap);
        insertPoolRelays(certificate.getPoolParams(), poolUpdate);
    }

    private int getEpochActivationDelay(PoolHash poolHash) {
        Boolean otherUpdateExist = batchCertificateDataService.poolUpdateExistsByPoolHash(poolHash);

        // If the pool is first registered, return 2, else 3
        // This method is currently not accurate. Real world case requires
        // communicating with node. Will be re-implemented after implementing
        // local state query mini-protocol
        return Boolean.FALSE.equals(otherUpdateExist) ? 2 : 3;
    }

    // Insert new or get pool hash from DB
    private PoolHash getOrInsertPoolHash(String hexBytes, int epochNo) {
        Optional<PoolHash> poolHash = batchCertificateDataService.findPoolHashByHashRaw(hexBytes);

        // This part will only save this entity to cache layer for fast future use
        poolHash.ifPresent(batchCertificateDataService::savePoolHash);

        return poolHash.orElseGet(() -> {
            PoolHash newPoolHash = buildPoolHash(hexBytes, epochNo);
            return batchCertificateDataService.savePoolHash(newPoolHash);
        });
    }

    private PoolMetadataRef insertPoolMetadataRef(PoolParams poolParams, PoolHash poolHash, Tx tx) {
        if (!StringUtils.hasText(poolParams.getPoolMetadataUrl())) {
            return null;
        }

        Optional<PoolMetadataRef> poolMetadataRefOptional = batchCertificateDataService
                .findPoolMetadataRefByPoolHashAndUrlAndHash(
                        poolHash, poolParams.getPoolMetadataUrl(), poolParams.getPoolMetadataHash());

        // This part will only save this entity to cache layer for fast future use
        poolMetadataRefOptional.ifPresent(batchCertificateDataService::savePoolMetadataRef);

        return poolMetadataRefOptional.orElseGet(() -> {
            PoolMetadataRef poolMetadataRef = buildPoolMetadataRef(poolParams, poolHash, tx);
            return batchCertificateDataService.savePoolMetadataRef(poolMetadataRef);
        });
    }

    private PoolUpdate insertPoolUpdate(PoolParams poolParams,
                                        int activeEpochNo, int certificateIdx, PoolHash poolHash,
                                        StakeAddress rewardAccount, PoolMetadataRef poolMetadata,
                                        Tx tx) {
        PoolUpdate poolUpdate = PoolUpdate.builder()
                .poolHash(poolHash)
                .certIndex(certificateIdx)
                .vrfKeyHash(poolParams.getVrfKeyHash())
                .pledge(poolParams.getPledge())
                .rewardAddr(rewardAccount)
                .activeEpochNo(activeEpochNo)
                .meta(poolMetadata)
                .margin(parseMargin(poolParams.getMargin()))
                .fixedCost(poolParams.getCost())
                .registeredTx(tx)
                .build();

        return batchCertificateDataService.savePoolUpdate(poolUpdate);
    }

    private void insertPoolOwners(int network, PoolParams poolParams,
                                  PoolUpdate poolUpdate, Map<String, StakeAddress> stakeAddressMap) {
        List<PoolOwner> poolOwners = poolParams.getPoolOwners().stream()
                .map(poolOwnerHash -> {
                    String stakeAddressHex = AddressUtil.getRewardAddressString(
                            new StakeCredential(StakeCredType.ADDR_KEYHASH, poolOwnerHash), network);
                    StakeAddress stakeAddress = stakeAddressMap.get(stakeAddressHex);
                    return buildPoolOwner(stakeAddress, poolUpdate);
                }).toList();

        batchCertificateDataService.savePoolOwners(poolOwners);
    }

    private static PoolHash buildPoolHash(String hexBytes, int epochNo) {
        PoolHash newPoolHash = new PoolHash();
        newPoolHash.setHashRaw(hexBytes);
        newPoolHash.setView(
                Bech32.encode(HexUtil.decodeHexString(hexBytes), ConsumerConstant.POOL_HASH_PREFIX));
        newPoolHash.setPoolSize(BigInteger.ZERO); //TODO hardcode pool size
        newPoolHash.setEpochNo(epochNo);//TODO verify
        return newPoolHash;
    }

    private static PoolMetadataRef buildPoolMetadataRef(
            PoolParams poolParams, PoolHash poolHash, Tx tx) {
        return PoolMetadataRef.builder()
                .poolHash(poolHash)
                .url(poolParams.getPoolMetadataUrl())
                .hash(poolParams.getPoolMetadataHash())
                .registeredTx(tx)
                .build();
    }

    private PoolOwner buildPoolOwner(StakeAddress poolOwnerStakeAddress, PoolUpdate poolUpdate) {
        return PoolOwner.builder()
                .stakeAddress(poolOwnerStakeAddress)
                .poolUpdate(poolUpdate)
                .build();
    }

    private void insertPoolRelays(PoolParams poolParams, PoolUpdate poolUpdate) {
        List<PoolRelay> poolRelays = poolParams.getRelays().stream()
                .map(relay -> buildPoolRelay(relay, poolUpdate))
                .collect(Collectors.toList());

        batchCertificateDataService.savePoolRelays(poolRelays);
    }

    private PoolRelay buildPoolRelay(Relay relay, PoolUpdate poolUpdate) {
        PoolRelayBuilder<?, ?> poolRelayBuilder = PoolRelay.builder();

        poolRelayBuilder.poolUpdate(poolUpdate);

        poolRelayBuilder.port(Objects.nonNull(relay.getPort()) ? relay.getPort() : null);
        poolRelayBuilder.ipv4(StringUtils.hasText(relay.getIpv4()) ? relay.getIpv4() : null);
        poolRelayBuilder.ipv6(StringUtils.hasText(relay.getIpv6()) ? relay.getIpv6() : null);

        poolRelayBuilder.port(Objects.nonNull(relay.getPort()) ? relay.getPort() : null);
        poolRelayBuilder.dnsName(relay.getDnsName());

        poolRelayBuilder.dnsSrvName(relay.getDnsName());

        //TODO -- Check for accuracy
//    switch (relay.getRelayType()) {
//      case SINGLE_HOST_ADDR -> {
//        poolRelayBuilder.port(Objects.nonNull(relay.getPort()) ? relay.getPort() : null);
//        poolRelayBuilder.ipv4(StringUtils.hasText(relay.getIpv4()) ? relay.getIpv4() : null);
//        poolRelayBuilder.ipv6(StringUtils.hasText(relay.getIpv6()) ? relay.getIpv6() : null);
//      }
//      case SINGLE_HOST_NAME -> {
//        poolRelayBuilder.port(Objects.nonNull(relay.getPort()) ? relay.getPort() : null);
//        poolRelayBuilder.dnsName(relay.getDnsName());
//      }
//      case MULTI_HOST_NAME -> poolRelayBuilder.dnsSrvName(relay.getDnsName());
//    }

        return poolRelayBuilder.build();
    }

    private Double parseMargin(String margin) {
        String[] values = margin.split("/");
        double divider = Double.parseDouble(values[0]);
        double dividend = Double.parseDouble(values[1]);
        return divider / dividend;
    }
}
