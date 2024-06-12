## 1. AdaPotsRepository

## 2. AddressRepository

## 3. AddressTxAmountRepository

## 4. AddressTxCountRepository

## 5. AggregateAddressTokenRepository

## 6. AggregateAddressTxBalanceRepository

## 7. AggregatePoolInfoRepository
## 8. AssetMetadataRepository
- Created by cf-ledger-consumer-schedules
<details>
<summary> <h3>List queries:</h3></summary>

#### findFirstBySubject
#### findBySubjectIn
#### findByFingerprintIn
</details>

## 9. BlockRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findFirstByBlockNo
- related table:
  - slot_leader
> **_NOTE:_** Include the slot_leader information.
#### findFirstByHash
- related table:
  - slot_leader
> **_NOTE:_** Include the slot_leader information.
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
> **_NOTE:_** A new table for pools is needed to store pool-related information.

## 10. CommitteeRegistrationRepository

## 11. CommitteInfoRepository

## 12. CostModelRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAll
#### findById
</details>

## 13. CustomBlockRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByBlockIdAndLimit
- query:
    ```sql
    SELECT * from block where id >= :blockId order by id asc;
    SELECT * from block where id <= :blockId order by id desc;
    ```
</details>

## 14. DelegationRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByTx
- related table:
  - pool_hash
  - address
#### getDelegatorsByAddress
- query:
    ```sql
     @Query(
      value =
          "SELECT sa.id AS stakeAddressId, sa.view AS view , bk.time AS time, tx.fee AS fee "
              + "FROM StakeAddress sa "
              + "JOIN StakeRegistration sr ON sa.id = sr.addr.id AND sr.id = (SELECT max(sr2.id) FROM StakeRegistration sr2 WHERE sa.id = sr2.addr.id) "
              + "JOIN Tx tx ON sr.tx.id  = tx.id "
              + "JOIN Block bk ON tx.block.id = bk.id "
              + "WHERE sa.id IN :addressIds "
              + "ORDER BY tx.id DESC")
    ```
- related table:
  - stake_address
  - stake_registration
  - tx
  - block
#### findDelegationPoolsSummary
- query:
    ```sql
     @Query(
      value =
          "SELECT ph.id AS poolId, ph.view AS poolView, pod.poolName AS poolName, pu.pledge AS pledge, pu.fixedCost AS fee, pu.margin AS margin "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData pod ON pod.pool.id = ph.id AND pod.id = (SELECT MAX(pod2.id) FROM PoolOfflineData pod2 WHERE pod2.pool.id = ph.id) "
              + "LEFT JOIN PoolUpdate pu ON pu.poolHash.id = ph.id AND pu.id = (SELECT MAX(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHash.id = ph.id) "
              + "WHERE ph.id IN :poolIds")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
#### findDelegationByAddress
- query:
    ```sql
     @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo, block.slotNo as slotNo,"
          + " block.blockNo as blockNo, block.epochNo as epochNo, poolHash.view as poolId,"
          + " poolOfflineData.json as poolData, poolOfflineData.tickerName as tickerName"
          + " FROM Delegation delegation"
          + " INNER JOIN Tx tx ON delegation.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " INNER JOIN StakeAddress stake ON delegation.address = stake"
          + " INNER JOIN PoolHash poolHash ON delegation.poolHash = poolHash"
          + " LEFT JOIN PoolOfflineData poolOfflineData ON poolOfflineData.id ="
          + " (SELECT max(pod.id) FROM PoolOfflineData pod WHERE pod.pool = poolHash)"
          + " WHERE stake.view = :stakeKey"
          + " ORDER BY block.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - delegation
  - tx
  - block
  - stake_address
  - pool_hash
  - pool_offline_data
#### findDelegationByAddress
- query:
    ```sql
     @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo, "
          + " poolOfflineData.poolName as poolName, delegation.poolHash.view as poolId,"
          + " block.blockNo as blockNo, block.epochNo as epochNo, tx.fee as fee, tx.outSum as outSum"
          + " FROM Delegation delegation"
          + " INNER JOIN Tx tx ON delegation.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " LEFT JOIN PoolOfflineData poolOfflineData on poolOfflineData.poolId = delegation.poolHash.id"
          + " WHERE delegation.address = :stakeKey"
          + " AND (block.time >= :fromTime ) "
          + " AND (block.time <= :toTime)"
          + " AND ( :txHash IS NULL OR tx.hash = :txHash)")
    ```
- related table:
  - delegation
  - tx
  - block
  - pool_offline_data
#### findDelegationByAddressAndTx
- query:
    ```sql
     @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo,"
          + " block.blockNo as blockNo, block.epochNo as epochNo, poolHash.view as poolId,"
          + " poolOfflineData.poolName as poolData, poolOfflineData.tickerName as tickerName,"
          + " tx.fee as fee, tx.outSum as outSum"
          + " FROM Delegation delegation"
          + " INNER JOIN Tx tx ON delegation.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " INNER JOIN PoolHash poolHash ON delegation.poolHash = poolHash"
          + " LEFT JOIN PoolOfflineData poolOfflineData ON poolOfflineData.id ="
          + " (SELECT max(pod.id) FROM PoolOfflineData pod WHERE pod.pool = poolHash)"
          + " WHERE delegation.address = :stakeKey AND tx.hash = :txHash")
    ```
- related table:
  - delegation
  - tx
  - block
  - pool_hash
  - pool_offline_data
#### findPoolDataByAddress
- query:
    ```sql
    @Query(
      "SELECT poolHash.view as poolId, poolOfflineData.poolName as poolData,"
          + " poolOfflineData.tickerName as tickerName, poolOfflineData.logoUrl as logoUrl, poolOfflineData.iconUrl as iconUrl "
          + " FROM Delegation delegation"
          + " INNER JOIN PoolHash poolHash ON delegation.poolHash = poolHash"
          + " LEFT JOIN PoolOfflineData poolOfflineData ON poolOfflineData.id ="
          + " (SELECT max(pod.id) FROM PoolOfflineData pod WHERE pod.pool = poolHash)"
          + " WHERE delegation.id = (SELECT max(d2.id) FROM Delegation d2 where d2.address = :address )"
          + " AND (SELECT max(sr.txId) FROM StakeRegistration sr WHERE sr.addr = :address) >"
          + " (SELECT COALESCE(max(sd.txId), 0) FROM StakeDeregistration sd WHERE sd.addr = :address)")
    ```
- related table:
  - delegation
  - pool_hash
  - pool_offline_data
  - stake_registration
  - stake_deregistration
#### findPoolDataByAddressIn
- query:
    ```sql
    @Query(
      "SELECT poolHash.view as poolId, poolOfflineData.poolName as poolData,"
          + " poolOfflineData.tickerName as tickerName, stake.view as stakeAddress"
          + " FROM Delegation delegation"
          + " INNER JOIN StakeAddress stake ON stake.id = delegation.address.id"
          + " INNER JOIN PoolHash poolHash ON delegation.poolHash = poolHash"
          + " LEFT JOIN PoolOfflineData poolOfflineData ON poolOfflineData.id ="
          + " (SELECT max(pod.id) FROM PoolOfflineData pod WHERE pod.pool = poolHash)"
          + " WHERE delegation.id IN "
          + " (SELECT max(d.id) FROM Delegation d "
          + " INNER JOIN StakeAddress sa ON d.address = sa"
          + " WHERE sa.view IN :addresses"
          + " GROUP BY sa.view )")
    ```
- related table:
  - delegation
  - stake_address
  - pool_hash
  - pool_offline_data
#### existsByAddress
#### liveDelegatorsList
- query:
    ```sql
    @Query(
      value =
          "SELECT dg1.address.id "
              + "FROM Delegation dg1 "
              + "JOIN PoolHash ph ON dg1.poolHash.id = ph.id "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash) "
              + "AND NOT EXISTS "
              + "(SELECT TRUE "
              + "FROM Delegation dg2 "
              + "WHERE dg2.address.id = dg1.address.id "
              + "AND dg2.tx.id > dg1.tx.id) "
              + "AND NOT EXISTS "
              + "(SELECT TRUE "
              + "FROM StakeDeregistration sd "
              + "WHERE sd.addr.id = dg1.address.id "
              + "AND sd.tx.id > dg1.tx.id)")
    ```
- related table:
  - delegation
  - pool_hash
  - stake_deregistration
#### findAllDelegations
- query:
    ```sql
    @Query(
      value = "SELECT DISTINCT delegation.txId FROM Delegation delegation",
      countQuery = "SELECT COUNT(DISTINCT delegation.txId) FROM Delegation delegation")
    ```
#### findDelegationByTxIdIn
- query:
    ```sql
    @Query(
      value =
          "SELECT delegation.address.view as stakeAddress, poolHash.view as poolView,"
              + " po.tickerName as tickerName, po.poolName as poolName, delegation.txId as txId"
              + " FROM Delegation delegation"
              + " INNER JOIN StakeAddress stake ON stake.id = delegation.stakeAddressId"
              + " INNER JOIN PoolHash poolHash ON delegation.poolHash = poolHash"
              + " LEFT JOIN PoolOfflineData po ON poolHash.id = po.pool.id "
              + " AND (po.id IS NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.pool.id = poolHash.id))"
              + " WHERE delegation.txId IN :txIds")
    ```
- related table:
  - delegation
  - stake_address
  - pool_hash
  - pool_offline_data
#### getBalanceByPoolIdIn
  - query:
      ```sql
      @Query(
        value =
            """
            select sum(lsab.quantity) as balance, d.poolHash.hashRaw as poolHash from Delegation d
            join StakeAddress sa on sa.id = d.stakeAddressId
            JOIN LatestStakeAddressBalance lsab on lsab.address = sa.view
            where d.poolHash.id in :poolIds
            group by d.poolHash.hashRaw
            """)
      ```
- related table:
  - stake_address
  - latest_stake_address_balance
</details>

### Related table:
- stake_address
- stake_registration
- stake_deregistration
- tx
- block
- pool_hash
- address
- pool_offline_data
- pool_update
- delegation
- latest_stake_address_balance
> **_NOTE:_**
> - How can we distinguish the order between two transactions?.
> - It's necessary to create the pool_offline_data table when using the staking store of yaci_store.

## 15. DelegationVoteRepository
> **_NOTE:_** - Regarding governance, we are currently using `delegation_vote` tables from yaci_store.

## 16. DRepRegistrationRepository
> **_NOTE:_** - Regarding governance, we are currently using `drep_registration` tables from yaci_store.

## 17. EpochParamRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findEpochParamByEpochNo
#### findEpochParamInTime
- query:
    ```sql
      @Query(
      value =
          "SELECT ep "
              + "FROM EpochParam ep "
              + "JOIN Epoch e ON e.no = ep.epochNo "
              + "WHERE e.startTime <=  :epochTime")
    ```
- related table:
  - epoch
#### findKeyDepositByEpochNo
- query:
    ```sql
    @Query(value = "SELECT COALESCE(ep.keyDeposit, 0) FROM EpochParam ep WHERE ep.epochNo = :epochNo")
    ```
#### findByEpochNoIn
#### findByEpochNo
</details>

### Related table:
- epoch




## x. TEMPLATE
<details>
<summary> <h3>List queries:</h3></summary>

#### TEMPLATE_QUERY
- query:
    ```sql
    @Query(
      value =
          "SELECT * from TEMPLATE"
    ```
- related table:
  - XXX
</details>

### Related table:
- XXX
> **_NOTE:_** Some note.


