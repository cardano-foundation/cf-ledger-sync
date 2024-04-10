package org.cardanofoundation.ledgersync.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.consumercommon.entity.Epoch;
import org.cardanofoundation.ledgersync.consumercommon.entity.EpochParam;
import org.cardanofoundation.ledgersync.consumercommon.entity.ParamProposal;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.mapper.EpochParamMapper;
import org.cardanofoundation.ledgersync.repository.BlockRepository;
import org.cardanofoundation.ledgersync.repository.EpochParamRepository;
import org.cardanofoundation.ledgersync.repository.EpochRepository;
import org.cardanofoundation.ledgersync.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.service.CostModelService;
import org.cardanofoundation.ledgersync.service.EpochParamService;
import org.cardanofoundation.ledgersync.service.GenesisDataService;
import org.cardanofoundation.ledgersync.util.EpochParamUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class EpochParamServiceImpl implements EpochParamService {

    final BlockRepository blockRepository;
    final ParamProposalRepository paramProposalRepository;
    final EpochParamRepository epochParamRepository;
    final EpochRepository epochRepository;
    final CostModelService costModelService;
    final GenesisDataService genesisDataService;
    final EpochParamMapper epochParamMapper;
    EpochParam defShelleyEpochParam;
    EpochParam defAlonzoEpochParam;
    EpochParam defBabbageEpochParam;
    EpochParam defConwayEpochParam;

    public EpochParamServiceImpl(BlockRepository blockRepository, ParamProposalRepository paramProposalRepository,
                                 EpochParamRepository epochParamRepository, EpochRepository epochRepository,
                                 CostModelService costModelService,
                                 @Lazy GenesisDataService genesisDataService,
                                 EpochParamMapper epochParamMapper) {
        this.blockRepository = blockRepository;
        this.paramProposalRepository = paramProposalRepository;
        this.epochParamRepository = epochParamRepository;
        this.epochRepository = epochRepository;
        this.costModelService = costModelService;
        this.genesisDataService = genesisDataService;
        this.epochParamMapper = epochParamMapper;
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

            curEpochParam.setCostModel(costModelService.getGenesisCostModel());
            curEpochParam.setMinUtxoValue(null);
        }

        if (curEra == EraType.SHELLEY && prevEra == EraType.BYRON) {
            epochParamMapper.updateByEpochParam(curEpochParam, defShelleyEpochParam);
        }

        if (curEra == EraType.ALONZO && prevEra == EraType.MARY) {
            epochParamMapper.updateByEpochParam(curEpochParam, defAlonzoEpochParam);
            curEpochParam.setCostModel(costModelService.getGenesisCostModel());
            curEpochParam.setMinUtxoValue(null);
        }

        if (curEra == EraType.BABBAGE && prevEra == EraType.ALONZO) {
            epochParamMapper.updateByEpochParam(curEpochParam, defBabbageEpochParam);
        }

        if (curEra == EraType.CONWAY && prevEra == EraType.BABBAGE) {
            epochParamMapper.updateByEpochParam(curEpochParam, defConwayEpochParam);
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
