---
slug: 1
title: |
  1. Off_chain_vote_data Table Design
authors: [Sotatek-QuanLeA]
tags: [Draft]
---

## Status

Draft

## Context

In the development of Cardano's governance and voting ecosystem, one of the key requirements is to support off-chain voting to save space on the blockchain and increase scalability. This off-chain voting system needs to be transparent, secure, and efficiently queryable.

The **off_chain_vote_data** table is designed to store information related to off-chain votes, submitted by the Cardano community, while maintaining a strong connection to on-chain data through hashes and necessary references.

## Decision

We decided to build the **off_chain_vote_data** table with the main purpose of storing off-chain votes. The table needs to meet the following information:

1. **Vote data information**: Each vote must have information about the url and hash to ensure data integrity.

2. **Integration with on-chain systems**: It is necessary to ensure that off-chain voting data can be compared and linked to the on-chain data system.

3. **Data needs to be validated**: It is necessary to ensure that the returned information of the url is validated according to CIP standards

### Off_chain_vote_data table

The **off_chain_vote_data table** stores information about off-chain votes, including the following main fields:

| Field         | Data type      | Description                                                         |
| ------------- | -------------- | ------------------------------------------------------------------- |
| `id`          | `bigint`       | Primary key, unique identifier for each vote.                       |
| `anchor_url`  | `varchar(255)` | A URL to a JSON payload of metadata.                                |
| `anchor_hash` | `varchar(64)`  | A hash of the contents of the metadata URL.                         |
| `content`     | `jsonb`        | The payload as JSON.                                                |
| `block_id`    | `bigint`       | The Block table index of the tx that includes this anchor.          |
| `is_valid`    | `boolean`      | The validity status of the vote (validated via hash and signature). |
| `fetch_error` | `text`         | The text of the error. (null if is_valid is true)                   |

## Consequences

**Benefits:**
- Reduce the load on the main blockchain, store large data off-chain.

- Enhance security and authentication capabilities for voting data.

- Enhance scalability of the voting system.

**Disadvantages:**
- The system needs to rely on off-chain mechanisms, requiring a more complex authentication process.

- Integration requires tight synchronization between on-chain and off-chain data.