package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataMap;
import com.bloxbean.cardano.yaci.core.model.AuxData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxMetadataHash;
import org.cardanofoundation.ledgersync.common.util.CborSerializationUtil;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.repository.TxMetadataRepository;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TxMetaDataServiceTest {

  @Mock
  BlockDataService blockDataService;

  @Mock
  TxMetadataRepository txMetadataRepository;


  TxMetaDataServiceImpl victim;


  @BeforeEach
  void setUp() {
    victim = new TxMetaDataServiceImpl(txMetadataRepository, blockDataService, new ObjectMapper());
  }

  @Test
  void testHandleEmptyAuxiliaryDataMap() {
    when(blockDataService.getAllAggregatedBlocks())
        .thenReturn(Collections.emptyList());
    victim.handleAuxiliaryDataMaps(Collections.emptyMap());
  }

  @Test
  void testHandleAuxiliaryData() {
    Map<String, Tx> txMap = new HashMap<>();
    ReflectionTestUtils.setField(victim, "mapper", new ObjectMapper());

    Tx validTx = Tx.builder()
        .id(1L)
        .hash("1234567890")
        .validContract(Boolean.TRUE)
        .build();

    AggregatedTx aggregatedTx = AggregatedTx.builder()
        .hash(validTx.getHash())
        .blockIndex(1L)
        .auxiliaryDataHash(validTx.getHash())
        .validContract(validTx.getValidContract())
        .build();

    txMap.put(validTx.getHash(), validTx);

    Map<Integer, AuxData> auxiliaryDataMap = new HashMap();
    CBORMetadataMap cborMap = new CBORMetadataMap();
    cborMap.put(BigInteger.ONE, "56ef9d2933811580c7beb451bcf69d305153644d9ba149dee95e455536ee8b8f");
    String cbor = HexUtil.encodeHexString(CborSerializationUtil.serialize(cborMap.getMap()));
    AuxData auxData = new AuxData(cbor,
        """
            {"1":{"cardano": "First Metadata from cardanocli-js"}}
            """, null, null, null);

    auxiliaryDataMap.put(1, auxData);

    AggregatedBlock aggregatedBlock = AggregatedBlock.builder()
        .auxiliaryDataMap(auxiliaryDataMap)
        .txList(List.of(aggregatedTx))
        .build();

    Map<String, TxMetadataHash> expect = new HashMap<>();
    expect.put(validTx.getHash(), TxMetadataHash.builder()
        .hash("1234567890")
        .build());

    when(blockDataService.getAllAggregatedBlocks())
        .thenReturn(List.of(aggregatedBlock));

    victim.handleAuxiliaryDataMaps(txMap);
  }

}
