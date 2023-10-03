package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolHistory.class)
public abstract class PoolHistory_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolHistory, Double> margin;
	public static volatile SingularAttribute<PoolHistory, Double> activeStakePct;
	public static volatile SingularAttribute<PoolHistory, BigInteger> fixedCost;
	public static volatile SingularAttribute<PoolHistory, PoolHash> pool;
	public static volatile SingularAttribute<PoolHistory, Double> epochRos;
	public static volatile SingularAttribute<PoolHistory, Integer> epochNo;
	public static volatile SingularAttribute<PoolHistory, Integer> delegatorCnt;
	public static volatile SingularAttribute<PoolHistory, Long> poolId;
	public static volatile SingularAttribute<PoolHistory, BigInteger> activeStake;
	public static volatile SingularAttribute<PoolHistory, Double> saturationPct;
	public static volatile SingularAttribute<PoolHistory, BigInteger> poolFees;
	public static volatile SingularAttribute<PoolHistory, BigInteger> delegatorRewards;
	public static volatile SingularAttribute<PoolHistory, Integer> blockCnt;

	public static final String MARGIN = "margin";
	public static final String ACTIVE_STAKE_PCT = "activeStakePct";
	public static final String FIXED_COST = "fixedCost";
	public static final String POOL = "pool";
	public static final String EPOCH_ROS = "epochRos";
	public static final String EPOCH_NO = "epochNo";
	public static final String DELEGATOR_CNT = "delegatorCnt";
	public static final String POOL_ID = "poolId";
	public static final String ACTIVE_STAKE = "activeStake";
	public static final String SATURATION_PCT = "saturationPct";
	public static final String POOL_FEES = "poolFees";
	public static final String DELEGATOR_REWARDS = "delegatorRewards";
	public static final String BLOCK_CNT = "blockCnt";

}

