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
### List queries:
#### findFirstByBlockNo
#### findFirstByHash
#### findAllBlock
- query
    ```sql
    @Query(value = "SELECT b FROM Block b", countQuery = "SELECT sum (e.blkCount) + 1 FROM Epoch e")
    ```
- related table:
  - epoch
### Related table:
- slot_leader
- epoch
