package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolInfo.class)
public abstract class PoolInfo_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolInfo, BigInteger> liveStake;
	public static volatile SingularAttribute<PoolInfo, PoolHash> pool;
	public static volatile SingularAttribute<PoolInfo, Long> poolId;
	public static volatile SingularAttribute<PoolInfo, BigInteger> activeStake;
	public static volatile SingularAttribute<PoolInfo, Integer> fetchedAtEpoch;
	public static volatile SingularAttribute<PoolInfo, Double> liveSaturation;

	public static final String LIVE_STAKE = "liveStake";
	public static final String POOL = "pool";
	public static final String POOL_ID = "poolId";
	public static final String ACTIVE_STAKE = "activeStake";
	public static final String FETCHED_AT_EPOCH = "fetchedAtEpoch";
	public static final String LIVE_SATURATION = "liveSaturation";

}

