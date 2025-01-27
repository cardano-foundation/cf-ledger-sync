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

## 18. EpochRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findFirstByNo
#### findCurrentEpochNo
- query:
    ```sql
    @Query(value = "SELECT max(no) FROM Epoch")
    ```
#### findCurrentEpochSummary
- query:
    ```sql
    @Query(
      value =
          "SELECT no as no, blkCount as blkCount, maxSlot as maxSlot , startTime as startTime"
              + ",endTime as endTime "
              + "FROM Epoch "
              + "WHERE no  = (SELECT MAX(epoch.no) FROM Epoch epoch)")
    ```
#### findByCurrentEpochNo
- query:
    ```sql
    @Query(value = "SELECT ep FROM Epoch ep WHERE ep.no = (SELECT max(epoch.no) FROM Epoch epoch)")
    ```
#### findEpochTime
- query:
    ```sql
    @Query(
      value =
          "SELECT new org.cardanofoundation.explorer.api.projection.EpochTimeProjection( "
              + "e.no, e.startTime, e.endTime) "
              + "FROM Epoch e "
              + "WHERE e.no BETWEEN :min AND :max")
    ```
</details>

## 19. EpochStakeCheckpointRepository
> **_NOTE:_** - Regarding reward, Analyze later

## 20. EpochStakeRepository
> **_NOTE:_** - Analyze later

## 21. FailedTxOutRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findFailedTxOutByTx
- query:
    ```sql
    @Query(
      "SELECT failedTxOut.address AS address, tx.hash AS txHash, failedTxOut.value AS value,"
          + " failedTxOut.index AS index, failedTxOut.multiAssetsDescr AS assetsJson"
          + " FROM FailedTxOut failedTxOut "
          + " INNER JOIN Tx tx ON failedTxOut.tx = tx "
          + " WHERE tx = :tx")
    ```
- related table:
  - tx
</details>

### Related table:
- tx

## 22. GovernanceActionRepository
> **_NOTE:_** - Regarding governance, we are currently using `gov_action_proposal` tables from yaci_store

## 23. LatestAddressBalanceRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAllLatestAddressBalance
- query:
    ```sql
    @Query(value = "SELECT lab FROM LatestAddressBalance lab")
    ```
</details>

## 24. LatestStakeAddressBalanceRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByStakeAddress
- query:
    ```sql
    @Query("SELECT s FROM LatestStakeAddressBalance s WHERE s.address = :stakeAddress")
    ```
</details>

## 25. LatestTokenBalanceRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findTokenAndBalanceByAddress
- query:
    ```sql
    @Query(
      value =
          """
          SELECT ma.fingerprint as fingerprint,
          ma.policy as policy,
          ma.name as tokenName,
          ltb.quantity as quantity,
          am.url as url, am.decimals as decimals, am.ticker as ticker,
          am.logo as logo, am.description as description, am.subject as subject
          FROM LatestTokenBalance ltb
          INNER JOIN MultiAsset ma ON ma.unit = ltb.unit
          LEFT JOIN AssetMetadata am ON ma.fingerprint = ma.fingerprint
          WHERE ltb.address = :address
          AND ltb.quantity > 0
          """)
    ```
- related table:
  - multi_asset
  - asset_metadata
#### findTokenAndBalanceByAddressAndNameView
- query:
    ```sql
    @Query(
      value =
          """
          SELECT ma.fingerprint as fingerprint,
          ma.policy as policy,
          ma.name as tokenName,
          ltb.quantity as quantity,
          am.url as url, am.decimals as decimals, am.ticker as ticker,
          am.logo as logo, am.description as description, am.subject as subject
          FROM LatestTokenBalance ltb
          INNER JOIN MultiAsset ma ON ma.unit = ltb.unit
          LEFT JOIN AssetMetadata am ON ma.fingerprint = ma.fingerprint
          WHERE ltb.address = :address
          AND (lower(ma.nameView) LIKE CONCAT('%', :searchValue, '%') OR ma.fingerprint = :searchValue)
          AND ltb.quantity > 0
          """)
    ```
- related table:
  - multi_asset
  - asset_metadata
#### findAddressAndBalanceByPolicy
- query:
    ```sql
    @Query(
      value =
          """
      SELECT ltb.address as address, ltb.quantity as quantity, ma.name as tokenName, ma.fingerprint as fingerprint
      FROM LatestTokenBalance ltb
               INNER JOIN MultiAsset ma ON ma.unit = ltb.unit
      WHERE ltb.policy = :policy
      AND ltb.quantity > 0
      """)
    ```
- related table:
  - multi_asset
#### getTopHolderOfToken
- query:
    ```sql
    @Query(
      value =
          """
    select (case when ltb.stakeAddress is null then ltb.address else ltb.stakeAddress end) as address, ltb.quantity as quantity
    from LatestTokenBalance ltb
    where ltb.unit = :unit
    and ltb.quantity > 0
    """)
    ```
</details>

### Related table:
- multi_asset
- asset_metadata

## 26. LatestVotingProcedureRepository
> **_NOTE:_** - Regarding governance

## 27. MaTxMintRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByTxId
- query:
    ```sql
    @Query(
      "SELECT ma.name as name, ma.policy as policy, mtm.quantity as assetQuantity,"
          + " ma.fingerprint as fingerprint, am.url as url, am.ticker as ticker,"
          + " am.decimals as decimals, am.logo as logo, am.description as description"
          + " FROM MaTxMint mtm "
          + " JOIN MultiAsset ma ON ma.id = mtm.ident.id"
          + " LEFT JOIN AssetMetadata am ON am.fingerprint = ma.fingerprint"
          + " WHERE mtm.tx.id=:txId")
    ```
- related table:
  - multi_asset
  - asset_metadata
#### findByIdent
- query:
    ```sql
    @Query(
      "SELECT maTxMint"
          + " FROM MaTxMint maTxMint "
          + " WHERE maTxMint.ident.fingerprint = :tokenId ")
  @EntityGraph(attributePaths = {MaTxMint_.TX, "tx.block"})
    ```
- related table:
  - tx
#### getTxMetadataToken
- query:
    ```sql
    @Query(
      value =
          "SELECT tm.json FROM Tx tx"
              + " JOIN MaTxMint mtm ON mtm.tx = tx"
              + " JOIN TxMetadata tm ON tm.tx = tx"
              + " JOIN MultiAsset ma ON ma = mtm.ident"
              + " WHERE ma.fingerprint = :fingerprint AND tm.key = :label"
              + " ORDER BY mtm.id DESC LIMIT 1")
    ```
- related table:
  - tx_metadata
  - multi_asset
#### findFirstTxMintByMultiAssetId
- query:
    ```sql
     @Query(
      value = "SELECT mtm.tx_id from ma_tx_mint mtm where mtm.ident = :multiAssetId LIMIT 1",
      nativeQuery = true)
    ```
#### existsMoreOneMintTx
- query:
    ```sql
    @Query(
      value =
          "SELECT TRUE FROM MaTxMint mtm "
              + "WHERE mtm.ident IN :multiAssets "
              + "AND mtm.tx.id != :txId")
    ```
#### mintNumber
- query:
    ```sql
    @Query(
      value =
          "SELECT CASE WHEN count(mtm.id) >= 1 THEN TRUE ELSE FALSE END AS FLAG "
              + "FROM MaTxMint mtm "
              + "JOIN MultiAsset ma ON ma = mtm.ident "
              + "WHERE ma.fingerprint = :fingerprint AND mtm.quantity = 1")
    ```
- related table:
  - multi_asset
</details>

### Related table:
- multi_asset
- asset_metadata
- tx
- tx_metadata

## 28. MultiAssetRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAll
- query:
    ```sql
    @Query(
      value =
          "SELECT ma.id as id, ma.policy as policy, ma.name as name, ma.nameView as nameView, ttc.txCount as txCount,"
              + " ma.fingerprint as fingerprint, ma.supply as supply, ma.time as time,"
              + " LENGTH(ma.nameView) as nameViewLength, "
              + " am.url as url, am.ticker as ticker, am.decimals as decimals, "
              + " am.logo as logo, am.description as description, am.subject as subject"
              + " FROM MultiAsset ma"
              + " LEFT JOIN AssetMetadata am ON am.fingerprint = ma.fingerprint"
              + " INNER JOIN TokenTxCount ttc on ttc.ident = ma.id "
              + " WHERE ma.fingerprint = :query OR LOWER(ma.nameView) LIKE CONCAT('%', :query, '%')")
    ```
- related table:
  - asset_metadata
  - token_tx_count
#### countAllByQuery
- query:
    ```sql
    @Query(
      value =
          "SELECT COUNT(*) FROM (SELECT 1 FROM multi_asset ma "
              + "WHERE ma.fingerprint = :query OR LOWER(ma.name_view) LIKE CONCAT('%', :query, '%') limit 1000) as A",
      nativeQuery = true)
    ```
#### findMultiAssets
- query:
    ```sql
    @Query(
      value =
          "SELECT ma.id as id, ma.policy as policy, ma.name as name, ma.nameView as nameView, coalesce(ttc.txCount,0) as txCount,"
              + " ma.fingerprint as fingerprint, ma.supply as supply, ma.time as time,"
              + " LENGTH(ma.nameView) as nameViewLength, "
              + " am.url as url, am.ticker as ticker, am.decimals as decimals, "
              + " am.logo as logo, am.description as description, am.subject as subject"
              + " FROM MultiAsset ma"
              + " LEFT JOIN AssetMetadata am ON am.fingerprint = ma.fingerprint"
              + " INNER JOIN TokenTxCount ttc on ttc.ident = ma.id")
    ```
- related table:
  - asset_metadata
  - token_tx_count
#### findMultiAssets
#### findByFingerprint
#### countByPolicy
#### findAllByPolicy
#### findTokenInfoByScriptHash
- query:
    ```sql
    @Query(
      value =
          """
          SELECT ma.id as id, ma.policy as policy, ma.name as name, ma.nameView as nameView, ttc.txCount as txCount,
                ma.fingerprint as fingerprint, ma.supply as supply, ma.time as time,
                am.subject as subject, am.url as url, am.ticker as ticker,
                am.decimals as decimals, am.logo as logo, am.description as description
                FROM MultiAsset ma
                LEFT JOIN AssetMetadata am ON am.fingerprint = ma.fingerprint
                INNER JOIN TokenTxCount ttc on ttc.ident = ma.id
                WHERE ma.policy = :scriptHash
      """)
    ```
- related table:
  - asset_metadata
  - token_tx_count
#### findAllByIdIn
#### getTokenTxCount
- query:
    ```sql
    @Query(
      value =
          """
          SELECT ttc.txCount
                FROM MultiAsset ma
                LEFT JOIN TokenTxCount ttc on ttc.ident = ma.id
                WHERE ma.fingerprint = :fingerprint
      """)
    ```
- related table:
  - token_tx_count
#### findByNameViewLike
- query:
    ```sql
    @Query("SELECT ma FROM MultiAsset ma WHERE lower(ma.nameView) LIKE CONCAT('%', :query, '%') ")
    ```
#### getMintingAssets
- query:
    ```sql
    @Query(
      "SELECT ma.fingerprint as fingerprint, ma.name as tokenName, mtm.quantity as quantity FROM MultiAsset ma "
          + "JOIN MaTxMint mtm on (mtm.ident = ma and mtm.tx = :tx and ma.policy = :policy)")
    ```
- related table:
  - ma_tx_mint
#### countMultiAssetByPolicy
- query:
    ```sql
    @Query(
      "SELECT COALESCE(count(multiAsset.id), 0)"
          + " FROM MultiAsset multiAsset"
          + " WHERE multiAsset.policy = :policy")
    ```
#### countAssetHoldersByPolicy
- query:
    ```sql
     @Query(
      value =
          """
    SELECT COALESCE(COUNT(latestTokenBalance), 0) as numberOfHolders
              FROM MultiAsset multiAsset
              LEFT JOIN LatestTokenBalance latestTokenBalance ON multiAsset.unit = latestTokenBalance.unit
              WHERE multiAsset.policy = :policy
    """)
    ```
- related table:
  - latest_token_balance
#### findTopMultiAssetByScriptHashIn
- query:
    ```sql
    @Query(
      value =
          "WITH firstResult AS ("
              + " SELECT topMultiAsset.*"
              + " FROM script s"
              + " CROSS JOIN LATERAL"
              + " (SELECT ma.name as name, ma.name_view as name_view, ma.policy as policy, ma.fingerprint as fingerprint"
              + " FROM multi_asset ma "
              + " JOIN token_tx_count ttc on ttc.ident = ma.id"
              + " WHERE ma.policy = s.hash ORDER BY ttc.tx_count DESC LIMIT 5)"
              + " AS topMultiAsset WHERE s.hash IN :scriptHashes)"
              + " SELECT firstResult.policy as policy, firstResult.name as name, firstResult.name_view as nameView, firstResult.fingerprint as fingerprint,"
              + " am.url as url, am.ticker as ticker, am.decimals as decimals, "
              + " am.logo as logo, am.description as description, am.subject as subject"
              + " FROM firstResult"
              + " LEFT JOIN asset_metadata am ON am.fingerprint = firstResult.fingerprint",
      nativeQuery = true)
    ```
- related table:
  - script
  - token_tx_count
  - asset_metadata
#### getSliceByPolicy
#### findAllByUnitIn
- query:
    ```sql
    @Query("SELECT ma FROM MultiAsset ma WHERE ma.unit IN :units")
    ```
</details>

### Related table:
- asset_metadata
- token_tx_count
- ma_tx_mint
- latest_token_balance
- script

## 29. ParamProposalRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getParamProposalByRegisteredTxId
- query:
    ```sql
    @Query(
      "SELECT pp "
          + "FROM ParamProposal  pp "
          + "WHERE pp.registeredTx.id = :id "
          + "ORDER BY pp.id DESC")
    ```
#### findProtocolsChange
- query:
    ```sql
    @Query(
      "SELECT pp.minFeeA AS minFeeA , pp.minFeeB AS minFeeB, pp.maxBlockSize AS maxBlockSize ,"
          + "pp.maxTxSize AS maxTxSize,pp.maxBhSize AS maxBhSize,pp.keyDeposit AS keyDeposit, "
          + "pp.poolDeposit AS poolDeposit, pp.maxEpoch AS maxEpoch, pp.optimalPoolCount AS optimalPoolCount, "
          + "pp.influence AS influence, pp.monetaryExpandRate AS monetaryExpandRate, "
          + "pp.treasuryGrowthRate AS treasuryGrowthRate, pp.decentralisation AS decentralisation,"
          + "pp.entropy AS entropy, pp.protocolMajor AS protocolMajor, pp.protocolMinor AS protocolMinor, "
          + "pp.minUtxoValue AS minUtxoValue, pp.minPoolCost AS minPoolCost, pp.priceMem AS priceMem,"
          + " pp.priceStep AS priceStep, pp.maxTxExMem AS maxTxExMem, pp.maxTxExSteps AS maxTxExSteps, "
          + "pp.maxBlockExMem AS maxBlockExMem, pp.maxBlockExSteps AS maxBlockExSteps, "
          + "pp.maxValSize AS maxValSize, pp.collateralPercent AS collateralPercent, "
          + "pp.maxCollateralInputs AS maxCollateralInputs, pp.coinsPerUtxoSize AS coinsPerUtxoSize,"
          + "pp.costModelId AS costModel, pp.registeredTxId AS tx, pp.epochNo AS epochNo "
          + "FROM ParamProposal  pp "
          + "GROUP BY pp.minFeeA, pp.minFeeB, pp.maxBlockSize, pp.maxTxSize,pp.maxBhSize,pp.keyDeposit, "
          + "pp.poolDeposit, pp.maxEpoch, pp.optimalPoolCount, "
          + "pp.influence, pp.monetaryExpandRate, "
          + "pp.treasuryGrowthRate, pp.decentralisation, pp.entropy, pp.protocolMajor, "
          + "pp.protocolMinor, pp.minUtxoValue, pp.minPoolCost, pp.priceMem, pp.priceStep, pp.maxTxExMem, "
          + "pp.maxTxExSteps, pp.maxBlockExMem, pp.maxBlockExSteps, pp.maxValSize, pp.collateralPercent, "
          + "pp.maxCollateralInputs, pp.coinsPerUtxoSize, pp.costModel.id, pp.registeredTx.id, pp.epochNo")
    ```
#### findProtocolsChange
- query:
    ```sql
    @Query(
      "SELECT pp.minFeeA AS minFeeA , pp.minFeeB AS minFeeB, pp.maxBlockSize AS maxBlockSize ,"
          + "pp.maxTxSize AS maxTxSize,pp.maxBhSize AS maxBhSize,pp.keyDeposit AS keyDeposit, "
          + "pp.poolDeposit AS poolDeposit, pp.maxEpoch AS maxEpoch, pp.optimalPoolCount AS optimalPoolCount, "
          + "pp.influence AS influence, pp.monetaryExpandRate AS monetaryExpandRate, "
          + "pp.treasuryGrowthRate AS treasuryGrowthRate, pp.decentralisation AS decentralisation,"
          + "pp.entropy AS entropy, pp.protocolMajor AS protocolMajor, pp.protocolMinor AS protocolMinor, "
          + "pp.minUtxoValue AS minUtxoValue, pp.minPoolCost AS minPoolCost, pp.priceMem AS priceMem,"
          + " pp.priceStep AS priceStep, pp.maxTxExMem AS maxTxExMem, pp.maxTxExSteps AS maxTxExSteps, "
          + "pp.maxBlockExMem AS maxBlockExMem, pp.maxBlockExSteps AS maxBlockExSteps, "
          + "pp.maxValSize AS maxValSize, pp.collateralPercent AS collateralPercent, "
          + "pp.maxCollateralInputs AS maxCollateralInputs, pp.coinsPerUtxoSize AS coinsPerUtxoSize,"
          + "pp.costModelId AS costModel, pp.registeredTxId AS tx, e.no  AS epochNo "
          + "FROM ParamProposal  pp "
          + "RIGHT JOIN Epoch e ON e.no = pp.epochNo "
          + "WHERE e.startTime <= :epochTime "
          + "GROUP BY pp.minFeeA, pp.minFeeB, pp.maxBlockSize, pp.maxTxSize,pp.maxBhSize,pp.keyDeposit, "
          + "pp.poolDeposit, pp.maxEpoch, pp.optimalPoolCount, "
          + "pp.influence, pp.monetaryExpandRate, "
          + "pp.treasuryGrowthRate, pp.decentralisation, pp.entropy, pp.protocolMajor, "
          + "pp.protocolMinor, pp.minUtxoValue, pp.minPoolCost, pp.priceMem, pp.priceStep, pp.maxTxExMem, "
          + "pp.maxTxExSteps, pp.maxBlockExMem, pp.maxBlockExSteps, pp.maxValSize, pp.collateralPercent, "
          + "pp.maxCollateralInputs, pp.coinsPerUtxoSize, pp.costModel.id, pp.registeredTx.id, e.no")
    ```
- related table:
  - epoch
#### findMaxEpochChange
- query:
    ```sql
    @Query(
      """
      SELECT MAX(ep.epochNo)
      FROM ParamProposal pp
      JOIN EpochParam ep ON ep.epochNo = pp.epochNo + 1
      """)
    ```
- related table:
  - epoch_param
#### findProtocolsChange
- query:
    ```sql
    @Query(
      "SELECT CASE WHEN pp.minFeeA IS NOT NULL THEN pp.minFeeA ELSE ep.minFeeA END  AS minFeeA, "
          + " CASE WHEN pp.minFeeB IS NOT NULL THEN pp.minFeeB ELSE ep.minFeeB END  AS minFeeB,  "
          + " CASE WHEN pp.maxBlockSize IS NOT NULL THEN pp.maxBlockSize ELSE ep.maxBlockSize END  AS maxBlockSize,  "
          + " CASE WHEN pp.maxTxSize IS NOT NULL THEN pp.maxTxSize ELSE ep.maxTxSize END  AS maxTxSize,  "
          + " CASE WHEN pp.maxBhSize IS NOT NULL THEN pp.maxBhSize ELSE ep.maxBhSize END  AS maxBhSize,  "
          + " CASE WHEN pp.keyDeposit IS NOT NULL THEN pp.keyDeposit ELSE ep.keyDeposit END  AS keyDeposit,  "
          + " CASE WHEN pp.poolDeposit IS NOT NULL THEN pp.poolDeposit ELSE ep.poolDeposit END  AS poolDeposit,  "
          + " CASE WHEN pp.maxEpoch IS NOT NULL THEN pp.maxEpoch ELSE ep.maxEpoch END  AS maxEpoch,  "
          + " CASE WHEN pp.optimalPoolCount IS NOT NULL THEN pp.optimalPoolCount ELSE ep.optimalPoolCount END  AS optimalPoolCount,  "
          + " CASE WHEN pp.influence IS NOT NULL THEN pp.influence ELSE ep.influence END  AS influence,  "
          + " CASE WHEN pp.monetaryExpandRate IS NOT NULL THEN pp.monetaryExpandRate ELSE ep.monetaryExpandRate END  AS monetaryExpandRate,  "
          + " CASE WHEN pp.treasuryGrowthRate IS NOT NULL THEN pp.treasuryGrowthRate ELSE ep.treasuryGrowthRate END  AS treasuryGrowthRate,  "
          + " CASE WHEN pp.decentralisation IS NOT NULL THEN pp.decentralisation ELSE ep.decentralisation END  AS decentralisation,  "
          + " CASE WHEN pp.entropy IS NOT NULL THEN pp.entropy ELSE ep.extraEntropy END  AS entropy,  "
          + " CASE WHEN pp.protocolMajor IS NOT NULL THEN pp.protocolMajor ELSE ep.protocolMajor END  AS protocolMajor,  "
          + " CASE WHEN pp.protocolMinor IS NOT NULL THEN pp.protocolMinor ELSE ep.protocolMinor END  AS protocolMinor,  "
          + " CASE WHEN pp.minUtxoValue IS NOT NULL THEN pp.minUtxoValue ELSE ep.minUtxoValue END  AS minUtxoValue,  "
          + " CASE WHEN pp.minPoolCost IS NOT NULL THEN pp.minPoolCost ELSE ep.minPoolCost END  AS minPoolCost,  "
          + " CASE WHEN pp.priceMem IS NOT NULL THEN pp.priceMem ELSE ep.priceMem END  AS priceMem,  "
          + " CASE WHEN pp.priceStep IS NOT NULL THEN pp.priceStep ELSE ep.priceStep END  AS priceStep,  "
          + " CASE WHEN pp.maxTxExMem IS NOT NULL THEN pp.maxTxExMem ELSE ep.maxTxExMem END  AS maxTxExMem,  "
          + " CASE WHEN pp.maxTxExSteps IS NOT NULL THEN pp.maxTxExSteps ELSE ep.maxTxExSteps END  AS maxTxExSteps,  "
          + " CASE WHEN pp.maxBlockExMem IS NOT NULL THEN pp.maxBlockExMem ELSE ep.maxBlockExMem END  AS maxBlockExMem,  "
          + " CASE WHEN pp.maxBlockExSteps IS NOT NULL THEN pp.maxBlockExSteps ELSE ep.maxBlockExSteps END  AS maxBlockExSteps,  "
          + " CASE WHEN pp.maxValSize IS NOT NULL THEN pp.maxValSize ELSE ep.maxValSize END  AS maxValSize,  "
          + " CASE WHEN pp.collateralPercent IS NOT NULL THEN pp.collateralPercent ELSE ep.collateralPercent END  AS collateralPercent,  "
          + " CASE WHEN pp.maxCollateralInputs IS NOT NULL THEN pp.maxCollateralInputs ELSE ep.maxCollateralInputs END  AS maxCollateralInputs,  "
          + " CASE WHEN pp.coinsPerUtxoSize IS NOT NULL THEN pp.coinsPerUtxoSize ELSE ep.coinsPerUtxoSize END  AS coinsPerUtxoSize,  "
          + " CASE WHEN pp.costModelId IS NOT NULL THEN pp.costModelId "
          + "      ELSE ep.costModel.id "
          + " END AS costModel,  " // check Change
          + " CASE WHEN pp.minFeeA IS NOT NULL THEN TRUE ELSE FALSE END  AS minFeeAProposal, "
          + " CASE WHEN pp.minFeeB IS NOT NULL THEN TRUE ELSE FALSE END  AS minFeeBProposal,  "
          + " CASE WHEN pp.maxBlockSize IS NOT NULL THEN TRUE ELSE FALSE END  AS maxBlockSizeProposal,  "
          + " CASE WHEN pp.maxTxSize IS NOT NULL THEN TRUE ELSE FALSE END  AS maxTxSizeProposal,  "
          + " CASE WHEN pp.maxBhSize IS NOT NULL THEN TRUE ELSE FALSE END  AS maxBhSizeProposal,  "
          + " CASE WHEN pp.keyDeposit IS NOT NULL THEN TRUE ELSE FALSE END  AS keyDepositProposal,  "
          + " CASE WHEN pp.poolDeposit IS NOT NULL THEN TRUE ELSE FALSE END  AS poolDepositProposal,  "
          + " CASE WHEN pp.maxEpoch IS NOT NULL THEN TRUE ELSE FALSE END  AS maxEpochProposal,  "
          + " CASE WHEN pp.optimalPoolCount IS NOT NULL THEN TRUE ELSE FALSE END  AS optimalPoolCountProposal,  "
          + " CASE WHEN pp.influence IS NOT NULL THEN TRUE ELSE FALSE END  AS influenceProposal,  "
          + " CASE WHEN pp.monetaryExpandRate IS NOT NULL THEN TRUE ELSE FALSE END  AS monetaryExpandRateProposal,  "
          + " CASE WHEN pp.treasuryGrowthRate IS NOT NULL THEN TRUE ELSE FALSE END  AS treasuryGrowthRateProposal,  "
          + " CASE WHEN pp.decentralisation IS NOT NULL THEN TRUE ELSE FALSE END  AS decentralisationProposal,  "
          + " CASE WHEN pp.entropy IS NOT NULL THEN TRUE ELSE FALSE END  AS entropyProposal,  "
          + " CASE WHEN pp.protocolMajor IS NOT NULL THEN TRUE ELSE FALSE END  AS protocolMajorProposal,  "
          + " CASE WHEN pp.protocolMinor IS NOT NULL THEN TRUE ELSE FALSE END  AS protocolMinorProposal,  "
          + " CASE WHEN pp.minUtxoValue IS NOT NULL THEN TRUE ELSE FALSE END  AS minUtxoValueProposal,  "
          + " CASE WHEN pp.minPoolCost IS NOT NULL THEN TRUE ELSE FALSE END  AS minPoolCostProposal,  "
          + " CASE WHEN pp.priceMem IS NOT NULL THEN TRUE ELSE FALSE END  AS priceMemProposal,  "
          + " CASE WHEN pp.priceStep IS NOT NULL THEN TRUE ELSE FALSE END  AS priceStepProposal,  "
          + " CASE WHEN pp.maxTxExMem IS NOT NULL THEN TRUE ELSE FALSE END  AS maxTxExMemProposal,  "
          + " CASE WHEN pp.maxTxExSteps IS NOT NULL THEN TRUE ELSE FALSE END  AS maxTxExStepsProposal,  "
          + " CASE WHEN pp.maxBlockExMem IS NOT NULL THEN TRUE ELSE FALSE END  AS maxBlockExMemProposal,  "
          + " CASE WHEN pp.maxBlockExSteps IS NOT NULL THEN TRUE ELSE FALSE END  AS maxBlockExStepsProposal,  "
          + " CASE WHEN pp.maxValSize IS NOT NULL THEN TRUE ELSE FALSE END  AS maxValSizeProposal,  "
          + " CASE WHEN pp.collateralPercent IS NOT NULL THEN TRUE ELSE FALSE END  AS collateralPercentProposal,  "
          + " CASE WHEN pp.maxCollateralInputs IS NOT NULL THEN TRUE ELSE FALSE END  AS maxCollateralInputsProposal,  "
          + " CASE WHEN pp.coinsPerUtxoSize IS NOT NULL THEN TRUE ELSE FALSE END  AS coinsPerUtxoSizeProposal,  "
          + " CASE WHEN pp.costModelId IS NOT NULL THEN TRUE ELSE FALSE END AS costModelProposal, "
          + " b.time  AS blockTime,  "
          + " e.startTime AS epochTime, "
          + " ep.epochNo AS epochNo, "
          + " tx.hash AS hash "
          + " FROM EpochParam ep  "
          + " LEFT JOIN ParamProposal  pp ON pp.epochNo + 1 = ep.epochNo "
          + " LEFT JOIN Tx tx ON tx.id = pp.registeredTxId "
          + " LEFT JOIN Block b ON b.id =  tx.blockId "
          + " INNER JOIN Epoch e ON e.no = ep.epochNo "
          + " WHERE ep.epochNo <= :epochNo "
          + " ORDER BY ep.epochNo DESC ")
    ```
- related table:
  - epoch_param
  - tx
  - block
  - epoch
#### findKeysByEpochNo
- query:
    ```sql
    @Query("SELECT DISTINCT pp.key " + "FROM ParamProposal  pp " + "WHERE pp.epochNo = :epochNo ")
    ```
</details>

### Related table:
- epoch
- epoch_param
- tx
- block

## 30. PoolHashRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findEpochByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT bk.epochNo AS epochNo, count(bk.id) AS countBlock FROM PoolHash ph "
              + "JOIN SlotLeader sl ON sl.poolHash.id = ph.id "
              + "JOIN Block bk ON bk.slotLeader.id = sl.id "
              + "WHERE ph.id = :poolId AND bk.epochNo IN :epochNo "
              + "GROUP BY bk.epochNo")
    ```
- related table:
  - slot_leader
  - block
#### findAllWithoutUsingKoi0s
- query:
    ```sql
    @Query(
      value =
          " SELECT ph.id AS poolId, ph.view AS poolView, po.poolName AS poolName, pu.pledge AS pledge, "
              + "po.tickerName as tickerName, LENGTH(po.poolName) as poolNameLength, "
              + "COALESCE(api.governanceParticipationRate, -1) as governanceParticipationRate, COALESCE(api.votingPower, -1) as votingPower,"
              + "COALESCE(api.blockLifeTime, 0) as lifetimeBlock, COALESCE(api.blockInEpoch, 0) as epochBlock "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData po ON ph.id = po.poolId AND (po.id IS NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.poolId = ph.id)) "
              + "LEFT JOIN PoolUpdate pu ON ph.id = pu.poolHashId AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHashId = ph.id)"
              + "LEFT JOIN AggregatePoolInfo api on api.poolId = ph.id "
              + "WHERE ph.id NOT IN :exceptPoolIds "
              + "AND ( :param is null OR ph.view = :param OR ph.hashRaw = :param "
              + "OR LOWER(po.poolName) LIKE CONCAT('%', :param, '%') OR LOWER(po.tickerName) LIKE CONCAT('%', :param, '%'))"
              + "AND ( :minPledge <= coalesce(pu.pledge,0) and :maxPledge >= coalesce(pu.pledge,0) )"
              + "AND ( :minVotingPower <= coalesce(api.votingPower,0) and :maxVotingPower >= coalesce(api.votingPower,0)) "
              + "AND ( :minGovParticipationRate <= coalesce(api.governanceParticipationRate,0) and :maxGovParticipationRate >= coalesce(api.governanceParticipationRate,0)) "
              + "AND ( :minBlockLifeTime <= coalesce(api.blockLifeTime,0) and :maxBlockLifeTime >= coalesce(api.blockLifeTime,0))")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
  - aggregate_pool_info
#### findAllWithUsingKoiOs
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, ph.view AS poolView, po.poolName AS poolName, pu.pledge AS pledge, "
              + "po.tickerName as tickerName, LENGTH(po.poolName) as poolNameLength, "
              + "COALESCE(pi.activeStake, -1) AS poolSize, COALESCE(pi.liveSaturation, -1) AS saturation, "
              + "COALESCE(api.governanceParticipationRate, -1) as governanceParticipationRate, COALESCE(api.votingPower, -1) as votingPower,"
              + "COALESCE(api.blockLifeTime, 0) as lifetimeBlock, COALESCE(api.blockInEpoch, 0) as epochBlock "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData po ON ph.id = po.poolId AND (po.id IS NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.poolId = ph.id)) "
              + "LEFT JOIN PoolUpdate pu ON ph.id = pu.poolHashId AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHashId = ph.id) "
              + "LEFT JOIN PoolInfo pi ON ph.id = pi.poolId AND pi.fetchedAtEpoch = :epochNo "
              + "LEFT JOIN AggregatePoolInfo api on api.poolId = ph.id "
              + "WHERE ph.id NOT IN :exceptPoolIds "
              + "AND ( :param is null OR  ph.view = :param OR ph.hashRaw = :param "
              + "OR LOWER(po.poolName) LIKE CONCAT('%', :param, '%')  OR LOWER(po.tickerName) LIKE CONCAT('%', :param, '%'))"
              + "AND ( :minPoolSize <= coalesce(pi.activeStake, 0) and :maxPoolSize >= coalesce(pi.activeStake,0) )"
              + "AND ( :minPledge <= coalesce(pu.pledge,0) and :maxPledge >= coalesce(pu.pledge,0) )"
              + "AND ( :minSaturation <= coalesce(pi.liveSaturation,0) and :maxSaturation >= coalesce(pi.liveSaturation,0) ) "
              + "AND ( :minVotingPower <= coalesce(api.votingPower,0) and :maxVotingPower >= coalesce(api.votingPower,0) ) "
              + "AND ( :minGovParticipationRate <= coalesce(api.governanceParticipationRate,0) and :maxGovParticipationRate >= coalesce(api.governanceParticipationRate,0) ) "
              + "AND ( :minBlockLifeTime <= coalesce(api.blockLifeTime,0) and :maxBlockLifeTime >= coalesce(api.blockLifeTime,0) ) ")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
  - aggregate_pool_info
#### getListPoolIdIn
- query:
    ```sql
    @Query(value = "SELECT ph.id FROM PoolHash ph " + "WHERE ph.view IN :poolViews ")
    ```
#### findByViewOrHashRaw
- query:
    ```sql
    @Query(
      value =
          "SELECT ph FROM PoolHash ph "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash)")
    ```
#### getDataForPoolDetailNoReward
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, ph.hashRaw AS hashRaw, po.poolName AS poolName, po.tickerName AS tickerName, pu.pledge AS pledge, pu.margin AS margin, "
              + "pu.fixedCost AS cost, ep.optimalPoolCount AS paramK, sa.view AS rewardAddress, "
              + "po.json as json, po.iconUrl AS iconUrl, po.logoUrl AS logoUrl, ph.view AS poolView "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData po ON ph.id = po.pool.id AND (po.id is NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.pool.id  = ph.id)) "
              + "LEFT JOIN PoolUpdate pu ON ph.id = pu.poolHash.id AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHash.id  = ph.id) "
              + "LEFT JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "LEFT JOIN EpochParam ep ON ep.epochNo = :epochNo "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash)")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
  - stake_address
  - epoch_param
#### getDataForPoolDetail
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, ph.hashRaw AS hashRaw, po.poolName AS poolName, po.tickerName AS tickerName, pu.pledge AS pledge, pu.margin AS margin, "
              + "pu.fixedCost AS cost, ep.optimalPoolCount AS paramK, ap.reserves AS reserves, sa.view AS rewardAddress, "
              + "po.json as json, po.iconUrl AS iconUrl, po.logoUrl AS logoUrl, ph.view AS poolView "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData po ON ph.id = po.pool.id AND (po.id is NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.pool.id  = ph.id)) "
              + "LEFT JOIN PoolUpdate pu ON ph.id = pu.poolHash.id AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHash.id  = ph.id) "
              + "LEFT JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "LEFT JOIN EpochParam ep ON ep.epochNo = :epochNo "
              + "LEFT JOIN AdaPots ap ON ap.epochNo = :epochNo "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash)")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - stake_address
  - epoch_param
  - ada_pots
#### getPoolRegistration
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.pledge AS pledge, pu.margin AS margin, pu.vrfKeyHash AS vrfKey, pu.fixedCost AS cost, tx.hash AS txHash, bk.time AS time, tx.deposit AS deposit, tx.fee AS fee, sa.view AS rewardAccount "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx ON pu.registeredTx.id = tx.id "
              + "JOIN Block bk ON tx.block.id  = bk.id "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "WHERE pu.id = :id")
    ```
- related table:
  - pool_update
  - tx
  - block
  - stake_address
#### getPoolInfo
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS id, pod.poolName AS poolName, ph.hashRaw AS poolId, ph.view AS poolView, pod.iconUrl as icon "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData pod ON ph.id  = pod.pool.id AND pod.id = (SELECT max(pod2.id) FROM PoolOfflineData pod2 WHERE ph.id = pod2.pool.id ) "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash)")
    ```
- related table:
  - pool_hash
  - pool_offline_data
#### getPoolRegistrationByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.id AS poolUpdateId, pu.pledge AS pledge, pu.margin AS margin, pu.vrfKeyHash AS vrfKey, pu.fixedCost AS cost, tx.hash AS txHash, bk.time AS time, ep.poolDeposit AS deposit, tx.fee AS fee, sa.view AS rewardAccount "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx ON pu.registeredTx.id = tx.id "
              + "JOIN Block bk ON tx.block.id  = bk.id "
              + "JOIN EpochParam ep ON ep.epochNo = bk.epochNo "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "WHERE pu.id IN :poolCertificateIds ")
    ```
- related table:
  - pool_update
  - tx
  - block
  - epoch_param
  - stake_address
#### findByPoolNameLike
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS id, pod.poolName AS poolName, ph.hashRaw AS poolId, ph.view AS poolView, pod.iconUrl as icon "
              + "FROM PoolHash ph "
              + "INNER JOIN PoolOfflineData pod ON ph.id  = pod.pool.id AND pod.id = "
              + "(SELECT max(pod2.id) FROM PoolOfflineData pod2 WHERE ph.id = pod2.pool.id ) "
              + "WHERE LOWER(pod.poolName) LIKE CONCAT('%', :query, '%') OR "
              + "LOWER(pod.tickerName) LIKE CONCAT('%', :query, '%') ")
    ```
- related table:
  - pool_hash
  - pool_offline_data
#### getSlotNoWhenFirstDelegationByPoolHash
- query:
    ```sql
    @Query(
      value =
          "select min(d.slotNo) from PoolHash ph"
              + " join Delegation d on d.poolHash.id = ph.id"
              + " where ph.hashRaw =:poolHash")
    ```
#### getSlotCreatedAtGroupByPoolHash
- query:
    ```sql
    @Query(
      value =
          """
    select min(d.slotNo) as createdAt, ph.hashRaw as poolHash, ph.id as poolId from PoolHash ph
    join Delegation d on d.poolHash.id = ph.id
    group by ph.hashRaw, ph.id
    order by ph.id desc
""")
    ```
- related table:
  - delegation
#### getHashRawByView
- query:
    ```sql
    @Query(value = "select ph.hashRaw from PoolHash ph where ph.view = :view")
    ```
#### getPoolNameByPoolHashOrPoolView
- query:
    ```sql
    @Query(
      value =
          "SELECT pod.poolName AS poolName"
              + " FROM PoolHash ph"
              + " INNER JOIN PoolOfflineData pod ON ph.id  = pod.poolId AND pod.id ="
              + " (SELECT max(pod2.id) FROM PoolOfflineData pod2 WHERE ph.id = pod2.poolId)"
              + " WHERE  ph.hashRaw = :poolViewOrHash or ph.view = :poolViewOrHash")
    ```
- related table:
  - pool_hash
  - pool_offline_data
#### getPoolRangeWithUsingKoi0s
- query:
    ```sql
    @Query(
      value =
          " SELECT min(pi.activeStake) as minPoolSize, max(pi.activeStake) as maxPoolSize,"
              + " min(pi.liveSaturation) as minSaturation, max(pi.liveSaturation) as maxSaturation,"
              + " min(pu.pledge) as minPledge, max(pu.pledge) as maxPledge,"
              + " min(api.votingPower) as minVotingPower, max(api.votingPower) as maxVotingPower,"
              + " min (api.governanceParticipationRate) as minGovParticipationRate, max(api.governanceParticipationRate) as maxGovParticipationRate,"
              + " min (api.blockLifeTime) as minLifetimeBlock, max(api.blockLifeTime) as maxLifetimeBlock"
              + " FROM PoolHash ph"
              + " left join PoolOfflineData po ON ph.id = po.poolId AND (po.id IS NULL OR po.id = (SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.poolId = ph.id))"
              + " left join PoolUpdate pu ON ph.id = pu.poolHashId AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHashId = ph.id)"
              + " LEFT JOIN PoolInfo pi ON ph.id = pi.poolId AND pi.fetchedAtEpoch = :epochNo "
              + " left join AggregatePoolInfo api on api.poolId = ph.id ")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
  - pool_info
  - aggregate_pool_info
#### getPoolRangeWithoutUsingKoi0s
- query:
    ```sql
    @Query(
      value =
          " SELECT min(pu.pledge) as minPledge, max(pu.pledge) as maxPledge,"
              + " min(api.votingPower) as minVotingPower, max(api.votingPower) as maxVotingPower,"
              + " min (api.governanceParticipationRate) as minGovParticipationRate, max(api.governanceParticipationRate) as maxGovParticipationRate,"
              + " min (api.blockLifeTime) as minLifetimeBlock, max(api.blockLifeTime) as maxLifetimeBlock"
              + " FROM PoolHash ph"
              + " left join PoolUpdate pu ON ph.id = pu.poolHashId AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHashId = ph.id)"
              + " left join AggregatePoolInfo api on api.poolId = ph.id ")
    ```
- related table:
  - pool_hash
  - pool_update
  - aggregate_pool_info
</details>

### Related table:
- slot_leader
- block
- pool_hash
- pool_offline_data
- pool_update
- aggregate_pool_info
- stake_address
- epoch_param
- ada_pots
- tx
- epoch_param
- delegation
- pool_info
- aggregate_pool_info

## 31. PoolHistoryCheckpointRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### checkRewardByPoolViewAndEpoch
- query:
    ```sql
    @Query(
      value =
          "SELECT COUNT(cp.id) FROM PoolHistoryCheckpoint cp "
              + "WHERE cp.view IN :poolViews AND cp.epochCheckpoint = "
              + "(SELECT max(e.no) - 1 FROM Epoch e) AND cp.isSpendableReward = TRUE")
    ```
</details>

### Related table:
- pool_history_checkpoint

## 32. PoolHistoryRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getPoolHistoryKoios
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.epochNo AS epochNo, ph.delegatorRewards AS delegateReward, ph.epochRos AS ros, "
              + "ph.activeStake AS activeStake, ph.poolFees AS poolFees "
              + "FROM PoolHistory ph "
              + "WHERE ph.pool.view = :poolId "
              + "ORDER BY ph.epochNo DESC")
    ```
#### getPoolHistoryKoios
- query:
    ```sql
     @Query(
      value =
          "SELECT ph.epochNo AS epochNo, ph.delegatorRewards AS delegateReward, ph.epochRos AS ros, "
              + "ph.activeStake AS activeStake, ph.poolFees AS poolFees "
              + "FROM PoolHistory ph "
              + "WHERE ph.pool.view = :poolId "
              + "ORDER BY ph.epochNo DESC")
    ```
#### getPoolHistoryKoiosForEpochChart
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.epochNo AS chartKey, ph.activeStake AS chartValue "
              + "FROM PoolHistory ph "
              + "WHERE ph.pool.view = :poolId "
              + "ORDER BY ph.epochNo ASC")
    ```
#### getDataForDelegatorChart
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.epochNo AS chartKey, CAST(ph.delegatorCnt as long) AS chartValue "
              + "FROM PoolHistory ph "
              + "WHERE ph.pool.view = :poolId "
              + "ORDER BY ph.epochNo ASC")
    ```
</details>

### Related table:
- pool_history

## 33. PoolInfoRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getTotalLiveStake
- query:
    ```sql
    @Query(value = "SELECT SUM(pi.liveStake) FROM PoolInfo pi WHERE pi.fetchedAtEpoch = :epochNo")
    ```
#### getPoolInfoKoios
- query:
    ```sql
    @Query(
      value =
          "SELECT pi.pool.view AS view, pi.activeStake AS activeStake, pi.liveSaturation AS saturation "
              + "FROM PoolInfo pi "
              + "WHERE pi.pool.view IN :poolIds AND pi.fetchedAtEpoch = :epochNo")
    ```
#### getActiveStakeByPoolAndEpoch
- query:
    ```sql
    @Query(
      value =
          "SELECT pi.activeStake FROM PoolInfo pi "
              + "WHERE (pi.pool.view = :poolId OR pi.pool.hashRaw = :poolId) AND pi.fetchedAtEpoch = :epochNo")
    ```
#### getTotalActiveStake
- query:
    ```sql
     @Query(value = "SELECT SUM(pi.activeStake) FROM PoolInfo pi WHERE pi.fetchedAtEpoch = :epochNo")
    ```
</details>

### Related table:
- pool_info

## 34. PoolOwnerRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getStakeKeyList
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS poolId, sa.view AS address "
              + "FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id AND pu.id = "
              + "(SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE ph.id = pu2.poolHash.id) "
              + "JOIN PoolOwner po ON po.poolUpdate.id  = pu.id  "
              + "JOIN StakeAddress sa ON sa.id = po.stakeAddress.id "
              + "WHERE ph.id IN :poolIds "
              + "GROUP BY ph.id, sa.view ")
    ```
- related table:
  - pool_hash
  - pool_update
  - stake_address
</details>

### Related table:
- pool_hash
- pool_update
- pool_owner
- stake_address

## 35. PoolRelayRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByPoolHashIdIn
- query:
    ```sql
    @Query(
      "SELECT pr.poolUpdate.id as poolUpdateId, pr.dnsName as dnsName, "
          + " pr.dnsSrvName as dnsSrvName, pr.ipv4 as ipv4, pr.ipv6 as ipv6, pr.port as port"
          + " FROM PoolRelay pr WHERE pr.poolUpdate.id IN :poolUpdateIds")
    ```
</details>

### Related table:
- pool_relay

## 36. PoolRetireRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByAnnouncedTx
- query:
    ```sql
    @Query(
      value =
          "SELECT pr.announcedTx.id as txId, pr.retiringEpoch as retiringEpoch, "
              + "ph.view as poolId "
              + "FROM PoolRetire pr "
              + "INNER JOIN PoolHash ph ON pr.poolHash.id = ph.id "
              + "WHERE pr.announcedTx = :tx")
    ```
- related table:
  - pool_hash
#### getDataForPoolDeRegistration
- query:
    ```sql
    @Query(
      value =
          "SELECT pr.announcedTxId as txId, pu.pledge AS pledge, pu.margin AS margin, "
              + "pu.fixedCost AS cost, pu.poolHash.id AS poolId, po.poolName AS poolName, ph.view AS poolView "
              + "FROM PoolRetire pr "
              + "JOIN PoolHash ph ON pr.poolHash.id = ph.id "
              + "LEFT JOIN PoolOfflineData po ON pr.poolHash.id  = po.pool.id AND (po.id is NULL OR po.id = "
              + "(SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.pool.id  = pr.poolHash.id)) "
              + "LEFT JOIN PoolUpdate pu ON pr.poolHash.id = pu.poolHash.id "
              + "AND (pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE pr.poolHash.id  = pu2.poolHash.id))",
      countQuery = "SELECT count(pr) FROM PoolRetire pr")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - pool_update
#### getPoolDeRegistration
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.fee AS fee, pr.retiringEpoch AS retiringEpoch, tx.hash AS txHash, bk.time AS time, "
              + "CASE "
              + "WHEN tx.id = (SELECT max(pr1.announcedTx.id) FROM PoolRetire pr1 WHERE pr1.announcedTx.id = tx.id AND pr.retiringEpoch = pr1.retiringEpoch) THEN TRUE "
              + "ELSE FALSE "
              + "END AS refundFlag "
              + "FROM PoolRetire pr "
              + "JOIN Tx tx ON pr.announcedTx.id  = tx.id "
              + "JOIN Block bk ON tx.block.id = bk.id "
              + "WHERE pr.id IN :poolRetiredIds "
              + "AND (:txHash IS NULL OR tx.hash = :txHash) "
              + "AND (CAST(:fromDate AS timestamp) IS NULL OR bk.time >= :fromDate) "
              + "AND (CAST(:toDate AS timestamp) IS NULL OR bk.time <= :toDate) ",
      countQuery =
          "SELECT pr.id "
              + "FROM PoolRetire pr "
              + "JOIN Tx tx ON pr.announcedTx.id  = tx.id "
              + "JOIN Block bk ON tx.block.id = bk.id "
              + "WHERE pr.id IN :poolRetiredIds "
              + "AND (:txHash IS NULL OR tx.hash = :txHash) "
              + "AND (CAST(:fromDate AS timestamp) IS NULL OR bk.time >= :fromDate) "
              + "AND (CAST(:toDate AS timestamp) IS NULL OR bk.time <= :toDate)")
    ```
- related table:
  - tx
  - block
#### findByPoolView
- query:
    ```sql
    @Query(
      value =
          "SELECT pr.retiringEpoch FROM PoolRetire pr "
              + "JOIN PoolHash ph ON pr.poolHash.id  = ph.id "
              + "WHERE ph.view = :poolView OR ph.hashRaw = :poolView "
              + "ORDER BY pr.id DESC")
    ```
- related table:
  - pool_hash
#### existsByPoolHash
#### getPoolRetireByPoolViewOrHash
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.id as txId, tx.hash as txHash, b.epochNo as txEpochNo,"
              + "pr.retiringEpoch as certEpochNo, pr.certIndex as certIndex, pr.id as poolRetireId, "
              + "b.time as blockTime, b.blockNo as blockNo, b.epochSlotNo as epochSlotNo, b.slotNo as slotNo "
              + "FROM PoolRetire pr "
              + "JOIN Tx tx on pr.announcedTx = tx "
              + "JOIN Block b on tx.block = b "
              + "WHERE pr.poolHash.view = :poolViewOrHash "
              + "OR pr.poolHash.hashRaw = :poolViewOrHash ")
    ```
- related table:
  - tx
  - block
#### getLastPoolRetireByPoolHash
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.id as txId, tx.hash as txHash, b.epochNo as txEpochNo,"
              + "pr.retiringEpoch as certEpochNo, pr.certIndex as certIndex, pr.id as poolRetireId, "
              + "b.time as blockTime, b.blockNo as blockNo, b.epochSlotNo as epochSlotNo, b.slotNo as slotNo "
              + "FROM PoolRetire pr "
              + "JOIN Tx tx on pr.announcedTx = tx "
              + "JOIN Block b on tx.block = b "
              + "WHERE pr.poolHash.view = :poolViewOrHash "
              + "OR pr.poolHash.hashRaw = :poolViewOrHash "
              + "ORDER BY tx.id DESC, pr.certIndex DESC "
              + "LIMIT 1")
    ```
- related table:
  - tx
  - block
</details>

### Related table:
- pool_retire
- pool_hash
- pool_offline_data
- pool_update
- tx
- block

## 37. PoolUpdateRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findOwnerAccountByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.view FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id AND pu.id = (SELECT max(pu2.id) FROM PoolUpdate pu2 WHERE ph.id = pu2.poolHash.id) "
              + "JOIN PoolOwner po ON pu.id = po.poolUpdate.id "
              + "JOIN StakeAddress sa ON po.stakeAddress.id = sa.id "
              + "WHERE ph.id  = :poolId ")
    ```
- related table:
  - pool_owner
  - stake_address
#### findOwnerAccountByPoolView
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.view FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id "
              + "JOIN PoolOwner po ON pu.id = po.poolUpdate.id "
              + "JOIN StakeAddress sa ON po.stakeAddress.id = sa.id "
              + "WHERE (ph.view  = :poolView OR ph.hashRaw = :poolView) "
              + "GROUP BY sa.view")
    ```
- related table:
  - pool_owner
  - stake_address
#### findByTx
- query:
    ```sql
     @Query(
      "SELECT pu.id AS poolUpdateId, ph.view AS poolView, pu.pledge AS pledge, "
          + "pu.margin AS margin, pu.vrfKeyHash AS vrfKey, pu.fixedCost  AS cost, sa.view AS rewardAccount, "
          + "pmr.url AS metadataUrl, pmr.hash as metadataHash "
          + "FROM PoolUpdate pu "
          + "INNER JOIN PoolHash ph ON pu.poolHash.id = ph.id "
          + "LEFT JOIN PoolMetadataRef pmr ON pu.meta = pmr "
          + "INNER JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
          + "WHERE pu.registeredTx = :tx")
    ```
- related table:
  - pool_hash
  - pool_metadata_ref
  - stake_address
#### getDataForPoolRegistration
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.registeredTxId AS txId, pu.pledge AS pledge, pu.margin AS margin, pu.fixedCost AS cost, "
              + "pu.poolHash.id AS poolId, po.poolName AS poolName, ph.view AS poolView "
              + "FROM PoolUpdate pu  "
              + "JOIN PoolHash ph ON pu.poolHash.id = ph.id "
              + "LEFT JOIN PoolOfflineData po ON pu.poolHash.id = po.pool.id AND (po.id is NULL OR po.id = "
              + "(SELECT max(po2.id) FROM PoolOfflineData po2 WHERE po2.pool.id  = pu.poolHash.id)) ",
      countQuery = "SELECT count(pu) FROM PoolUpdate pu")
    ```
- related table:
  - pool_hash
  - pool_offline_data
#### getCreatedTimeOfPool
- query:
    ```sql
    @Query(
      value =
          "SELECT bk.time FROM PoolUpdate pu "
              + "JOIN Tx t ON pu.registeredTx.id = t.id "
              + "JOIN Block bk ON t.block.id = bk.id "
              + "WHERE pu.id = (SELECT min(pu2.id) FROM PoolUpdate pu2 WHERE pu2.poolHash.id = :poolId) "
              + "AND pu.poolHash.id = :poolId ")
    ```
- related table:
  - tx
  - block
#### findOwnerAccountByPoolUpdate
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.id AS poolUpdateId, sa.view AS view FROM PoolUpdate pu "
              + "JOIN PoolOwner po ON pu.id = po.poolUpdate.id "
              + "JOIN StakeAddress sa ON po.stakeAddress.id = sa.id "
              + "WHERE pu.id IN :poolUpdateIds ")
    ```
- related table:
  - pool_owner
  - stake_address
#### findPoolUpdateByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.id AS poolUpdateId, tx.hash AS txHash, tx.fee AS fee, bk.time AS time, pu.margin AS margin "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx ON pu.registeredTx.id  = tx.id "
              + "JOIN Block bk ON tx.blockId = bk.id "
              + "WHERE pu.id IN :poolCertificateIds "
              + "AND (:txHash IS NULL OR tx.hash = :txHash) "
              + "AND (CAST(:fromDate AS timestamp) IS NULL OR bk.time >= :fromDate) "
              + "AND (CAST(:toDate AS timestamp) IS NULL OR bk.time <= :toDate) ")
    ```
- related table:
  - tx
  - block
#### findPoolUpdateDetailById
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.id AS hashId, ph.hashRaw AS poolId , ph.view AS poolView, pod.poolName AS poolName, "
              + "pu.pledge AS pledge, pu.margin AS margin, pu.vrfKeyHash AS vrfKey, pu.fixedCost  AS cost, "
              + "tx.hash AS txHash, bk.time AS time, tx.fee AS fee, sa.view AS rewardAccount, tx.deposit AS deposit "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData pod ON ph.id = pod.pool.id AND pod.id = (SELECT max(pod2.id) FROM PoolOfflineData pod2 WHERE ph.id = pod2.pool.id) "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id "
              + "JOIN Tx tx ON pu.registeredTx.id = tx.id "
              + "JOIN Block bk ON tx.block.id  = bk.id "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id  = sa.id "
              + "WHERE pu.id = :id ")
    ```
- related table:
  - pool_hash
  - pool_offline_data
  - tx
  - block
  - stake_address
#### findOwnerAccountByPoolUpdate
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.view FROM PoolUpdate pu "
              + "JOIN PoolOwner po ON pu.id = po.poolUpdate.id "
              + "JOIN StakeAddress sa ON po.stakeAddress.id = sa.id "
              + "WHERE pu.id  = :id ")
    ```
- related table:
  - pool_owner
  - stake_address
> **_NOTE:_** Should be changed to PoolOwnerRepository.
#### findTopByIdLessThanAndPoolHashIdOrderByIdDesc
#### findPoolUpdateByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.id AS poolUpdateId, ph.id AS hashId, ph.hashRaw AS poolId , ph.view AS poolView, pod.poolName AS poolName, pu.pledge AS pledge, pu.margin AS margin, pu.vrfKeyHash AS vrfKey, pu.fixedCost  AS cost, tx.hash AS txHash, bk.time AS time, tx.fee AS fee, sa.view AS rewardAccount, tx.deposit AS deposit "
              + "FROM PoolHash ph "
              + "LEFT JOIN PoolOfflineData pod ON ph.id = pod.pool.id AND pod.id = (SELECT max(pod2.id) FROM PoolOfflineData pod2 WHERE ph.id = pod2.pool.id) "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id "
              + "JOIN Tx tx ON pu.registeredTx.id = tx.id "
              + "JOIN Block bk ON tx.block.id  = bk.id "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id  = sa.id "
              + "WHERE pu.id IN :poolCertificateIds ")
    ```
- related table:
  - pool_offline_data
  - tx
  - block
  - stake_address
#### findPoolByRewardAccount
- query:
    ```sql
    @Query(
      "SELECT poolHash.view FROM PoolUpdate poolUpdate "
          + "INNER JOIN PoolHash poolHash ON poolUpdate.poolHash = poolHash "
          + "WHERE poolUpdate.rewardAddr = :stakeAddress "
          + "AND poolUpdate.registeredTx.id = "
          + "(SELECT max(poolUpdate2.registeredTx.id) "
          + "FROM PoolUpdate poolUpdate2 "
          + "WHERE poolUpdate2.poolHash = poolHash) "
          + "AND (SELECT COALESCE(max(poolRetire.retiringEpoch), 0) + 2 "
          + "FROM PoolRetire poolRetire WHERE poolRetire.poolHash = poolHash) < poolUpdate.activeEpochNo")
    ```
- related table:
  - pool_hash
  - pool_retire
#### findPoolRegistrationByPool
- query:
    ```sql
    @Query(
      value =
          "SELECT pu.id AS poolUpdateId, tx.hash AS txHash, tx.fee AS fee, bk.time AS time, pu.margin AS margin, ep.poolDeposit AS deposit "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx ON pu.registeredTx.id  = tx.id "
              + "JOIN Block bk ON tx.blockId = bk.id "
              + "JOIN EpochParam ep ON ep.epochNo = bk.epochNo "
              + "WHERE pu.id IN :poolCertificateIds "
              + "AND (:txHash IS NULL OR tx.hash = :txHash) "
              + "AND (CAST(:fromDate AS timestamp) IS NULL OR bk.time >= :fromDate) "
              + "AND (CAST(:toDate AS timestamp) IS NULL OR bk.time <= :toDate) ")
    ```
- related table:
  - tx
  - block
  - epoch_param
#### findRewardAccountByPoolView
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.view FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash) "
              + "GROUP BY sa.view")
    ```
- related table:
  - stake_address
#### findRewardAccountByPoolId
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.view FROM PoolHash ph "
              + "JOIN PoolUpdate pu ON ph.id = pu.poolHash.id "
              + "JOIN StakeAddress sa ON pu.rewardAddr.id = sa.id "
              + "WHERE ph.id = :poolId "
              + "GROUP BY sa.view")
    ```
- related table:
  - stake_address
#### TEMPLATE_QUERY
- query:
    ```sql
    @Query(
      value =
          "SELECT COUNT(pu.id) FROM PoolUpdate pu "
              + "JOIN PoolHash ph ON pu.poolHash.id = ph.id "
              + "WHERE (ph.view = :poolViewOrHash "
              + "OR ph.hashRaw = :poolViewOrHash)")
    ```
- related table:
  - pool_hash
#### getPoolUpdateByPoolViewOrHash
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.id as txId, tx.hash as txHash, b.epochNo as txEpochNo,"
              + "pu.activeEpochNo as certEpochNo, pu.certIndex as certIndex, pu.id as poolUpdateId, "
              + "b.time as blockTime, b.blockNo as blockNo, b.epochSlotNo as epochSlotNo, b.slotNo as slotNo "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx on pu.registeredTx = tx "
              + "JOIN Block b on tx.block = b "
              + "WHERE pu.poolHash.view = :poolViewOrHash "
              + "OR pu.poolHash.hashRaw = :poolViewOrHash ")
    ```
- related table:
  - tx
  - block
#### getLastPoolUpdateByPoolHash
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.id as txId, tx.hash as txHash, b.epochNo as txEpochNo,"
              + "pu.activeEpochNo as certEpochNo, pu.certIndex as certIndex, pu.id as poolUpdateId, "
              + "b.time as blockTime, b.blockNo as blockNo, b.epochSlotNo as epochSlotNo, b.slotNo as slotNo "
              + "FROM PoolUpdate pu "
              + "JOIN Tx tx on pu.registeredTx = tx "
              + "JOIN Block b on tx.block = b "
              + "WHERE pu.poolHash.view = :poolViewOrHash "
              + "OR pu.poolHash.hashRaw = :poolViewOrHash "
              + "ORDER BY tx.id DESC, pu.certIndex DESC "
              + "LIMIT 1")
    ```
- related table:
  - tx
  - block
  - pool_hash
</details>

### Related table:
- pool_update
- pool_owner
- pool_retire
- stake_address
- pool_metadata_ref
- tx
- block
- epoch_param
- pool_hash

## 38. SlotLeaderRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByIdIn

</details>

## 39. StakeAddressRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByView
#### findStakeAddressOrderByBalance
- query:
    ```sql
    @Query(
      value =
          "SELECT sa.id as id, sa.view as stakeAddress, lsab.quantity as totalStake"
              + " FROM StakeAddress sa"
              + " LEFT JOIN LatestStakeAddressBalance lsab ON sa.view = lsab.address"
              + " WHERE EXISTS (SELECT d FROM Delegation d WHERE d.address = sa)"
              + " AND (SELECT max(sr.txId) FROM StakeRegistration sr WHERE sr.addr = sa) >"
              + " (SELECT COALESCE(max(sd.txId), 0) FROM StakeDeregistration sd WHERE sd.addr = sa)"
              + " AND lsab.quantity IS NOT NULL"
              + " ORDER BY totalStake DESC")
    ```
- related table:
  - latest_stake_address_balance
  - delegation
  - stake_registration
#### getPoolViewByStakeKey
- query:
    ```sql
    @Query(
      value =
          "SELECT ph.view FROM StakeAddress sa "
              + "JOIN PoolOwner po ON sa.id  = po.stakeAddress.id "
              + "JOIN PoolUpdate pu ON po.poolUpdate.id  = pu.id "
              + "JOIN PoolHash ph ON pu.poolHash.id = ph.id "
              + "WHERE sa.view = :stakeKey "
              + "GROUP BY ph.view ")
    ```
- related table:
  - pool_owner
  - pool_update
  - pool_hash
#### getViewByAddressId
- query:
    ```sql
    @Query(value = "SELECT sa.view FROM StakeAddress sa WHERE sa.id IN :addressIds")
    ```
#### getStakeAssociatedAddress
- query:
    ```sql
    @Query("SELECT stake.view" + " FROM StakeAddress stake" + " WHERE stake.scriptHash = :scriptHash")
    ```
#### getBalanceByView
- query:
    ```sql
    @Query(
      value =
          "SELECT COALESCE(SUM(lsab.quantity), 0) FROM LatestStakeAddressBalance lsab WHERE lsab.address IN :views")
    ```
- related table:
  - latest_stake_address_balance
#### getAssociatedAddress
- query:
    ```sql
    @Query(value = "SELECT sa.view FROM StakeAddress sa WHERE sa.scriptHash = :scriptHash")
    ```
</details>

### Related table:
- stake_address
- latest_stake_address_balance
- delegation
- stake_registration
- pool_owner
- pool_update
- pool_hash

## 40. StakeAddressTxCountRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findById

</details>

## 41. StakeDeRegistrationRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findMaxTxIdByStake
- query:
    ```sql
    @Query(
      "SELECT max(stakeDeregis.tx.id) "
          + " FROM StakeDeregistration stakeDeregis"
          + " WHERE stakeDeregis.addr = :stake")
    ```
#### getStakeDeRegistrationsByAddress
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time,"
              + " b.epochSlotNo as epochSlotNo, b.blockNo as blockNo, b.epochNo as epochNo, b.slotNo as slotNo,"
              + " 'De Registered' AS action, tx.blockIndex as blockIndex, tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeDeregistration sd"
              + " JOIN Tx tx ON tx.id = sd.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " WHERE sd.addr = :stakeKey"
              + " ORDER BY b.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - tx
  - block
#### getStakeDeRegistrationsByAddress
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time,"
              + " b.epochSlotNo as epochSlotNo, b.blockNo as blockNo, b.epochNo as epochNo,"
              + " 'De Registered' AS action, tx.blockIndex as blockIndex, tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeDeregistration dr"
              + " JOIN Tx tx ON tx.id = dr.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " WHERE dr.addr = :stakeKey"
              + " AND (b.time >= :fromTime ) "
              + " AND (b.time <= :toTime)"
              + " AND ( :txHash IS NULL OR tx.hash = :txHash)")
    ```
- related table:
  - tx
  - block
#### findByAddressAndTx
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time, b.epochNo as epochNo,"
              + " tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeDeregistration sd"
              + " JOIN Tx tx ON tx.id = sd.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " JOIN StakeAddress sa ON sa.id = sd.addr.id"
              + " WHERE sa.view = :stakeKey"
              + " AND tx.hash = :txHash")
    ```
- related table:
  - tx
  - block
  - stake_address
#### findByTx
- related table:
  - stake_address
#### existsByAddr

</details>

### Related table:
- stake_deregistration
- tx
- block
- stake_address

## 42. StakeRegistrationRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findMaxTxIdByStake
- query:
    ```sql
    @Query(
      "SELECT max(stakeRegis.tx.id) "
          + " FROM StakeRegistration stakeRegis"
          + " WHERE stakeRegis.addr = :stake")
    ```
#### getStakeRegistrationsByAddress
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time, b.slotNo as slotNo,"
              + " b.epochSlotNo as epochSlotNo, b.blockNo as blockNo, b.epochNo as epochNo,"
              + " 'Registered' AS action, tx.blockIndex as blockIndex, tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeRegistration sr"
              + " JOIN Tx tx ON tx.id = sr.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " WHERE sr.addr = :stakeKey"
              + " ORDER BY b.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - tx
  - block
#### getStakeRegistrationsByAddress
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time,"
              + " b.epochSlotNo as epochSlotNo, b.blockNo as blockNo, b.epochNo as epochNo,"
              + " 'Registered' AS action, tx.blockIndex as blockIndex, tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeRegistration sr"
              + " JOIN Tx tx ON tx.id = sr.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " WHERE sr.addr = :stakeKey"
              + " AND (b.time >= :fromTime ) "
              + " AND (b.time <= :toTime)"
              + " AND ( :txHash IS NULL OR tx.hash = :txHash)")
    ```
- related table:
  - tx
  - block
#### findByAddressAndTx
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as txHash, b.time as time, b.epochNo as epochNo,"
              + " tx.fee as fee, tx.deposit as deposit"
              + " FROM StakeRegistration sr"
              + " JOIN Tx tx ON tx.id = sr.tx.id"
              + " JOIN Block b ON b.id = tx.blockId"
              + " JOIN StakeAddress sa ON sa.id = sr.addr.id"
              + " WHERE sa.view = :stakeKey"
              + " AND tx.hash = :txHash")
    ```
- related table:
  - tx
  - block
  - stake_address
#### findByTx
- related table:
  - stake_address
#### existsByAddr

</details>

### Related table:
- stake_registration
- tx
- block
- stake_address

## 43. StakeTxBalanceRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findMinMaxBalanceByStakeAddress
- query:
    ```sql
    @Query(
      value =
          """
        select :fromBalance + coalesce(min(calculated_balances.sum_balance), 0) as minVal,
               :fromBalance + coalesce(max(calculated_balances.sum_balance), 0) as maxVal
        from (select sum(stb.balance_change) over (order by stb.tx_id rows unbounded PRECEDING) as sum_balance
              from stake_tx_balance stb
              where stb.stake_address_id = :addressId
                and stb.time > :fromDate
                and stb.time <= :toDate) as calculated_balances
        """,
      nativeQuery = true)
    ```
#### findMaxTxIdByStakeAddressId
- query:
    ```sql
    @Query(
      value =
          "select MAX(stb.txId)"
              + " from StakeTxBalance stb "
              + " where stb.stakeAddressId = :stakeAddressId ")
    ```
</details>

### Related table:
- stake_tx_balance

## 44. TokenTxCountRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findById

</details>

## 45. TreasuryRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getTreasuryByAddress
- query:
    ```sql
    @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo, block.slotNo as slotNo,"
          + " block.blockNo as blockNo, tx.blockIndex as blockIndex, block.epochNo as epochNo,"
          + " treasury.amount as amount"
          + " FROM Treasury treasury"
          + " INNER JOIN Tx tx ON treasury.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " INNER JOIN StakeAddress stake ON treasury.addr = stake"
          + " WHERE stake.view = :stakeKey"
          + " ORDER BY block.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - tx
  - block
  - stake_address
#### findByTx
- query:
    ```sql
    @Query(
      "SELECT stake.view as stakeAddress, treasury.amount as amount"
          + " FROM Treasury treasury"
          + " INNER JOIN StakeAddress stake ON treasury.addr = stake"
          + " WHERE treasury.tx = :tx"
          + " ORDER BY treasury.amount DESC")
    ```
- related table:
  - stake_address
  - tx
#### findAllTx
- query:
    ```sql
    @Query(
      "SELECT treasury.tx.id as txId, count(DISTINCT treasury.addr) as numberOfStakes, sum(treasury.amount) as rewards"
          + " FROM Treasury treasury"
          + " GROUP BY treasury.tx.id")
    ```
- related table:
  - tx
</details>

### Related table:
- treasury
- tx
- block
- stake_address

## 46. TxBootstrapWitnessesRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAllByTx
- related table:
  - tx
</details>

### Related table:
- tx_bootstrap_witnesses
- tx

## 47. TxChartRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### getTransactionGraphByHour
- query:
    ```sql
    @Query(
      "SELECT SUM(tx.txSimple) AS simpleTransactions,"
          + " SUM(tx.txWithMetadataWithoutSc) AS metadata,"
          + "SUM(tx.txWithSc) AS smartContract, "
          + "tx.hour AS time FROM TxChart tx "
          + "WHERE tx.hour in :hours "
          + "GROUP BY tx.hour "
          + "ORDER BY tx.hour ASC ")
    ```
#### getTransactionGraphByDay
- query:
    ```sql
    @Query(
      "SELECT SUM(tx.txSimple) AS simpleTransactions,"
          + " SUM(tx.txWithMetadataWithoutSc) AS metadata ,"
          + "SUM(tx.txWithSc) AS smartContract, "
          + "tx.day AS time FROM TxChart tx "
          + "WHERE tx.day in :day "
          + "GROUP BY tx.day "
          + "ORDER BY tx.day ASC ")
    ```
#### getTransactionGraphDayGreaterThan
- query:
    ```sql
    @Query(
      "SELECT SUM(tx.txSimple) AS simpleTransactions,"
          + " SUM(tx.txWithMetadataWithoutSc) AS  metadata,"
          + "SUM(tx.txWithSc) AS smartContract, "
          + "tx.day AS time FROM TxChart tx "
          + "WHERE tx.day >= :day "
          + "GROUP BY tx.day "
          + "ORDER BY tx.day ASC")
    ```
</details>

### Related table:
- tx_chart

## 48. TxMetadataRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAllByTxOrderByKeyAsc
#### findAllByTxHash
- query:
    ```sql
    @Query("SELECT t FROM TxMetadata t WHERE t.tx.hash = :txHash")
    ```
</details>

## 49. TxOutRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAddressOutputListByTxId
- query:
    ```sql
    @Query(
      "SELECT tx.id AS txId, txOut.address AS address"
          + " FROM TxOut txOut"
          + " INNER JOIN Tx tx ON tx.id = txOut.tx.id"
          + " WHERE tx.id IN :txIds"
          + " ORDER BY txOut.index ASC")
    ```
- related table:
  - tx
#### findAddressInputListByTxId
- query:
    ```sql
    @Query(
      "SELECT tx.id AS txId, txOut.address AS address"
          + "   FROM TxOut txOut "
          + "   INNER JOIN TxIn txIn ON txOut.tx.id = txIn.txOut.id"
          + "   INNER JOIN Tx tx ON tx.id = txIn.txInput.id AND txIn.txOutIndex = txOut.index"
          + "   WHERE tx.id IN :txIds"
          + "   ORDER BY txIn.id ASC")
    ```
- related table:
  - tx_in
  - tx
#### getTxAddressOutputInfo
- query:
    ```sql
    @Query(
      "SELECT txOut.address AS address, txOut.index as index, COALESCE(stake.view, txOut.address) AS stakeAddress,"
          + "   txOut.value AS value, maTxOut.quantity as assetQuantity,"
          + "   stake.view as stakeView, "
          + "   asset.name as assetName, asset.fingerprint as assetId, asset.id as multiAssetId"
          + " FROM TxOut txOut "
          + " LEFT JOIN StakeAddress stake ON txOut.stakeAddress = stake "
          + " LEFT JOIN MaTxOut maTxOut ON maTxOut.txOut = txOut"
          + " LEFT JOIN MultiAsset asset ON maTxOut.ident = asset"
          + " WHERE txOut.tx = :tx"
          + " ORDER BY txOut.index ASC")
    ```
- related table:
  - stake_address
  - ma_tx_out
  - multi_asset
#### getTxAddressInputInfo
- query:
    ```sql
    @Query(
      "SELECT txOut.address AS address, txOut.index as index, txIn.txOut.hash AS txHash,"
          + "   COALESCE(stake.view,txOut.address) AS stakeAddress,"
          + "   stake.view as stakeView, "
          + "   txOut.value AS value, maTxOut.quantity as assetQuantity,"
          + "   asset.name as assetName, asset.fingerprint as assetId, asset.id as multiAssetId"
          + " FROM TxOut txOut "
          + " INNER JOIN TxIn txIn ON txOut.tx = txIn.txOut AND txIn.txOutIndex = txOut.index "
          + " LEFT JOIN StakeAddress stake ON txOut.stakeAddress = stake"
          + " LEFT JOIN MaTxOut maTxOut ON maTxOut.txOut = txOut"
          + " LEFT JOIN MultiAsset asset ON maTxOut.ident = asset"
          + " WHERE txIn.txInput = :tx"
          + " ORDER BY txIn.id ASC")
    ```
- related table:
  - tx_in
  - stake_address
  - ma_tx_out
  - multi_asset
#### getContractDatumOutByTx
- query:
    ```sql
    @Query(
      "SELECT txOut.id as txOutId, txOut.address as address, d.hash as datumHashOut, d.bytes as datumBytesOut"
          + " FROM TxOut txOut"
          + " JOIN Datum d ON (txOut.dataHash = d.hash OR txOut.inlineDatum = d)"
          + " WHERE txOut.tx = :tx"
          + " ORDER BY txOut.index DESC")
    ```
- related table:
  - datum
#### getContractDatumOutByTxFail
- query:
    ```sql
    @Query(
      "SELECT failedTxOut.id as txOutId, failedTxOut.address as address, d.hash as datumHashOut, d.bytes as datumBytesOut"
          + " FROM FailedTxOut failedTxOut"
          + " JOIN Datum d ON (failedTxOut.dataHash = d.hash OR failedTxOut.inlineDatum = d)"
          + " WHERE failedTxOut.tx = :tx"
          + " ORDER BY failedTxOut.index DESC")
    ```
- related table:
  - failed_tx_out
  - datum
#### sumValueOutputByTxAndStakeAddress
- query:
    ```sql
    @Query(
      "SELECT COALESCE(sum(txOut.value), 0) FROM TxOut txOut "
          + "INNER JOIN Tx tx ON tx.id = txOut.tx.id "
          + "INNER JOIN StakeAddress stake ON txOut.stakeAddress = stake "
          + "WHERE tx.hash = :txHash AND stake.view = :stakeAddress")
    ```
- related table:
  - tx
  - stake_address
#### sumValueInputByTxAndStakeAddress
- query:
    ```sql
    @Query(
      "SELECT COALESCE(sum(txOut.value), 0) "
          + "FROM TxOut txOut "
          + "INNER JOIN TxIn txIn ON txOut.tx.id = txIn.txOut.id "
          + "INNER JOIN Tx tx ON tx.id = txIn.txInput.id AND txIn.txOutIndex = txOut.index "
          + "INNER JOIN StakeAddress stake ON txOut.stakeAddress = stake "
          + "WHERE tx.hash = :txHash AND stake.view = :stakeAddress")
    ```
- related table:
  - tx_in
  - tx
  - stake_address
</details>

### Related table:
- tx_out
- tx
- tx_in
- stake_address
- ma_tx_out
- multi_asset
- datum
- failed_tx_out

## 50. TxRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAllTx
- query:
    ```sql
    @Query(
      value = "SELECT tx FROM Tx tx WHERE tx.blockId IS NOT NULL",
      countQuery = "SELECT sum(e.txCount) FROM Epoch e")
    ```
- related table:
  - epoch
#### findByBlockIn
#### findAllByBlock
#### findByBlockNo
- query:
    ```sql
    @Query(
      "SELECT tx FROM Tx tx INNER JOIN Block b ON b.id = tx.blockId "
          + "WHERE b.blockNo = :blockNo")
    ```
- related table:
  - block
#### findByBlockHash
- query:
    ```sql
    @Query(
      "SELECT tx FROM Tx tx INNER JOIN Block b ON b.id = tx.blockId " + "WHERE b.hash = :blockHash")
    ```
#### findByHash
- related table:
  - block
  - tx_metadata_hash
#### findLatestTxId
- query:
    ```sql
    @Query(value = "SELECT tx.id FROM Tx tx ")
    ```
#### findLatestTxIO
- query:
    ```sql
    @Query(
      value =
          "SELECT tx.hash as hash, "
              + "b.blockNo as blockNo, "
              + "outp.address as toAddress, "
              + "inp.address as fromAddress, "
              + "tx.outSum as amount,"
              + "tx.validContract as validContract, "
              + "b.time as time, "
              + "b.epochNo as epochNo, "
              + "b.epochSlotNo as epochSlotNo, "
              + "b.slotNo as slot "
              + "FROM Tx tx "
              + "JOIN Block b ON b.id = tx.blockId "
              + "JOIN TxIn txi ON txi.txInputId = tx.id "
              + "LEFT JOIN TxOut outp ON outp.tx.id = tx.id "
              + "LEFT JOIN TxOut inp ON inp.tx.id = txi.txOutputId AND "
              + "inp.index = txi.txOutIndex "
              + "WHERE tx.id IN :txIds "
              + "ORDER BY b.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - block
  - tx_in
  - tx_out
#### findByIdIn
- query:
    ```sql
    @Query("SELECT tx FROM Tx tx WHERE tx.id IN :ids ORDER BY tx.blockId DESC, tx.blockIndex DESC")
    ```
- related table:
  - block
#### findTxIn
- query:
    ```sql
    @Query(
      "SELECT tx.id as id, "
          + "tx.hash as hash, "
          + "b.blockNo as blockNo, "
          + "b.time as time, "
          + "b.epochNo as epochNo, "
          + "b.epochSlotNo as epochSlotNo, "
          + "b.slotNo as slot "
          + "FROM Tx tx "
          + "JOIN Block b ON b.id = tx.blockId "
          + "WHERE tx.id IN :txIds "
          + "ORDER BY b.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - block
#### getSmartContractTxsByTxIds
- query:
    ```sql
    @Query(
      "SELECT tx.id as txId, tx.hash as hash, b.time as time, b.blockNo as blockNo,  "
          + " b.epochNo as epochNo, b.epochSlotNo as epochSlotNo, b.slotNo as absoluteSlot "
          + " FROM Tx tx "
          + " JOIN Block b ON tx.block = b"
          + " WHERE tx.id IN :txIds")
    ```
- related table:
  - block
#### getSmartContractTxsPurpose
- query:
    ```sql
    @Query(
      "SELECT DISTINCT(r.purpose) as scriptPurposeType, tx.id as txId FROM Tx tx"
          + " JOIN Redeemer r ON r.tx = tx"
          + " WHERE tx.id IN :txIds"
          + " AND r.scriptHash = :scriptHash")
    ```
- related table:
  - redeemer
#### findAllByHashIn
</details>

### Related table:
- tx
- epoch
- block
- tx_in
- tx_out
- redeemer

## 51. TxWitnessesRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findAllByTx

</details>

## 52. UnconsumeTxInRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findTxCollateralInput
- query:
    ```sql
    @Query(
      "SELECT txOut.address AS address, txOut.index AS index, cti.txOut.hash AS txHash,"
          + "   txOut.value AS value, maTxOut.quantity as assetQuantity,"
          + "   asset.name as assetName, asset.fingerprint as assetId"
          + " FROM UnconsumeTxIn cti "
          + " INNER JOIN TxOut txOut on txOut.tx = cti.txOut and txOut.index = cti.txOutIndex "
          + " LEFT JOIN MaTxOut maTxOut ON maTxOut.txOut = txOut"
          + " LEFT JOIN MultiAsset asset ON maTxOut.ident = asset"
          + " WHERE cti.txIn = :tx")
    ```
- related table:
  - tx_out
  - ma_tx_out
  - multi_asset
</details>

### Related table:
- unconsume_tx_in
- tx_out
- ma_tx_out
- multi_asset

## 53. VotingProcedureRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findVotingProcedureByVoterHashAndGovActionType
- query:
    ```sql
    @Query(
      value =
          "select gap.txHash as govActionTxHash, gap.index as govActionIndex, gap.type as govActionType, vp.voterHash as voterHash,"
              + " vp.txHash as votingProcedureTxHash, vp.index as votingProcedureTxIndex, vp.blockTime as blockTime,vp.vote as vote"
              + " from VotingProcedure vp "
              + " join GovActionProposal gap on gap.txHash = vp.govActionTxHash and gap.index = vp.govActionIndex"
              + " where vp.voterHash = :voterHash and (:govActionType is null or gap.type = :govActionType)"
              + " and gap.blockTime >= :blockTime")
    ```
- related table:
  - gov_action_proposal
#### getVotingProcedureByTxHashAndIndexAndVoterHash
- query:
    ```sql
    @Query(
      value =
          "select vp.govActionTxHash as govActionTxHash, vp.govActionIndex as govActionIndex, vp.vote as vote, vp.txHash as votingProcedureTxHash, vp.index as votingProcedureTxIndex,"
              + " vp.blockTime as blockTime"
              + " from VotingProcedure vp where vp.govActionTxHash = :txHash and vp.govActionIndex = :index and vp.voterHash = :voterHash and vp.voterType in :voterType"
              + " order by vp.blockTime desc")
    ```
</details>

### Related table:
- voting_procedure
- gov_action_proposal

## 54. WithdrawalRepository
<details>
<summary> <h3>List queries:</h3></summary>

#### findByTx
- related table:
  - stake_address
#### getRewardWithdrawnByStakeAddress
- query:
    ```sql
    @Query(
      "SELECT SUM(w.amount) FROM Withdrawal w "
          + " INNER JOIN StakeAddress stakeAddress ON w.addr.id = stakeAddress.id"
          + " WHERE stakeAddress.view = :stakeAddress")
    ```
- related table:
  - stake_address
#### getWithdrawalByAddressAndTx
- query:
    ```sql
    @Query(
      "SELECT tx.hash as txHash,withdrawal.amount as amount, block.time as time, tx.fee as fee,"
          + " block.epochNo as epochNo, tx.id as txId"
          + " FROM Withdrawal withdrawal"
          + " INNER JOIN Tx tx ON withdrawal.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " WHERE withdrawal.addr = :stakeKey AND tx.hash = :hash")
    ```
- related table:
  - tx
  - block
#### getWithdrawalByAddress
- query:
    ```sql
    @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo, block.slotNo as slotNo,"
          + " block.blockNo as blockNo, block.epochNo as epochNo, withdrawal.amount as amount"
          + " FROM Withdrawal withdrawal"
          + " INNER JOIN Tx tx ON withdrawal.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " INNER JOIN StakeAddress stake ON withdrawal.addr = stake"
          + " WHERE stake.view = :stakeKey"
          + " ORDER BY block.blockNo DESC, tx.blockIndex DESC")
    ```
- related table:
  - tx
  - block
  - stake_address
#### getWithdrawalByAddress
- query:
    ```sql
    @Query(
      "SELECT tx.hash as txHash, block.time as time, block.epochSlotNo as epochSlotNo,"
          + " tx.fee as fee, block.blockNo as blockNo, block.epochNo as epochNo,"
          + " withdrawal.amount as amount"
          + " FROM Withdrawal withdrawal"
          + " INNER JOIN Tx tx ON withdrawal.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " WHERE withdrawal.addr = :stakeKey"
          + " AND (block.time >= :fromTime )"
          + " AND (block.time <= :toTime)"
          + " AND ( :txHash IS NULL OR tx.hash = :txHash)")
    ```
- related table:
  - tx
  - block
  - stake_address
#### TEMPLATE_QUERY
- query:
    ```sql
    @Query(
      "SELECT new org.cardanofoundation.explorer.api.model.response.stake.lifecycle.StakeRewardResponse("
          + "block.epochNo, epoch.endTime, sum(withdrawal.amount))"
          + " FROM Withdrawal withdrawal"
          + " INNER JOIN Tx tx ON withdrawal.tx = tx"
          + " INNER JOIN Block block ON tx.block = block"
          + " INNER JOIN Epoch epoch ON block.epochNo = epoch.no"
          + " WHERE withdrawal.addr = :stakeAddress"
          + " GROUP BY block.epochNo, epoch.endTime")
    ```
- related table:
  - tx
  - block
  - epoch
  - stake_address
#### sumByAddrAndTx
- query:
    ```sql
    @Query(
      "SELECT sum(w.amount) FROM Withdrawal w"
          + " WHERE w.addr = :stakeAddress AND w.tx.id < :txId")
    ```
- related table:
  - stake_address
#### existsByAddr
#### getRewardWithdrawnByAddrIn
- query:
    ```sql
    @Query(
      "SELECT w.stakeAddressId as stakeAddressId, SUM(w.amount) as amount"
          + " FROM Withdrawal w"
          + " WHERE w.stakeAddressId IN :stakeIds"
          + " GROUP BY w.stakeAddressId")
    ```
#### getRewardWithdrawnByAddressList
- query:
    ```sql
    @Query(
      "SELECT COALESCE(SUM(w.amount), 0) FROM Withdrawal w "
          + " INNER JOIN StakeAddress stakeAddress ON w.addr.id = stakeAddress.id"
          + " WHERE stakeAddress.view IN :addressList")
    ```
- related table:
  - stake_address
</details>


### Related table:
- withdrawal
- stake_address
- tx
- block
- epoch



