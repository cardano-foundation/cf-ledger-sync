package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.ParamProposal;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ParamProposalRepository extends JpaRepository<ParamProposal, Long> {

    List<ParamProposal> findParamProposalsByEpochNo(Integer epochNo);

    @Modifying
    void deleteAllByRegisteredTxIn(Collection<Tx> registeredTxs);
}
