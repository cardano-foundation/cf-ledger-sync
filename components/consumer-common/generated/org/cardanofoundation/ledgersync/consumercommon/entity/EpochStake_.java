package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EpochStake.class)
public abstract class EpochStake_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<EpochStake, BigInteger> amount;
	public static volatile SingularAttribute<EpochStake, Integer> epochNo;
	public static volatile SingularAttribute<EpochStake, Long> stakeAddressId;
	public static volatile SingularAttribute<EpochStake, PoolHash> pool;
	public static volatile SingularAttribute<EpochStake, Long> poolId;
	public static volatile SingularAttribute<EpochStake, StakeAddress> addr;

	public static final String AMOUNT = "amount";
	public static final String EPOCH_NO = "epochNo";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String POOL = "pool";
	public static final String POOL_ID = "poolId";
	public static final String ADDR = "addr";

}

