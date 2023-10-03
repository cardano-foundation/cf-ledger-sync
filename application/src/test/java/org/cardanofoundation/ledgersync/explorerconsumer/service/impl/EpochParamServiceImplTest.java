package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.util.List;
import java.util.Optional;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.Epoch;
import org.cardanofoundation.explorer.consumercommon.entity.EpochParam;
import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;
import org.cardanofoundation.explorer.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.mapper.EpochParamMapper;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochParamRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.EpochRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.CostModelService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.EpochParamServiceImpl;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpochParamServiceImplTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void setHandleEpochParamsLastGreaterThanMaxSlot() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    EpochParamMapper epochParamMapper = Mockito.mock(EpochParamMapper.class);
    EpochParam defShelleyEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defAlonzoEpochParam = Mockito.mock(EpochParam.class);
    Epoch epoch = Mockito.mock(Epoch.class);
    EpochParam mockEpochParam = Mockito.mock(EpochParam.class);
    Mockito.when(mockEpochParam.getEpochNo()).thenReturn(112);
    Mockito.when(epochParamRepository.findLastEpochParam()).thenReturn(Optional.of(mockEpochParam));
    List<Epoch> epoches = List.of(epoch);
    Mockito.when(epoch.getNo()).thenReturn(111);
    Mockito.when(epoch.getMaxSlot()).thenReturn(432000);
    Mockito.when(epochRepository.findAll()).thenReturn(epoches);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository, costModelService,
        epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams(Constant.MAINNET);
  }

  @Test
  void setDefShelleyEpochParamException() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    EpochParamMapper epochParamMapper = Mockito.mock(EpochParamMapper.class);
    EpochParam defShelleyEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defAlonzoEpochParam = Mockito.mock(EpochParam.class);
    Epoch epoch = Mockito.mock(Epoch.class);
    EpochParam mockEpochParam = Mockito.mock(EpochParam.class);
    Mockito.when(mockEpochParam.getEpochNo()).thenReturn(12);
    Mockito.when(epochParamRepository.findLastEpochParam()).thenReturn(Optional.of(mockEpochParam));
    Mockito.when(epoch.getNo()).thenReturn(34);
    Mockito.when(epoch.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch.getEra()).thenReturn(EraType.valueOf(EraType.SHELLEY.getValue()));
    Mockito.when(epochRepository.findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            Mockito.eq(ConsumerConstant.FIVE_DAYS), Mockito.anyInt()))
        .thenReturn(List.of(34));

    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Mockito.when(epochRepository.findEpochByNo(34)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(33)).thenReturn(optionalEpoch);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository,
        costModelService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);
    Assertions.assertThrows(RuntimeException.class,
        () -> epochParamServiceImpl.handleEpochParams(any(Integer.class)));
    Mockito.verify(epochParamRepository, Mockito.times(0)).save(any());
  }

  @Test
  void setDefShelleyEpochParamEraShelley() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    EpochParamMapper epochParamMapper = Mockito.mock(EpochParamMapper.class);
    EpochParam defShelleyEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defAlonzoEpochParam = Mockito.mock(EpochParam.class);
    Epoch epoch = Mockito.mock(Epoch.class);
    Epoch epoch2 = Mockito.mock(Epoch.class);
    EpochParam mockEpochParam = Mockito.mock(EpochParam.class);
    Mockito.when(mockEpochParam.getEpochNo()).thenReturn(2);
    Mockito.when(epochParamRepository.findLastEpochParam()).thenReturn(Optional.of(mockEpochParam));
    Optional<EpochParam> prevEpochParam = Optional.of(defAlonzoEpochParam);
    Mockito.when(epochParamRepository.findEpochParamByEpochNo(2))
        .thenReturn(prevEpochParam);
    Mockito.when(epoch.getNo()).thenReturn(3);
    Mockito.when(epoch.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch.getEra()).thenReturn(EraType.valueOf(EraType.SHELLEY.getValue()));
    Mockito.when(epoch2.getNo()).thenReturn(2);
    Mockito.when(epoch2.getMaxSlot()).thenReturn(ConsumerConstant.BYRON_SLOT);
    Mockito.when(epoch2.getEra()).thenReturn(EraType.valueOf(EraType.BYRON.getValue()));
    Mockito.when(epochRepository.findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            Mockito.eq(ConsumerConstant.FIVE_DAYS), Mockito.anyInt()))
        .thenReturn(List.of(3));
    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Optional<Epoch> optionalEpoch2 = Optional.of(epoch2);

    Block cachedBlock = Mockito.mock(Block.class);
    Optional<Block> optionalBlock = Optional.of(cachedBlock);
    Mockito.when(epochRepository.findEpochByNo(3)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(2)).thenReturn(optionalEpoch2);
    Mockito.when(blockRepository.findFirstByEpochNo(3)).thenReturn(optionalBlock);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository,
        costModelService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams(Constant.MAINNET);
    Mockito.verify(epochParamRepository, Mockito.times(1)).save(any());
    Mockito.verify(epochParamMapper, Mockito.times(2))
        .updateByEpochParam(any(), any());
  }

  @Test
  void setDefShelleyEpochParamEraAlonzo() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    EpochParamMapper epochParamMapper = Mockito.mock(EpochParamMapper.class);
    EpochParam defShelleyEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defAlonzoEpochParam = Mockito.mock(EpochParam.class);
    Epoch epoch = Mockito.mock(Epoch.class);
    Epoch epoch2 = Mockito.mock(Epoch.class);
    EpochParam mockEpochParam = Mockito.mock(EpochParam.class);
    Mockito.when(mockEpochParam.getEpochNo()).thenReturn(2);
    Mockito.when(epochParamRepository.findLastEpochParam()).thenReturn(Optional.of(mockEpochParam));
    Optional<EpochParam> prevEpochParam = Optional.of(defAlonzoEpochParam);
    Mockito.when(epochParamRepository.findEpochParamByEpochNo(4))
        .thenReturn(prevEpochParam);
    Mockito.when(epoch.getNo()).thenReturn(5);
    Mockito.when(epoch.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch.getEra()).thenReturn(EraType.valueOf(EraType.ALONZO.getValue()));
    Mockito.when(epoch2.getNo()).thenReturn(4);
    Mockito.when(epoch2.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch2.getEra()).thenReturn(EraType.valueOf(EraType.MARY.getValue()));
    Mockito.when(epochRepository.findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            Mockito.eq(ConsumerConstant.FIVE_DAYS), Mockito.anyInt()))
        .thenReturn(List.of(5));
    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Optional<Epoch> optionalEpoch2 = Optional.of(epoch2);

    ParamProposal paramProposal = Mockito.mock(ParamProposal.class);
    List<ParamProposal> prevParamProposals = List.of(paramProposal);
    Mockito.when(paramProposalRepository.findParamProposalsByEpochNo(4))
        .thenReturn(prevParamProposals);
    Block cachedBlock = Mockito.mock(Block.class);
    Optional<Block> optionalBlock = Optional.of(cachedBlock);
    Mockito.when(epochRepository.findEpochByNo(5)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(4)).thenReturn(optionalEpoch2);
    Mockito.when(blockRepository.findFirstByEpochNo(5)).thenReturn(optionalBlock);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository,
        costModelService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams(Constant.MAINNET);
    Mockito.verify(epochParamRepository, Mockito.times(1)).save(any());
    Mockito.verify(epochParamMapper, Mockito.times(2))
        .updateByEpochParam(any(), any());
    Mockito.verify(epochParamMapper, Mockito.times(1))
        .updateByParamProposal(any(), any());
    Mockito.verify(costModelService, Mockito.times(1)).getGenesisCostModel();
  }
}