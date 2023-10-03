package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxMetadataHash;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxMetaDataHashRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.TxMetaDataHashServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class TxMetaDataHashServiceTest {

  @Mock
  TxMetaDataHashRepository txMetaDataHashRepository;

  @InjectMocks
  TxMetaDataHashServiceImpl txMetaDataHashService;

  @Test
  @DisplayName("Handle metadata hash")
  void testTxHaveMetaDataHash() {
    AggregatedTx aggregatedTx = AggregatedTx.builder()
        .hash("1234567890")
        .auxiliaryDataHash("1234567890")
        .build();

    Map<String, Tx> txMap = new HashMap<>();

    txMap.put("1234567890", Tx.builder()
        .id(1L)
        .hash("1234567890")
        .build());

    Collection<AggregatedTx> successTxs = List.of(aggregatedTx);

    Map<String, TxMetadataHash> txMetadataMapActual = txMetaDataHashService.handleMetaDataHash(
        successTxs);

    Map<String, TxMetadataHash> expect = new HashMap<>();
    expect.put("1234567890", TxMetadataHash.builder()

            .hash("1234567890")
        .build());

    Assertions.assertTrue(!ObjectUtils.isEmpty(txMetadataMapActual));
    Assertions.assertEquals(expect.get(1L), txMetadataMapActual.get(1L));
  }

  @Test
  @DisplayName("Handle empty metadata hash")
  void testEmptyTxHaveMetaDataHash() {

    Map<String, TxMetadataHash> txMetadataMapActual =
        txMetaDataHashService.handleMetaDataHash(new ArrayList<>());

    Map<String, TxMetadataHash> expect = new HashMap<>();

    Assertions.assertTrue(ObjectUtils.isEmpty(txMetadataMapActual));
    Assertions.assertEquals(expect, txMetadataMapActual);
  }
}
