package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Delegation.class)
public abstract class Delegation_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Delegation, Integer> certIndex;
	public static volatile SingularAttribute<Delegation, StakeAddress> address;
	public static volatile SingularAttribute<Delegation, Tx> tx;
	public static volatile SingularAttribute<Delegation, Integer> activeEpochNo;
	public static volatile SingularAttribute<Delegation, Long> stakeAddressId;
	public static volatile SingularAttribute<Delegation, Redeemer> redeemer;
	public static volatile SingularAttribute<Delegation, Long> txId;
	public static volatile SingularAttribute<Delegation, Long> slotNo;
	public static volatile SingularAttribute<Delegation, PoolHash> poolHash;

	public static final String CERT_INDEX = "certIndex";
	public static final String ADDRESS = "address";
	public static final String TX = "tx";
	public static final String ACTIVE_EPOCH_NO = "activeEpochNo";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String REDEEMER = "redeemer";
	public static final String TX_ID = "txId";
	public static final String SLOT_NO = "slotNo";
	public static final String POOL_HASH = "poolHash";

}

