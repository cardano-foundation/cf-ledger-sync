package org.cardanofoundation.ledgersync.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenesisData {

    Block block;
    List<Tx> txs;
    List<TxOut> txOuts;
    List<SlotLeader> slotLeaders;
    CostModel costModel;
    EpochParam shelley;
    EpochParam alonzo;
    EpochParam babbage;
    Timestamp startTime;
}
