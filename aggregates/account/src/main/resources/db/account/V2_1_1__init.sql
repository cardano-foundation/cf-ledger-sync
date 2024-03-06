drop table if exists address_tx_amount;
create table address_tx_amount
(
    address            varchar(500),
    unit               varchar(255),
    tx_hash            varchar(64),
    slot               bigint,
    quantity           numeric(38) null,
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

-- address_balance_view
drop view if exists address_balance_view;
create view address_balance_view as
select ab.*
from address_balance ab
         inner join (select address, unit, max(slot) as max_slot
                     from address_balance ab2
                     group by address, unit) max_ab
                    on ab.address = max_ab.address and ab.unit = max_ab.unit and ab.slot = max_ab.max_slot;

-- stake_address_balance_view
drop view if exists stake_address_balance_view;
create view stake_address_balance_view AS
select sb.*
from stake_address_balance sb
         inner join (select address, MAX(slot) as max_slot
                     from stake_address_balance sb2
                     group by address) max_sb on sb.address = max_sb.address and sb.slot = max_sb.max_slot;
