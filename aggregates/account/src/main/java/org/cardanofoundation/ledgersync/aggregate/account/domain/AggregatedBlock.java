package org.cardanofoundation.ledgersync.aggregate.account.domain;

//import org.cardanofoundation.ledgersync.common.common.AuxData;

import com.bloxbean.cardano.yaci.core.model.AuxData;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.common.common.Era;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AggregatedBlock {

    Era era;
    int network;
    String hash;
    Integer epochNo;
    Integer epochSlotNo;
    Long slotNo;
    Long blockNo;
    String prevBlockHash;
    int blockSize;
    Timestamp blockTime;
    Long txCount;
    int protoMajor;
    int protoMinor;
    String vrfKey;
    String opCert;
    Long opCertCounter;
    List<AggregatedTx> txList;
    Map<Integer, AuxData> auxiliaryDataMap; // Key is tx index in block
    Boolean isGenesis;
}
