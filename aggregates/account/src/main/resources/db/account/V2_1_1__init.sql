drop table if exists address_tx_amount;
create table address_tx_amount
(
    address            varchar(500),
    unit               varchar(255),
    tx_hash            varchar(64),
    slot               bigint,
    quantity           numeric(38)  null,
    addr_full          text,
    policy             varchar(56),
    asset_name         varchar(255),
    payment_credential varchar(56),
    stake_address      varchar(255),
    block_hash         varchar(64),
    block              bigint,
    block_time         bigint,
    epoch              integer,
    update_datetime    timestamp,
    primary key (address, unit, tx_hash)
);
