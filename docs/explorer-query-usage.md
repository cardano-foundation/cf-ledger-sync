## 1. AdaPotsRepository
## 2. AddressRepository
## 3. AddressTxAmountRepository
## 4. AddressTxCountRepository
## 5. AggregateAddressTokenRepository
## 6. AggregateAddressTxBalanceRepository
## 7. AggregatePoolInfoRepository
## 8. AssetMetadataRepository
- Created by cf-ledger-consumer-schedules
### List queries:
#### findFirstBySubject
#### findBySubjectIn
#### findByFingerprintIn

## 9. BlockRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findFirstByBlockNo
- related table:
  - epoch
#### findFirstByHash
- related table:
  - epoch
#### findAllBlock
- query:
    ```sql
    @Query(value = "SELECT b FROM Block b", countQuery = "SELECT sum (e.blkCount) + 1 FROM Epoch e")
    ```
- related table:
  - epoch
#### countGenesisAndEbbBlockInfoByBlockIdLessThan
- query:
    ```sql
    @Query(
      value = "SELECT count(b) FROM Block b " + "WHERE b.blockNo IS NULL " + "AND b.id < :blockId")
    ```
#### countGenesisAndEbbBlockInfoByBlockIdGreaterThan
- query:
    ```sql
    @Query(
      value = "SELECT count(b) FROM Block b " + "WHERE b.blockNo IS NULL " + "AND b.id > :blockId")
    ```
#### findMinBlockNo
- query:
    ```sql
    @Query("SELECT min(b.blockNo) FROM Block b")
    ```
#### findNextBlockId
- query:
    ```sql
    @Query(
      value = "SELECT b.id FROM Block b " + "WHERE b.id > :blockId " + "ORDER BY b.id ASC LIMIT 1")
    ```
#### countAllBlock
- query:
    ```sql
    @Query(value = "SELECT sum(e.blkCount) FROM Epoch e")
    ```
- related table:
  - epoch
#### findAllByIdIn
#### findBlockByEpochNo
- query:
    ```sql
    @Query(value = "SELECT b FROM Block b WHERE b.epochNo = :epochNo")
    ```
#### findCurrentBlock
- query:
    ```sql
    @Query(value = "SELECT max(blockNo) FROM Block")
    ```
#### getCountBlockByPools
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, count(bk.id) AS countValue "
              + "FROM PoolHash ph "
              + "JOIN SlotLeader sl ON sl.poolHash.id = ph.id "
              + "JOIN Block bk ON bk.slotLeader.id = sl.id "
              + "WHERE ph.id IN :poolIds "
              + "GROUP BY ph.id")
    ```
- related table:
  - pool_hash
  - slot_leader
#### findTopDelegationByEpochBlock
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, ph.view AS poolView, count(bk.id) AS countValue "
              + "FROM PoolHash ph "
              + "JOIN SlotLeader sl ON sl.poolHash.id = ph.id "
              + "JOIN Block bk ON bk.slotLeader.id = sl.id "
              + "WHERE bk.epochNo = :epochNo "
              + "GROUP BY ph.id, ph.view "
              + "ORDER BY countValue DESC")
    ```
- related table:
  - pool_hash
  - slot_leader
#### findCurrentBlockById (redundant)
- query:
    ```sql
    @Query(value = "SELECT b from Block b WHERE b.id = (SELECT max(b2.id) FROM Block b2)")
    ```
#### findLatestBlock
- query:
    ```sql
    @Query("SELECT b FROM Block b WHERE b.blockNo IS NOT NULL ORDER BY b.blockNo DESC LIMIT 1")
    ```
#### findFirstBlock
- query:
    ```sql
    @Query("SELECT b FROM Block b WHERE b.blockNo IS NOT NULL ORDER BY b.blockNo ASC LIMIT 1")
    ```
#### findFirstShellyBlock
- query:
    ```sql
    @Query(
      "SELECT b FROM Block b"
          + " INNER JOIN Epoch e ON e.no = b.epochNo"
          + " WHERE e.era != org.cardanofoundation.explorer.common.entity.enumeration.EraType.BYRON"
          + " ORDER BY b.blockNo ASC LIMIT 1")
    ```
- related table:
  - epoch
#### getPoolInfoThatMintedBlock
- query:
    ```sql
    @Query(
      value =
          """
          select b.blockNo as blockNo, ph.view as poolView, po.poolName as poolName, po.tickerName as poolTicker, sl.description as description
          from Block b
          left join SlotLeader sl on sl.id = b.slotLeaderId
          left join PoolHash ph on ph.id = sl.poolHashId
          left join PoolOfflineData po on ph.id = po.poolId AND po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.poolId = ph.id)
          where b.blockNo = :blockNo
      """)
    ```
- related table:
  - slot_leader
  - pool_hash
  - pool_offline_data
</details>

### Related table:
- slot_leader
- epoch
- pool_hash
- pool_offline_data
