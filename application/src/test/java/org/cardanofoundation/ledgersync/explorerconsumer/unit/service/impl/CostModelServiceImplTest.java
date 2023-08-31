package org.cardanofoundation.ledgersync.explorerconsumer.unit.service.impl;

import com.bloxbean.cardano.client.plutus.spec.Language;
import com.bloxbean.cardano.yaci.core.model.ProtocolParamUpdate;
import com.bloxbean.cardano.yaci.core.model.Update;
import org.cardanofoundation.explorer.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.common.common.cost.mdl.CostModels;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.CostModelRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.CostModelServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CostModelServiceImplTest {

    @Test
    void getGenesisCostModelDoNotSave() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        CostModel costModel = Mockito.mock(CostModel.class);
        CostModel costModelFinded = Mockito.mock(CostModel.class);
        Optional<CostModel> costModelOption = Optional.of(costModelFinded);

        Mockito.when(costModel.getHash()).thenReturn("thx00");
        Mockito.when(costModelRepository.findByHash("thx00")).thenReturn(costModelOption);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        costModelService.setGenesisCostModel(costModel);
        Mockito.verify(costModelRepository, Mockito.times(0)).save(Mockito.any());
        assertEquals(costModelFinded, costModelService.getGenesisCostModel());
    }

    @Test
    void getGenesisCostModelSave() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        CostModel costModel = Mockito.mock(CostModel.class);
        Optional<CostModel> costModelOption = Optional.ofNullable(null);

        Mockito.when(costModel.getHash()).thenReturn("thx00");
        Mockito.when(costModelRepository.findByHash("thx00")).thenReturn(costModelOption);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        costModelService.setGenesisCostModel(costModel);
        Mockito.verify(costModelRepository, Mockito.times(1)).save(costModel);
        //Genesis cost = null?
        assertNull(costModelService.getGenesisCostModel());
    }


    @Test
    void handleCostModelNoUpdates() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
        Update update = Mockito.mock(Update.class);

        Mockito.when(tx.getUpdate()).thenReturn(update);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
        costModelService.handleCostModel(tx);
    }

    //TODO -- refactor fix test
//    @Test
//    void handleCostModelPlutusV1() {
//        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
//        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
//        Update update = Mockito.mock(Update.class);
//        ProtocolParamUpdate protocolParamUpdate = Mockito.mock(ProtocolParamUpdate.class);
//        Map<String, ProtocolParamUpdate> protocolParamUpdates = new HashMap<>();
//        Map<Language, List<BigInteger>> languageListMap = new HashMap<>();
//        Language languageCostModel = Language.PLUTUS_V1;
//        List<BigInteger> languageList = Arrays.asList(BigInteger.valueOf(0));
//        languageListMap.put(languageCostModel, languageList);
//        protocolParamUpdates.put("0", protocolParamUpdate);
//        CostModels costModel = Mockito.mock(CostModels.class);
//
//        Mockito.when(tx.getUpdate()).thenReturn(update);
//        Mockito.when(update.getProtocolParamUpdates()).thenReturn(protocolParamUpdates);
//        Mockito.when(protocolParamUpdate.getCostModels()).thenReturn(costModel);
//        Mockito.when(costModel.getLanguages()).thenReturn(languageListMap);
//        Mockito.when(costModel.getHash()).thenReturn("X0ksd");
//
//        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
//        costModelService.handleCostModel(tx);
//        Mockito.verify(costModelRepository,Mockito.times(1)).saveAll(Mockito.anyCollection());
//    }

    //TODO -- refactor fix test

//    @Test
//    void handleCostModelPlutusV2() {
//        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
//        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
//        Update update = Mockito.mock(Update.class);
//        ProtocolParamUpdate protocolParamUpdate = Mockito.mock(ProtocolParamUpdate.class);
//        Map<String, ProtocolParamUpdate> protocolParamUpdates = new HashMap<>();
//        Map<Language, List<BigInteger>> languageListMap = new HashMap<>();
//        Language languageCostModel = Language.PLUTUS_V2;
//        List<BigInteger> languageList = Arrays.asList(BigInteger.valueOf(1));
//        languageListMap.put(languageCostModel, languageList);
//        protocolParamUpdates.put("0", protocolParamUpdate);
//        CostModels costModel = Mockito.mock(CostModels.class);
//
//        Mockito.when(tx.getUpdate()).thenReturn(update);
//        Mockito.when(update.getProtocolParamUpdates()).thenReturn(protocolParamUpdates);
//        Mockito.when(protocolParamUpdate.getCostModels()).thenReturn(costModel);
//        Mockito.when(costModel.getLanguages()).thenReturn(languageListMap);
//        Mockito.when(costModel.getHash()).thenReturn("X0ksd");
//
//        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
//        costModelService.handleCostModel(tx);
//        Mockito.verify(costModelRepository,Mockito.times(1)).saveAll(Mockito.anyCollection());
//    }

    @Test
    void findCostModelByHashReturnNull() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);

        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        assertNull(costModelService.findCostModelByHash("thx00"));
    }

    @Test
    void findCostModelByHashReturnCostModel() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        CostModel costModel = Mockito.mock(CostModel.class);
        CostModel costModelFinded = Mockito.mock(CostModel.class);
        Optional<CostModel> costModelOption = Optional.of(costModelFinded);

        Mockito.when(costModel.getHash()).thenReturn("thx00");
        Mockito.when(costModelRepository.findByHash("thx00")).thenReturn(costModelOption);

        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        assertEquals(costModelFinded, costModelService.findCostModelByHash("thx00"));
    }

}
