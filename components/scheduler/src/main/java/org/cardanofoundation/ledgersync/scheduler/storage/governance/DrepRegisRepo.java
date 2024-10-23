package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrepRegisRepo extends JpaRepository<DRepRegistrationEntity, DRepRegistrationId> {

}
