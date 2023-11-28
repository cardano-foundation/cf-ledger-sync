package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.bloxbean.cardano.yaci.core.model.ProtocolParamUpdate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.CostModel;
import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CostModelService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.ParamProposalService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParamProposalServiceImpl implements ParamProposalService {

    ParamProposalRepository paramProposalRepository;
    CostModelService costModelService;

    @Override
    public List<ParamProposal> handleParamProposals(
            Collection<AggregatedTx> successTxs, Map<String, Tx> txMap) {

        successTxs.stream()
                .filter(aggregatedTx -> Objects.nonNull(aggregatedTx.getUpdate()))
                .forEach(costModelService::handleCostModel);

        List<ParamProposal> paramProposals = successTxs.stream()
                .filter(aggregatedTx -> Objects.nonNull(aggregatedTx.getUpdate()))
                .flatMap(aggregatedTx ->
                        handleParamProposal(aggregatedTx, txMap.get(aggregatedTx.getHash())).stream())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(paramProposals)) {
            return Collections.emptyList();
        }

        return paramProposalRepository.saveAll(paramProposals);
    }

    private List<ParamProposal> handleParamProposal(AggregatedTx aggregatedTx, Tx tx) {
        int epochNo = (int) aggregatedTx.getUpdate().getEpoch();

        List<ParamProposal> paramProposals = aggregatedTx
                .getUpdate()
                .getProtocolParamUpdates()
                .entrySet()
                .stream()
                .map(entrySet -> {
                    ProtocolParamUpdate protocolParamUpdate = entrySet.getValue(); //TODO refactor
                    if (Objects.isNull(protocolParamUpdate)) {
                        return null;
                    }

                    var minFreeA = toBigInteger(protocolParamUpdate.getMinFeeA());
                    var minFeeB = toBigInteger(protocolParamUpdate.getMinFeeB());
                    var maxBlockSize = toBigInteger(protocolParamUpdate.getMaxBlockSize());
                    var maxTxSize = toBigInteger(protocolParamUpdate.getMaxTxSize());
                    var maxBhSize = toBigInteger(protocolParamUpdate.getMaxBlockHeaderSize());
                    var keyDeposit = protocolParamUpdate.getKeyDeposit();
                    var optimalPoolCount = toBigInteger(protocolParamUpdate.getNOpt());//toBigInteger(protocolParamUpdate.getOptimalPoolCount()); //TODO refactor
                    var influence = toDouble(protocolParamUpdate.getPoolPledgeInfluence());
                    var monetaryExpandRate = toDouble(protocolParamUpdate.getExpansionRate());
                    var poolDeposit = protocolParamUpdate.getPoolDeposit();
                    var maxEpoch = toBigInteger(protocolParamUpdate.getMaxEpoch());
                    var treasuryGrowthRate = toDouble(protocolParamUpdate.getTreasuryGrowthRate());
                    var decentralisation = toDouble(protocolParamUpdate.getDecentralisationParam());

                    var extraEntropyTuple = protocolParamUpdate.getExtraEntropy();
                    String entropy = null;
                    if (extraEntropyTuple != null) {
                        entropy = extraEntropyTuple._2;
                    }

                    var protocolMajor = protocolParamUpdate.getProtocolMajorVer();
                    var protocolMinor = protocolParamUpdate.getProtocolMinorVer();
                    var minUtxoValue = protocolParamUpdate.getMinUtxo();
                    var minPoolCost = protocolParamUpdate.getMinPoolCost();
                    var coinsPerUtxoSize = protocolParamUpdate.getAdaPerUtxoByte();
//          var costModelRaw = protocolParamUpdate.getCostModels();
                    var costModelsHash = protocolParamUpdate.getCostModelsHash();

                    CostModel costModel = null;
                    if (Objects.nonNull(costModelsHash)) {
                        costModel = costModelService.findCostModelByHash(costModelsHash);
                    }

                    var priceMem = toDouble(protocolParamUpdate.getPriceMem());
                    var priceStep = toDouble(protocolParamUpdate.getPriceStep());
                    var maxTxExMem = protocolParamUpdate.getMaxTxExMem();
                    var maxTxExSteps = protocolParamUpdate.getMaxTxExSteps();
                    var maxBlockExMem = protocolParamUpdate.getMaxBlockExMem();
                    var maxBlockExSteps = protocolParamUpdate.getMaxBlockExSteps();
                    var maxValSize = toBigInteger(protocolParamUpdate.getMaxValSize());

                    var collateralPercent = protocolParamUpdate.getCollateralPercent();
                    var maxCollateralInputs = protocolParamUpdate.getMaxCollateralInputs();

                    return ParamProposal.builder()
                            .key(entrySet.getKey())
                            .epochNo(epochNo)
                            .minFeeA(minFreeA)
                            .minFeeB(minFeeB)
                            .maxBlockSize(maxBlockSize)
                            .maxTxSize(maxTxSize)
                            .maxBhSize(maxBhSize)
                            .keyDeposit(keyDeposit)
                            .optimalPoolCount(optimalPoolCount)
                            .influence(influence)
                            .monetaryExpandRate(monetaryExpandRate)
                            .poolDeposit(poolDeposit)
                            .maxEpoch(maxEpoch)
                            .treasuryGrowthRate(treasuryGrowthRate)
                            .decentralisation(decentralisation)
                            .entropy(entropy)
                            .protocolMajor(protocolMajor)
                            .protocolMinor(protocolMinor)
                            .minUtxoValue(minUtxoValue)
                            .minPoolCost(minPoolCost)
                            .coinsPerUtxoSize(coinsPerUtxoSize)
                            .costModel(costModel)
                            .priceMem(priceMem)
                            .priceStep(priceStep)
                            .maxTxExMem(maxTxExMem)
                            .maxTxExSteps(maxTxExSteps)
                            .maxBlockExMem(maxBlockExMem)
                            .maxBlockExSteps(maxBlockExSteps)
                            .maxValSize(maxValSize)
                            .collateralPercent(collateralPercent)
                            .maxCollateralInputs(maxCollateralInputs)
                            .registeredTx(tx)
                            .build();
                }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(paramProposals)) {
            return Collections.emptyList();
        }

        return new ArrayList<>(paramProposals);
    }


    private BigInteger toBigInteger(Integer integer) {
        return Objects.isNull(integer) ? null : new BigInteger(String.valueOf(integer));
    }

    private BigInteger toBigInteger(Long longVal) {
        return Objects.isNull(longVal) ? null : new BigInteger(String.valueOf(longVal));
    }

    private Double toDouble(BigDecimal decimal) {
        return Objects.isNull(decimal) ? null : decimal.doubleValue();
    }
}
