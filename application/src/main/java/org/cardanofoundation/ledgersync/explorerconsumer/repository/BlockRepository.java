package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findBlockByHash(String hash);

    Optional<Block> findBlockByBlockNo(long number);

    boolean existsBlockByHash(String hash);

    List<Block> findAllByBlockNoGreaterThanOrderByBlockNoDesc(Long blockNo);

    @Query("SELECT MAX(block.blockNo) FROM Block block")
    Optional<Long> getBlockHeight();

    @Query("SELECT MAX(block.id) FROM Block block")
    Optional<Long> getBlockIdHeight();

    Optional<Block> findFirstByEpochNo(Integer epochNo);

    Optional<Block> findBySlotNo(Long slotNo);
}
