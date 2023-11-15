update delegation d set redeemer_id = null where redeemer_id is not null;

update delegation d set
    redeemer_id = (select r.id from redeemer r where d.cert_index = r."index" and d.tx_id = r.tx_id and r.purpose = 'cert');

update stake_deregistration sd set redeemer_id = null where redeemer_id is not null;

update stake_deregistration sd set
    redeemer_id = (select r.id from redeemer r where sd.cert_index = r."index" and sd.tx_id = r.tx_id and r.purpose = 'cert');