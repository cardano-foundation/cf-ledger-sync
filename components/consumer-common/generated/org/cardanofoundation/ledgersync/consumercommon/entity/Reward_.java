package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;
import org.cardanofoundation.explorer.consumercommon.enumeration.RewardType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Reward.class)
public abstract class Reward_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Reward, BigInteger> amount;
	public static volatile SingularAttribute<Reward, Long> stakeAddressId;
	public static volatile SingularAttribute<Reward, Integer> earnedEpoch;
	public static volatile SingularAttribute<Reward, PoolHash> pool;
	public static volatile SingularAttribute<Reward, Long> poolId;
	public static volatile SingularAttribute<Reward, StakeAddress> addr;
	public static volatile SingularAttribute<Reward, RewardType> type;
	public static volatile SingularAttribute<Reward, Integer> spendableEpoch;

	public static final String AMOUNT = "amount";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String EARNED_EPOCH = "earnedEpoch";
	public static final String POOL = "pool";
	public static final String POOL_ID = "poolId";
	public static final String ADDR = "addr";
	public static final String TYPE = "type";
	public static final String SPENDABLE_EPOCH = "spendableEpoch";

}

