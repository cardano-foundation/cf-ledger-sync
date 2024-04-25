package org.cardanofoundation.ledgersync.service.impl;

import org.cardanofoundation.ledgersync.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.consumercommon.entity.Epoch;
import org.cardanofoundation.ledgersync.consumercommon.entity.EpochParam;
import org.cardanofoundation.ledgersync.consumercommon.entity.ParamProposal;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.mapper.EpochParamMapper;
import org.cardanofoundation.ledgersync.repository.BlockRepository;
import org.cardanofoundation.ledgersync.repository.EpochParamRepository;
import org.cardanofoundation.ledgersync.repository.EpochRepository;
import org.cardanofoundation.ledgersync.repository.ParamProposalRepository;
import org.cardanofoundation.ledgersync.service.CostModelService;
import org.cardanofoundation.ledgersync.service.GenesisDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

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
    GenesisDataService genesisDataService = Mockito.mock(GenesisDataService.class);
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
        paramProposalRepository, epochParamRepository, epochRepository, costModelService, genesisDataService,
        epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams();

  }

  @Test
  void setDefShelleyEpochParamException() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    GenesisDataService genesisDataService = Mockito.mock(GenesisDataService.class);
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
    Mockito.when(genesisDataService.getShelleyEpochLength()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epochRepository.findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
            Mockito.eq(ConsumerConstant.FIVE_DAYS), Mockito.anyInt()))
        .thenReturn(List.of(34));

    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Mockito.when(epochRepository.findEpochByNo(34)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(33)).thenReturn(optionalEpoch);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository,
        costModelService, genesisDataService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);
    Assertions.assertThrows(RuntimeException.class,
            epochParamServiceImpl::handleEpochParams);
    Mockito.verify(epochParamRepository, Mockito.times(0)).save(any());
  }

  @Test
  void setDefShelleyEpochParamEraShelley() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    GenesisDataService genesisDataService = Mockito.mock(GenesisDataService.class);
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
    Mockito.when(genesisDataService.getShelleyEpochLength()).thenReturn(ConsumerConstant.FIVE_DAYS);
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
        costModelService, genesisDataService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams();
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
    GenesisDataService genesisDataService = Mockito.mock(GenesisDataService.class);
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
    Mockito.when(genesisDataService.getShelleyEpochLength()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Optional<Epoch> optionalEpoch2 = Optional.of(epoch2);

    Mockito.when(genesisDataService.getUpdateQuorum()).thenReturn(4);
    Mockito.when(genesisDataService.getDelegationKeyHashes()).thenReturn(Set.of(
            "637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b",
            "8a4b77c4f534f8b8cc6f269e5ebb7ba77fa63a476e50e05e66d7051c",
            "b00470cd193d67aac47c373602fccd4195aad3002c169b5570de1126",
            "b260ffdb6eba541fcf18601923457307647dce807851b9d19da133ab"
    ));

    ParamProposal paramProposal1 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal2 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal3 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal4 = Mockito.mock(ParamProposal.class);

    Mockito.when(paramProposal1.getKey()).thenReturn("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b");
    Mockito.when(paramProposal2.getKey()).thenReturn("8a4b77c4f534f8b8cc6f269e5ebb7ba77fa63a476e50e05e66d7051c");
    Mockito.when(paramProposal3.getKey()).thenReturn("b00470cd193d67aac47c373602fccd4195aad3002c169b5570de1126");
    Mockito.when(paramProposal4.getKey()).thenReturn("b260ffdb6eba541fcf18601923457307647dce807851b9d19da133ab");

    List<ParamProposal> prevParamProposals = List.of(paramProposal1, paramProposal2, paramProposal3, paramProposal4);
    Mockito.when(paramProposalRepository.findParamProposalsByEpochNo(4))
        .thenReturn(prevParamProposals);
    Block cachedBlock = Mockito.mock(Block.class);
    Optional<Block> optionalBlock = Optional.of(cachedBlock);
    Mockito.when(epochRepository.findEpochByNo(5)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(4)).thenReturn(optionalEpoch2);
    Mockito.when(blockRepository.findFirstByEpochNo(5)).thenReturn(optionalBlock);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
        paramProposalRepository, epochParamRepository, epochRepository,
        costModelService, genesisDataService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);

    epochParamServiceImpl.handleEpochParams();
    Mockito.verify(epochParamRepository, Mockito.times(1)).save(any());
    Mockito.verify(epochParamMapper, Mockito.times(2))
        .updateByEpochParam(any(), any());
    Mockito.verify(epochParamMapper, Mockito.times(1))
        .updateByParamProposal(any(), any());
    Mockito.verify(costModelService, Mockito.times(1)).getGenesisCostModel();
  }

  @Test
  void setDefShelleyEpochParamEraConway() {
    BlockRepository blockRepository = Mockito.mock(BlockRepository.class);
    ParamProposalRepository paramProposalRepository = Mockito.mock(ParamProposalRepository.class);
    EpochParamRepository epochParamRepository = Mockito.mock(EpochParamRepository.class);
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    CostModelService costModelService = Mockito.mock(CostModelService.class);
    GenesisDataService genesisDataService = Mockito.mock(GenesisDataService.class);
    EpochParamMapper epochParamMapper = Mockito.mock(EpochParamMapper.class);

    EpochParam defShelleyEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defAlonzoEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defBabbageEpochParam = Mockito.mock(EpochParam.class);
    EpochParam defConwayEpochParam = Mockito.mock(EpochParam.class);

    Epoch epoch = Mockito.mock(Epoch.class);
    Epoch epoch2 = Mockito.mock(Epoch.class);
    EpochParam mockEpochParam = Mockito.mock(EpochParam.class);

    Mockito.when(mockEpochParam.getEpochNo()).thenReturn(4);
    Mockito.when(epochParamRepository.findLastEpochParam()).thenReturn(Optional.of(mockEpochParam));

    Optional<EpochParam> prevEpochParam = Optional.of(defBabbageEpochParam);
    Mockito.when(epochParamRepository.findEpochParamByEpochNo(4))
            .thenReturn(prevEpochParam);
    Mockito.when(epoch.getNo()).thenReturn(5);
    Mockito.when(epoch.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch.getEra()).thenReturn(EraType.valueOf(EraType.CONWAY.getValue()));
    Mockito.when(epoch2.getNo()).thenReturn(4);
    Mockito.when(epoch2.getMaxSlot()).thenReturn(ConsumerConstant.FIVE_DAYS);
    Mockito.when(epoch2.getEra()).thenReturn(EraType.valueOf(EraType.BABBAGE.getValue()));
    Mockito.when(epochRepository.findEpochNoByMaxSlotAndEpochNoMoreThanLastEpochParam(
                    Mockito.eq(ConsumerConstant.FIVE_DAYS), Mockito.anyInt()))
            .thenReturn(List.of(5));
    Mockito.when(genesisDataService.getShelleyEpochLength()).thenReturn(ConsumerConstant.FIVE_DAYS);

    Optional<Epoch> optionalEpoch = Optional.of(epoch);
    Optional<Epoch> optionalEpoch2 = Optional.of(epoch2);

    Mockito.when(genesisDataService.getUpdateQuorum()).thenReturn(4);
    Mockito.when(genesisDataService.getDelegationKeyHashes()).thenReturn(Set.of(
            "637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b",
            "8a4b77c4f534f8b8cc6f269e5ebb7ba77fa63a476e50e05e66d7051c",
            "b00470cd193d67aac47c373602fccd4195aad3002c169b5570de1126",
            "b260ffdb6eba541fcf18601923457307647dce807851b9d19da133ab"
    ));

    ParamProposal paramProposal1 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal2 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal3 = Mockito.mock(ParamProposal.class);
    ParamProposal paramProposal4 = Mockito.mock(ParamProposal.class);

    Mockito.when(paramProposal1.getKey()).thenReturn("637f2e950b0fd8f8e3e811c5fbeb19e411e7a2bf37272b84b29c1a0b");
    Mockito.when(paramProposal2.getKey()).thenReturn("8a4b77c4f534f8b8cc6f269e5ebb7ba77fa63a476e50e05e66d7051c");
    Mockito.when(paramProposal3.getKey()).thenReturn("b00470cd193d67aac47c373602fccd4195aad3002c169b5570de1126");
    Mockito.when(paramProposal4.getKey()).thenReturn("b260ffdb6eba541fcf18601923457307647dce807851b9d19da133ab");

    List<ParamProposal> prevParamProposals = List.of(paramProposal1, paramProposal2, paramProposal3, paramProposal4);
    Mockito.when(paramProposalRepository.findParamProposalsByEpochNo(4))
            .thenReturn(prevParamProposals);
    Block cachedBlock = Mockito.mock(Block.class);
    Optional<Block> optionalBlock = Optional.of(cachedBlock);
    Mockito.when(epochRepository.findEpochByNo(5)).thenReturn(optionalEpoch);
    Mockito.when(epochRepository.findEpochByNo(4)).thenReturn(optionalEpoch2);
    Mockito.when(blockRepository.findFirstByEpochNo(5)).thenReturn(optionalBlock);

    EpochParamServiceImpl epochParamServiceImpl = new EpochParamServiceImpl(blockRepository,
            paramProposalRepository, epochParamRepository, epochRepository,
            costModelService, genesisDataService, epochParamMapper);
    epochParamServiceImpl.setDefShelleyEpochParam(defShelleyEpochParam);
    epochParamServiceImpl.setDefAlonzoEpochParam(defAlonzoEpochParam);
    epochParamServiceImpl.setDefBabbageEpochParam(defBabbageEpochParam);
    epochParamServiceImpl.setDefConwayEpochParam(defConwayEpochParam);
    epochParamServiceImpl.handleEpochParams();

    Mockito.verify(epochParamRepository, Mockito.times(1)).save(any());
    Mockito.verify(epochParamMapper, Mockito.times(2))
            .updateByEpochParam(any(), any());
    Mockito.verify(epochParamMapper, Mockito.times(1))
            .updateByParamProposal(any(), any());
  }
}