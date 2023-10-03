package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.explorer.consumercommon.entity.EpochParam;
import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;
import org.cardanofoundation.explorer.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.explorerconsumer.mapper.EpochParamMapper;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochParamRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CostModelService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.EpochParamService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant.getShelleyEpochLength;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class EpochParamServiceImpl implements EpochParamService {

    final BlockRepository blockRepository;
    final ParamProposalRepository paramProposalRepository;
    final EpochParamRepository epochParamRepository;
    final EpochRepository epochRepository;
    final CostModelService costModelService;
    final EpochParamMapper epochParamMapper;

    EpochParam defShelleyEpochParam;
    EpochParam defAlonzoEpochParam;
    EpochParam defBabbageEpochParam;

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
    public void handleEpochParams(int networkMagic) {
        Integer lastEpochParam = epochParamRepository.findLastEpochParam()
                .map(EpochParam::getEpochNo)
                .orElse(0);

        epochRepository
                .findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
                        getShelleyEpochLength(networkMagic), lastEpochParam)
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

        if (curEra == EraType.BYRON || prevEra == null) {
            return;
        }
        log.info("Handling epoch param for epoch: {}", epochNo);

        Optional<EpochParam> prevEpochParam = epochParamRepository.findEpochParamByEpochNo(epochNo - 1);

        EpochParam curEpochParam = new EpochParam();

        prevEpochParam.ifPresent(
                epochParam -> epochParamMapper.updateByEpochParam(curEpochParam, epochParam));

        // setting for preview network and other network start with alonzo in future
        if (prevEra.equals(EraType.ALONZO)) {
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

        List<ParamProposal> prevParamProposals = paramProposalRepository
                .findParamProposalsByEpochNo(epochNo - 1);
        prevParamProposals.forEach(
                paramProposal -> epochParamMapper.updateByParamProposal(curEpochParam, paramProposal));

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
