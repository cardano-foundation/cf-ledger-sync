package org.cardanofoundation.ledgersync.scheduler.service.offchain;

import java.util.Optional;

import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.scheduler.storage.EraRepo;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainDataCheckpointStorage;

public interface OffChainProcessPersistDataService {
    void process();

    default OffChainDataCheckpoint getCurrentCheckpoint(
            OffChainDataCheckpointStorage offChainDataCheckpointStorage,
            EraRepo eraRepo,
            OffChainCheckpointType cpType) {

        Optional<OffChainDataCheckpoint> checkpoint = offChainDataCheckpointStorage.findFirstByType(cpType);
        if (checkpoint.isEmpty()) {
            Long starSlotAtEra = eraRepo.getStartSlotByEra(Era.CONWAY.getValue());
            return OffChainDataCheckpoint.builder().slotNo(starSlotAtEra).type(cpType).build();
        }
        return checkpoint.get();
    }
}
