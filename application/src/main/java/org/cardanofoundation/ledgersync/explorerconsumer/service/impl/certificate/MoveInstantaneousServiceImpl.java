package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.certificate;

import com.bloxbean.cardano.yaci.core.model.certs.MoveInstataneous;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.explorer.consumercommon.entity.PotTransfer.PotTransferBuilder;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BatchCertificateDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CertificateSyncService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.AddressUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MoveInstantaneousServiceImpl extends CertificateSyncService<MoveInstataneous> {

    BatchCertificateDataService batchCertificateDataService;

    @Override
    public void handle(AggregatedBlock aggregatedBlock,
                       MoveInstataneous certificate, int certificateIdx,
                       Tx tx, Redeemer redeemer, Map<String, StakeAddress> stakeAddressMap) {
        BigInteger potCoin = certificate.getAccountingPotCoin();
        if (Objects.nonNull(potCoin)) {
            insertPotTransfer(certificateIdx, potCoin, certificate.isTreasury(), tx);
        } else {
            insertRewards(aggregatedBlock, certificate, certificateIdx, tx, stakeAddressMap);
        }
    }

    private void insertRewards(AggregatedBlock aggregatedBlock,
                               MoveInstataneous certificate, int certIdx, Tx tx,
                               Map<String, StakeAddress> stakeAddressMap) {
        Map<String, BigInteger> rewardsMap = certificate
                .getStakeCredentialCoinMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> AddressUtil.getRewardAddressString(
                                entry.getKey(), aggregatedBlock.getNetwork()), Entry::getValue));

        List<Reserve> reserves = new ArrayList<>();
        List<Treasury> treasuries = new ArrayList<>();
        rewardsMap.forEach((stakeAddressHex, coin) -> {
            StakeAddress stakeAddress = stakeAddressMap.get(stakeAddressHex);
            if (certificate.isTreasury()) {
                treasuries.add(buildTreasury(stakeAddress, tx, certIdx, coin));
            } else {
                reserves.add(buildReserve(stakeAddress, tx, certIdx, coin));
            }
        });

        if (!CollectionUtils.isEmpty(reserves)) {
            batchCertificateDataService.saveReserves(reserves);
        }

        if (!CollectionUtils.isEmpty(treasuries)) {
            batchCertificateDataService.saveTreasuries(treasuries);
        }
    }

    private Reserve buildReserve(StakeAddress stakeAddress, Tx tx, int certIdx, BigInteger coin) {
        return Reserve.builder()
                .addr(stakeAddress)
                .tx(tx)
                .certIndex(certIdx)
                .amount(coin)
                .build();
    }

    private Treasury buildTreasury(StakeAddress stakeAddress, Tx tx, int certIdx, BigInteger coin) {
        return Treasury.builder()
                .addr(stakeAddress)
                .tx(tx)
                .certIndex(certIdx)
                .amount(coin)
                .build();
    }

    private void insertPotTransfer(int certificateIdx, BigInteger coin, boolean isTreasury, Tx tx) {
        PotTransferBuilder<?, ?> potTransferBuilder = PotTransfer.builder();

        potTransferBuilder.certIndex(certificateIdx);

        if (isTreasury) {
            potTransferBuilder.treasury(coin);
        } else {
            potTransferBuilder.reserves(coin);
        }

        potTransferBuilder.tx(tx);
        batchCertificateDataService.savePotTransfer(potTransferBuilder.build());
    }
}
