package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.ProtocolParamUpdate;
import com.bloxbean.cardano.yaci.core.model.Update;
import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.CostModelRepository;
import org.cardanofoundation.ledgersync.service.impl.plutus.PlutusKey;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CostModelServiceImplTest {

    @Test
    void getGenesisCostModelDoNotSave() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        CostModel costModel = Mockito.mock(CostModel.class);
        CostModel costModelFound = Mockito.mock(CostModel.class);
        Optional<CostModel> costModelOption = Optional.of(costModelFound);

        Mockito.when(costModel.getHash()).thenReturn("thx00");
        Mockito.when(costModelRepository.findByHash("thx00")).thenReturn(costModelOption);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        costModelService.setGenesisCostModel(PlutusKey.PLUTUS_V1, costModel);
        Mockito.verify(costModelRepository, Mockito.times(0)).save(Mockito.any());
        assertEquals(costModelFound, costModelService.getGenesisCostModel(PlutusKey.PLUTUS_V1));
    }

    @Test
    void getGenesisCostModelSave() {

        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        CostModel costModel = Mockito.mock(CostModel.class);
        Optional<CostModel> costModelOption = Optional.ofNullable(null);

        Mockito.when(costModel.getHash()).thenReturn("thx00");
        Mockito.when(costModelRepository.findByHash("thx00")).thenReturn(costModelOption);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);

        costModelService.setGenesisCostModel(PlutusKey.PLUTUS_V1, costModel);
        Mockito.verify(costModelRepository, Mockito.times(1)).save(costModel);
        assertEquals(costModel, costModelService.getGenesisCostModel(PlutusKey.PLUTUS_V1));
    }


    @Test
    void handleCostModelNoUpdates() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
        Update update = Mockito.mock(Update.class);

        Mockito.when(tx.getUpdate()).thenReturn(update);
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
        costModelService.handleCostModel(tx);
        Mockito.verifyNoInteractions(costModelRepository);
    }

    @Test
    void handleCostModelPlutusV1() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
        Update update = Mockito.mock(Update.class);
        ProtocolParamUpdate protocolParamUpdate = Mockito.mock(ProtocolParamUpdate.class);
        Map<String, ProtocolParamUpdate> protocolParamUpdates = new HashMap<>();
        protocolParamUpdates.put("0", protocolParamUpdate);
        Map<Integer, String> costModelMap = new HashMap<>();
        costModelMap.put(0, "98a61a0003236119032c01011903e819023b00011903e8195e7104011903e818201a0001ca761928eb041959d81" +
                "8641959d818641959d818641959d818641959d818641959d81864186418641959d81864194c5118201a0002acfa182019b551041a" +
                "000363151901ff00011a00015c3518201a000797751936f404021a0002ff941a0006ea7818dc0001011903e8196ff604021a0003bd0" +
                "81a00034ec5183e011a00102e0f19312a011a00032e801901a5011a0002da781903e819cf06011a00013a34182019a8f118201903e818" +
                "201a00013aac0119e143041903e80a1a00030219189c011a00030219189c011a0003207c1901d9011a000330001901ff0119ccf3182019fd" +
                "40182019ffd5182019581e18201940b318201a00012adf18201a0002ff941a0006ea7818dc0001011a00010f92192da7000119eabb18201a00" +
                "02ff941a0006ea7818dc0001011a0002ff941a0006ea7818dc0001011a000c504e197712041a001d6af61a0001425b041a00040c660004001a00" +
                "014fab18201a0003236119032c010119a0de18201a00033d7618201979f41820197fb8182019a95d1820197df718201995aa18201a0374f693194a1f0a");

        Mockito.when(tx.getUpdate()).thenReturn(update);
        Mockito.when(update.getProtocolParamUpdates()).thenReturn(protocolParamUpdates);
        Mockito.when(protocolParamUpdate.getCostModels()).thenReturn(costModelMap);
        Mockito.when(protocolParamUpdate.getCostModelsHash()).thenReturn("94b519687375dee65f3fc2c20c904af32e036611954fd57ada33ddd09b91d785");
        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
        costModelService.handleCostModel(tx);
        Mockito.verify(costModelRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    }

    @Test
    void handleCostModelPlutusV2() {
        CostModelRepository costModelRepository = Mockito.mock(CostModelRepository.class);
        AggregatedTx tx = Mockito.mock(AggregatedTx.class);
        Update update = Mockito.mock(Update.class);
        ProtocolParamUpdate protocolParamUpdate = Mockito.mock(ProtocolParamUpdate.class);
        Map<String, ProtocolParamUpdate> protocolParamUpdates = new HashMap<>();
        protocolParamUpdates.put("0", protocolParamUpdate);
        Map<Integer, String> costModelMap = new HashMap<>();
        costModelMap.put(1, "98af1a0003236119032c01011903e819023b00011903e8195e7104011903e818201a0001ca761928eb041959d81" +
                "8641959d818641959d818641959d818641959d818641959d81864186418641959d81864194c5118201a0002acfa182019b551041" +
                "a000363151901ff00011a00015c3518201a000797751936f404021a0002ff941a0006ea7818dc0001011903e8196ff604021a000" +
                "3bd081a00034ec5183e011a00102e0f19312a011a00032e801901a5011a0002da781903e819cf06011a00013a34182019a8f1182" +
                "01903e818201a00013aac0119e143041903e80a1a00030219189c011a00030219189c011a0003207c1901d9011a000330001901" +
                "ff0119ccf3182019fd40182019ffd5182019581e18201940b318201a00012adf18201a0002ff941a0006ea7818dc0001011a0001" +
                "0f92192da7000119eabb18201a0002ff941a0006ea7818dc0001011a0002ff941a0006ea7818dc0001011a0011b22c1a0005fdde" +
                "00021a000c504e197712041a001d6af61a0001425b041a00040c660004001a00014fab18201a0003236119032c010119a0de182" +
                "01a00033d7618201979f41820197fb8182019a95d1820197df718201995aa18201a0223accc0a1a0374f693194a1f0a1a02515e841980b30a");

        Mockito.when(tx.getUpdate()).thenReturn(update);
        Mockito.when(update.getProtocolParamUpdates()).thenReturn(protocolParamUpdates);
        Mockito.when(protocolParamUpdate.getCostModels()).thenReturn(costModelMap);
        Mockito.when(protocolParamUpdate.getCostModelsHash()).thenReturn("94b519687375dee65f3fc2c20c904af32e036611954fd57ada33ddd09b91d785");

        CostModelServiceImpl costModelService = new CostModelServiceImpl(costModelRepository);
        costModelService.handleCostModel(tx);
        Mockito.verify(costModelRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
    }

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
