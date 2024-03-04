package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolOwner.class)
public abstract class PoolOwner_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolOwner, PoolUpdate> poolUpdate;
	public static volatile SingularAttribute<PoolOwner, Long> stakeAddressId;
	public static volatile SingularAttribute<PoolOwner, Long> poolUpdateId;
	public static volatile SingularAttribute<PoolOwner, StakeAddress> stakeAddress;

	public static final String POOL_UPDATE = "poolUpdate";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String POOL_UPDATE_ID = "poolUpdateId";
	public static final String STAKE_ADDRESS = "stakeAddress";

}

