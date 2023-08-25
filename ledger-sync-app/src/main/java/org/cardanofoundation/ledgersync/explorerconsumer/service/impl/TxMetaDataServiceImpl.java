package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;


import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.common.cbor.CborSerializationUtil;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.yaci.core.model.AuxData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxMetadata;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxMetadataRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TxMetaDataService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TxMetaDataServiceImpl implements TxMetaDataService {

    private final TxMetadataRepository txMetadataRepository;
    private final BlockDataService blockDataService;
    private final ObjectMapper mapper;

    @Override
    public List<TxMetadata> handleAuxiliaryDataMaps(Map<String, Tx> txMap) {
        List<TxMetadata> txMetadataList = new ArrayList<>();

        blockDataService.getAllAggregatedBlocks().forEach(aggregatedBlock -> {
            if (CollectionUtils.isEmpty(aggregatedBlock.getAuxiliaryDataMap())) {
                return;
            }

            Map<Integer, AuxData> auxiliaryDataMap = aggregatedBlock.getAuxiliaryDataMap();
            aggregatedBlock.getTxList().stream()
                    .filter(aggregatedTx -> auxiliaryDataMap.containsKey((int) aggregatedTx.getBlockIndex()))
                    .flatMap(aggregatedTx -> {
                        int txIndex = (int) aggregatedTx.getBlockIndex();
                        AuxData auxData = auxiliaryDataMap.get(txIndex);
                        String txHash = aggregatedTx.getHash();
                        Tx tx = txMap.get(txHash);
                        return handleAuxiliaryData(auxData, tx).stream();
                    })
                    .forEach(txMetadataList::add);
        });

        return txMetadataRepository.saveAll(txMetadataList);
    }

    private List<TxMetadata> handleAuxiliaryData(AuxData auxiliaryData, Tx tx) {
        if (!ObjectUtils.isEmpty(auxiliaryData.getMetadataCbor())) {
            try {
                Map<BigDecimal, Object> json = mapper.readValue(auxiliaryData.getMetadataJson(),
                        new TypeReference<>() {
                        });

                return json.entrySet().stream().map(entry -> {
                    String metadataJson = null;

                    if (Objects.nonNull(entry.getValue()) &&
                            !entry.getValue().toString().contains(ConsumerConstant.BYTE_NULL)) {
                        metadataJson = JsonUtil.getPrettyJson(entry.getValue());
                    }

                    CBORMetadata cclMetadata = null;
                    cclMetadata = CBORMetadata.deserialize((co.nstant.in.cbor.model.Map) CborSerializationUtil.deserialize(HexUtil.decodeHexString(auxiliaryData.getMetadataCbor())));
                    DataItem valueDI = cclMetadata.getData().get(new UnsignedInteger(entry.getKey().toBigInteger()));
                    byte[] valueBytes = null;
                    try {
                        valueBytes = CborSerializationUtil.serialize(valueDI);
                    } catch (CborException e) {
                        throw new IllegalStateException(e);
                    }

                    return TxMetadata.builder()
                            .tx(tx)
                            .json(metadataJson)
                            .key(entry.getKey().toBigInteger())
                            .bytes(valueBytes) //TODO yaci
                            .build();
                }).distinct().collect(Collectors.toList());
            } catch (Exception ex) {
                log.error("Tx hash: {}, meta data json: {}, mess: {}", tx.getHash(),
                        auxiliaryData.getMetadataJson(), ex.getMessage());
                throw new IllegalStateException();
            }
        }

        return Collections.emptyList();
    }
}
