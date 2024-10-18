package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.ConstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstitutionRepo extends JpaRepository<ConstitutionEntity, Integer> {

}
