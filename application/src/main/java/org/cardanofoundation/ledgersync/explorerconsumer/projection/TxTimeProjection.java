package org.cardanofoundation.ledgersync.explorerconsumer.projection;

import java.sql.Timestamp;

public interface TxTimeProjection {

    Long getTxId();

    Timestamp getTxTime();

    Boolean getTxWithSc();

    Boolean getTxWithMetadataWithoutSc();

    Boolean getSimpleTx();
}
