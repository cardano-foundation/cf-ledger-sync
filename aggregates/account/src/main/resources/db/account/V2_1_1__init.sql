drop table if exists address_tx_amount;
create table address_tx_amount
(
    address            varchar(500),
    unit               varchar(255),
    tx_hash            varchar(64),
    slot               bigint,
    quantity           numeric(38) null,
    addr_full          text,
    stake_address      varchar(255),
    block              bigint,
    block_time         bigint,
    epoch              integer,
    primary key (address, unit, tx_hash)
);

CREATE INDEX idx_address_tx_amount_slot
    ON address_tx_amount(slot);
