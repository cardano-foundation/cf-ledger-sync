package org.cardanofoundation.ledgersync.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.mapper.EpochParamMapper;
import org.cardanofoundation.ledgersync.repository.*;
import org.cardanofoundation.ledgersync.service.CostModelService;
import org.cardanofoundation.ledgersync.service.EpochParamService;
import org.cardanofoundation.ledgersync.service.GenesisDataService;
import org.cardanofoundation.ledgersync.service.impl.plutus.PlutusKey;
import org.cardanofoundation.ledgersync.util.EpochParamUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class EpochParamServiceImpl implements EpochParamService {

    final BlockRepository blockRepository;
    final ParamProposalRepository paramProposalRepository;
    final EpochParamRepository epochParamRepository;
    final EpochRepository epochRepository;
    final CostModelRepository costModelRepository;
//    final CostModelService costModelService;
    final GenesisDataService genesisDataService;
    final EpochParamMapper epochParamMapper;
    final ObjectMapper objectMapper;
    EpochParam defShelleyEpochParam;
    EpochParam defAlonzoEpochParam;
    EpochParam defBabbageEpochParam;
    EpochParam defConwayEpochParam;

    public EpochParamServiceImpl(BlockRepository blockRepository, ParamProposalRepository paramProposalRepository,
                                 EpochParamRepository epochParamRepository, EpochRepository epochRepository,
                                 CostModelRepository costModelRepository,
                                 @Lazy GenesisDataService genesisDataService,
                                 EpochParamMapper epochParamMapper, ObjectMapper objectMapper) {
        this.blockRepository = blockRepository;
        this.paramProposalRepository = paramProposalRepository;
        this.epochParamRepository = epochParamRepository;
        this.epochRepository = epochRepository;
        this.costModelRepository = costModelRepository;
        this.genesisDataService = genesisDataService;
        this.epochParamMapper = epochParamMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void setDefShelleyEpochParam(EpochParam defShelleyEpochParam) {
        this.defShelleyEpochParam = defShelleyEpochParam;
    }

    @Override
    public void setDefAlonzoEpochParam(EpochParam defAlonzoEpochParam) {
        this.defAlonzoEpochParam = defAlonzoEpochParam;
    }

    @Override
    public void setDefBabbageEpochParam(EpochParam defBabbageEpochParam) {
        this.defBabbageEpochParam = defBabbageEpochParam;
    }

    @Override
    public void setDefConwayEpochParam(EpochParam defConwayEpochParam) {
        this.defConwayEpochParam = defConwayEpochParam;
    }

    @Override
    public void handleEpochParams() {
        Integer lastEpochParam = epochParamRepository.findLastEpochParam()
                .map(EpochParam::getEpochNo)
                .orElse(-1);

        epochRepository
                .findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
                        genesisDataService.getShelleyEpochLength(), lastEpochParam)
                .forEach(this::handleEpochParam);
    }

    /**
     * Handle epoch param for epochNo.
     *
     * @param epochNo
     */
    @SneakyThrows
    void handleEpochParam(int epochNo) {
        EraType curEra = getEra(epochNo);
        EraType prevEra = getEra(epochNo - BigInteger.ONE.intValue());

        if (curEra == EraType.BYRON) {
            return;
        }
        log.info("Handling epoch param for epoch: {}", epochNo);

        Optional<EpochParam> prevEpochParam = epochParamRepository.findEpochParamByEpochNo(epochNo - 1);

        EpochParam curEpochParam = new EpochParam();

        prevEpochParam.ifPresent(
                epochParam -> epochParamMapper.updateByEpochParam(curEpochParam, epochParam));

        // setting for preview network and other network start with alonzo in future
        if (curEra == EraType.ALONZO && prevEra == null) {
            epochParamMapper.updateByEpochParam(curEpochParam, defShelleyEpochParam);
            epochParamMapper.updateByEpochParam(curEpochParam, defAlonzoEpochParam);
            costModelRepository.save(defAlonzoEpochParam.getCostModel());
            curEpochParam.setMinUtxoValue(null);
        }

        if (curEra == EraType.SHELLEY && prevEra == EraType.BYRON) {
            epochParamMapper.updateByEpochParam(curEpochParam, defShelleyEpochParam);
        }

        if (curEra == EraType.ALONZO && prevEra == EraType.MARY) {
            epochParamMapper.updateByEpochParam(curEpochParam, defAlonzoEpochParam);
            costModelRepository.save(defAlonzoEpochParam.getCostModel());
            curEpochParam.setMinUtxoValue(null);
        }

        if (curEra == EraType.BABBAGE && prevEra == EraType.ALONZO) {
            epochParamMapper.updateByEpochParam(curEpochParam, defBabbageEpochParam);
        }

        if (curEra == EraType.CONWAY && prevEra == EraType.BABBAGE) {
            epochParamMapper.updateByEpochParam(curEpochParam, defConwayEpochParam);
            var genesisConwayCostModel = defConwayEpochParam.getCostModel();
            var currentCostModel = CostModel.builder()
                    .hash(genesisConwayCostModel.getHash())
                    .build();
            CostModel prevCostModel = prevEpochParam.map(EpochParam::getCostModel).orElse(null);
            if (prevCostModel != null) {
                // merge prev costs into genesis conway cost model
                Map<String, Object> costs = objectMapper.readValue(prevCostModel.getCosts(), new TypeReference<>() {
                });
                Map<String, Object> genesisConwayCosts = objectMapper.readValue(genesisConwayCostModel.getCosts(), new TypeReference<>() {
                });

                Map<String, Object> mergedCosts = new HashMap<>();

                costs.forEach((key, value) -> {
                    if (!mergedCosts.containsKey(key)) {
                        mergedCosts.put(key, value);
                    }
                });
                mergedCosts.put(PlutusKey.PLUTUS_V3.value, genesisConwayCosts.get(PlutusKey.PLUTUS_V3.value));

                currentCostModel.setCosts(objectMapper.writeValueAsString(mergedCosts));
            } else {
                currentCostModel.setCosts(genesisConwayCostModel.getCosts());
            }

            costModelRepository.save(currentCostModel);
            curEpochParam.setCostModel(currentCostModel);
//            costModelService.setGenesisCostModel(PlutusKey.PLUTUS_V3, defConwayEpochParam.getCostModel());
//            curEpochParam.setCostModel(costModelService.getGenesisCostModel(PlutusKey.PLUTUS_V3));
        }

        List<ParamProposal> prevParamProposals = paramProposalRepository
                .findParamProposalsByEpochNo(epochNo - 1);

        var paramProposalToUpdate = EpochParamUtil.getParamProposalToUpdate(prevParamProposals, genesisDataService.getDelegationKeyHashes(),
                genesisDataService.getUpdateQuorum());
        if (paramProposalToUpdate != null) {
            epochParamMapper.updateByParamProposal(curEpochParam, paramProposalToUpdate);
        }

        Block block = blockRepository.findFirstByEpochNo(epochNo)
                .orElseThrow(
                        () -> new RuntimeException("Block not found for epoch: " + epochNo));
        curEpochParam.setEpochNo(epochNo);
        curEpochParam.setBlock(block);
        epochParamRepository.save(curEpochParam);
    }

    /**
     * Get era by epoch
     *
     * @param epochNo epoch
     * @return eraType
     */
    private EraType getEra(int epochNo) {
        return epochRepository.findEpochByNo(epochNo)
                .map(Epoch::getEra)
                .orElse(null);
    }

}
