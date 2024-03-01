package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.Withdrawal;
import org.cardanofoundation.ledgersync.consumercommon.entity.Withdrawal.WithdrawalBuilder;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.WithdrawalRepository;
import org.cardanofoundation.ledgersync.service.WithdrawalsService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WithdrawalsServiceImpl implements WithdrawalsService {

    WithdrawalRepository withdrawalRepository;

    @Override
    public void handleWithdrawal(Collection<AggregatedTx> successTxs,
                                 Map<String, Tx> txMap,
                                 Map<String, StakeAddress> stakeAddressMap,
                                 Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap) {
        List<AggregatedTx> txWithWithdrawalList = successTxs.stream()
                .filter(aggregatedTx -> !CollectionUtils.isEmpty(aggregatedTx.getWithdrawals()))
                .toList();
        if (CollectionUtils.isEmpty(txWithWithdrawalList)) {
            return;
        }

        List<Withdrawal> withdrawals = txWithWithdrawalList.stream()
                .flatMap(aggregatedTx -> {
                    Map<String, BigInteger> withdrawalMap = aggregatedTx.getWithdrawals();
                    String txHash = aggregatedTx.getHash();
                    Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap.get(txHash);
                    Tx tx = txMap.get(txHash);

                    List<Entry<String, BigInteger>> entries = new ArrayList<>(withdrawalMap.entrySet());
                    return IntStream.range(0, entries.size()).mapToObj(idx -> {
                        Entry<String, BigInteger> entry = entries.get(idx);
                        String rewardAccount = entry.getKey();
                        BigInteger amount = entry.getValue();

                        Redeemer redeemer = null;
                        if (!CollectionUtils.isEmpty(redeemerInTxMap)) {
                            redeemer = redeemerInTxMap.get(Pair.of(RedeemerTag.Reward, idx));
                        }

                        StakeAddress rewardAddress = stakeAddressMap.get(rewardAccount);
                        if (Objects.isNull(rewardAddress)) {
                            throw new IllegalStateException(
                                    String.format("Stake address with address hex %s not found", rewardAccount));
                        }
                        return buildWithdrawal(rewardAddress, amount, tx, redeemer);
                    });
                })
                .toList();

        withdrawalRepository.saveAll(withdrawals);
    }

    private Withdrawal buildWithdrawal(
            StakeAddress rewardAddress, BigInteger amount, Tx tx, Redeemer redeemer) {

        WithdrawalBuilder<?, ?> withdrawalBuilder = Withdrawal.builder();

        withdrawalBuilder.addr(rewardAddress);
        withdrawalBuilder.amount(amount);
        withdrawalBuilder.tx(tx);
        withdrawalBuilder.redeemer(redeemer);

        return withdrawalBuilder.build();
    }
}
