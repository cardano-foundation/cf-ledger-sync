package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeDeRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeDeRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeDeregistrationRepo extends JpaRepository<CommitteeDeRegistrationEntity, CommitteeDeRegistrationId> {

}
