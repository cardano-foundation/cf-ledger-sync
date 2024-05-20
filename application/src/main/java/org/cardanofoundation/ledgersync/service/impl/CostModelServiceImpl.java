package org.cardanofoundation.ledgersync.service.impl;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.plutus.spec.Language;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.client.util.Tuple;
import com.bloxbean.cardano.yaci.core.util.CborSerializationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.CostModel;
import org.cardanofoundation.ledgersync.common.common.cost.mdl.PlutusV1Keys;
import org.cardanofoundation.ledgersync.common.common.cost.mdl.PlutusV2Keys;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.CostModelRepository;
import org.cardanofoundation.ledgersync.service.CostModelService;
import org.cardanofoundation.ledgersync.service.impl.plutus.PlutusKey;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostModelServiceImpl implements CostModelService {

    final CostModelRepository costModelRepository;
    final Map<PlutusKey, CostModel> genesisCostModelMap = new HashMap<>();

    @Override
    public CostModel getGenesisCostModel(PlutusKey plutusKey) {
        return this.genesisCostModelMap.get(plutusKey);
    }

    @Override
    public void setGenesisCostModel(PlutusKey plutusKey, CostModel costModel) {
        setup(plutusKey, costModel);
    }


    @Override
    public void handleCostModel(AggregatedTx tx) {
        if (CollectionUtils.isEmpty(tx.getUpdate().getProtocolParamUpdates())) {
            return;
        }

        //TODO -- yaci -
        Map<String, CostModel> costModels = tx.getUpdate().getProtocolParamUpdates().values().stream()
                .map(pp -> new Tuple<String, Map<Integer, String>>(pp.getCostModelsHash(), pp.getCostModels()))
                .filter(costMdlTuple -> !CollectionUtils.isEmpty(costMdlTuple._2))
                .map(costModelMessage -> {
                    String hash = costModelMessage._1;
                    Map<Integer, String> costModelMap = costModelMessage._2;

                    var languagePlutusV1V2Map = costModelMap.keySet()
                            .stream()
                            .filter(language -> language < 2)
                            .collect(Collectors.toMap(this::getPlutusKey,
                                    language -> getPlutusValue(language,
                                            convertCborCostModelToBigIntegerList(costModelMap.get(language)))));
                // TODO: plutus v3
//          var languageMap = costModelMessage.keySet()
//              .stream()
//              .collect(Collectors.toMap(this::getPlutusKey,
//                  language -> getPlutusValue(language,
//                      costModelMessage.getLanguages()
//                          .get(language))));
//

                    var json = JsonUtil.getPrettyJson(languagePlutusV1V2Map);
                    return CostModel.builder()
                            .costs(json)
                            .hash(hash)
                            .build();

                }).collect(Collectors.toConcurrentMap(CostModel::getHash, Function.identity()
                        , (past, future) -> future));

        if (!ObjectUtils.isEmpty(costModels)) {
            costModelRepository.existHash(
                            costModels.keySet())
                    .forEach(costModels::remove);
            costModelRepository.saveAll(costModels.values());
        }
    }

    @Override
    public CostModel findCostModelByHash(String hash) {
        var costModelOptional = costModelRepository.findByHash(hash);
        return costModelOptional.orElse(null);
    }

    private String getPlutusKey(Language language) {
        switch (language) {
            case PLUTUS_V1:
                return PlutusKey.PLUTUS_V1.value;
            case PLUTUS_V2:
                return PlutusKey.PLUTUS_V2.value;
            default:
                log.error("Un handle language {}", language);
                System.exit(1);
        }
        return null;
    }

    private String getPlutusKey(int language) {
        switch (language) {
            case 0:
                return PlutusKey.PLUTUS_V1.value;
            case 1:
                return PlutusKey.PLUTUS_V2.value;
            case 2:
                return PlutusKey.PLUTUS_V3.value;
            default:
                log.error("Un handle language {}", language);
                System.exit(1);
        }
        return null;
    }

    private Map<String, BigInteger> getPlutusValue(Integer language, List<BigInteger> values) {
        switch (language) {
            case 0:
                return new PlutusV1Keys().getCostModelMap(values);
            case 1:
                return new PlutusV2Keys().getCostModelMap(values);
            default:
                log.error("Un handle language {}", language);
                System.exit(1);
        }
        return Collections.emptyMap();
    }

    private List<BigInteger> convertCborCostModelToBigIntegerList(String cborCostModel) {
        Array costModelArray = (Array) CborSerializationUtil.deserializeOne(HexUtil.decodeHexString(cborCostModel));
        return costModelArray.getDataItems().stream()
                .filter(item -> item != SimpleValue.BREAK)
                .map(item -> ((UnsignedInteger) item).getValue())
                .collect(Collectors.toList());
    }

    public void setup(PlutusKey plutusKey, CostModel costModel) {
        costModelRepository.findByHash(costModel.getHash())
                .ifPresentOrElse(cm -> genesisCostModelMap.put(plutusKey, cm), () -> {
                            costModelRepository.save(costModel);
                            genesisCostModelMap.put(plutusKey, costModel);
                        }
                );
    }
}
