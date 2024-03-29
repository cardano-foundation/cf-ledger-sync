package org.cardanofoundation.ledgersync.repository;

import org.cardanofoundation.ledgersync.consumercommon.entity.TxMetadataHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TxMetaDataHashRepository extends JpaRepository<TxMetadataHash, Long> {

    List<TxMetadataHash> findAllByHashIn(List<String> hash);
}
