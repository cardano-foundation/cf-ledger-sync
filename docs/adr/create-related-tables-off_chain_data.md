---
slug: 1
title: |
  1. Design tables related to off chain data
authors: [Sotatek-QuanLeA]
tags: [Draft]
---

## Status

Draft

## Context

In the development of Cardano's governance and voting ecosystem, one of the key requirements was to support off-chain voting to save space on the blockchain and increase scalability. This off-chain voting system complies with [CIP 1694] (https://github.com/cardano-foundation/CIPs/tree/master/CIP-1694).

The system of tables related to off_chain data is designed to store information related to off-chain votes submitted by the Cardano community, while maintaining a tight connection to on-chain data through hashing and necessary references.

## Decision

We decided to build **off_chain data** tables with the main purpose of storing off-chain data. The tables will have the following information:

1. **Vote data information**: Each record must have information about the url and hash through fields linked to the ID of the related table (e.g. the **off_chain_gov_action_data** table will have a column linked to the **gov_action_proposal** table).

2. **Data to be validated**: It is necessary to ensure that the returned information of the url is validated according to CIP standards (the ***is_valid*** field will display that information).

3. **Plan for pulling data**: Data in the tables will be fetched from the linked tables every 5 minutes

4. **Plan for retrying fetching failed data**: The fetch error data will be retried every day for 30 days (***this number may need further discussion***)

5. **Vote data fetch error information**: Each record has information about the url that failed to fetch and the hash along with the error reason and number of retries

### off_chain_voting_data table

The **off_chain_voting_data table** processes data retrieved from the **voting_procedure** table

| Field                 | Data type     | Description                                                   |
| --------------------- | ------------- | ------------------------------------------------------------- |
| `id`                  | `bigint`      | Primary key, unique identifier for each vote.                 |
| `voting_procedure_id` | `uuid`        | The voting_procedure index                                    |
| `content`             | `jsonb`       | The payload as JSON.                                          |
| `is_valid`            | `varchar(50)` | The validity status of the vote (VALID, HASH_MISMATCH, null). |
| `valid_at_slot`       | `bigint`      | The slot number when anchor url was fetched and valid         |

### off_chain_gov_action table

The **off_chain_gov_action table** processes data retrieved from the **gov_action_proposal** table

| Field                | Data type     | Description                                                   |
| -------------------- | ------------- | ------------------------------------------------------------- |
| `id`                 | `bigint`      | Primary key, unique identifier for each vote.                 |
| `gov_action_tx_hash` | `varchar(64)` | The gov_action_proposal tx_hash field                         |
| `gov_action_idx`     | `integer`     | The gov_action_proposal idx field                             |
| `content`            | `jsonb`       | The payload as JSON.                                          |
| `is_valid`           | `varchar(50)` | The validity status of the vote (VALID, HASH_MISMATCH, null). |
| `valid_at_slot`      | `bigint`      | The slot number when anchor url was fetched and valid         |

### off_chain_drep_registration table

The **off_chain_drep_registration table** processes data retrieved from the **drep_registration** table

| Field                 | Data type     | Description                                                   |
| --------------------- | ------------- | ------------------------------------------------------------- |
| `id`                  | `bigint`      | Primary key, unique identifier for each vote.                 |
| `drep_reg_tx_hash`    | `varchar(64)` | The drep_registration tx_hash field                           |
| `drep_reg_cert_index` | `integer`     | The drep_registration cert_index field                        |
| `content`             | `jsonb`       | The payload as JSON.                                          |
| `is_valid`            | `varchar(50)` | The validity status of the vote (VALID, HASH_MISMATCH, null). |
| `valid_at_slot`       | `bigint`      | The slot number when anchor url was fetched and valid         |

### off_chain_constitution table

The **off_chain_constitution table** processes data retrieved from the **constitution** table

| Field                       | Data type     | Description                                                   |
| --------------------------- | ------------- | ------------------------------------------------------------- |
| `id`                        | `bigint`      | Primary key, unique identifier for each vote.                 |
| `constitution_active_epoch` | `integer`     | The constitution active_epoch field                           |
| `content`                   | `jsonb`       | The payload as JSON.                                          |
| `is_valid`                  | `varchar(50)` | The validity status of the vote (VALID, HASH_MISMATCH, null). |
| `valid_at_slot`             | `bigint`      | The slot number when anchor url was fetched and valid         |

### off_chain_committee_deregistration table

The **off_chain_committee_deregistration table** processes data retrieved from the **committee_deregistration** table

| Field                        | Data type     | Description                                                   |
| ---------------------------- | ------------- | ------------------------------------------------------------- |
| `id`                         | `bigint`      | Primary key, unique identifier for each vote.                 |
| `committee_dereg_tx_hash`    | `varchar(64)` | The committee_deregistration tx_hash field                    |
| `committee_dereg_cert_index` | `integer`     | The committee_deregistration cert_index field                 |
| `content`                    | `jsonb`       | The payload as JSON.                                          |
| `is_valid`                   | `varchar(50)` | The validity status of the vote (VALID, HASH_MISMATCH, null). |
| `valid_at_slot`              | `bigint`      | The slot number when anchor url was fetched and valid         |

### off_chain_vote_fetch_error table

The **off_chain_vote_fetch_error table** processes data retrieved from the above tables

| Field         | Data type      | Description                                   |
| ------------- | -------------- | --------------------------------------------- |
| `anchor_url`  | `varchar(255)` | The committee_deregistration tx_hash field    |
| `anchor_hash` | `varchar(64)`  | The committee_deregistration cert_index field |
| `fetch_error` | `varchar(255)` | The text of the error.                        |
| `fetch_time`  | `timestamp`    |                                               |
| `retry_count` | `integer`      | The number of retries.                        |

## Consequences

**Benefits:**
- Offchain component fetches the content from the anchor url and verify the integrity by checking hash

- Applications do not need to verify the validity of the anchor url themselves

- Separate off-chain tables for each type for easy control and expansion for each type later (such as Drep Registration table, Gov action table, Voting table, ...)

**Disadvantages:**
- More tables will increase database storage capacity

- More tables will affect performance when used for aggregation or reporting