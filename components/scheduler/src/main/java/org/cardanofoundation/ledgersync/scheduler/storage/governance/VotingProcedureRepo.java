package org.cardanofoundation.ledgersync.scheduler.storage.governance;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.VotingProcedureEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.VotingProcedureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingProcedureRepo extends JpaRepository<VotingProcedureEntity, VotingProcedureId> {

}
