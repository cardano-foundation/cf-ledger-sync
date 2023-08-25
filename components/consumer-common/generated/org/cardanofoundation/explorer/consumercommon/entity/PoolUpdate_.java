package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolUpdate.class)
public abstract class PoolUpdate_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolUpdate, String> vrfKeyHash;
	public static volatile SingularAttribute<PoolUpdate, Double> margin;
	public static volatile SingularAttribute<PoolUpdate, Long> rewardAddrId;
	public static volatile SingularAttribute<PoolUpdate, StakeAddress> rewardAddr;
	public static volatile SingularAttribute<PoolUpdate, BigInteger> fixedCost;
	public static volatile SingularAttribute<PoolUpdate, Integer> certIndex;
	public static volatile SingularAttribute<PoolUpdate, Integer> activeEpochNo;
	public static volatile SingularAttribute<PoolUpdate, PoolMetadataRef> meta;
	public static volatile SingularAttribute<PoolUpdate, BigInteger> pledge;
	public static volatile SingularAttribute<PoolUpdate, Tx> registeredTx;
	public static volatile SingularAttribute<PoolUpdate, Long> registeredTxId;
	public static volatile SingularAttribute<PoolUpdate, PoolHash> poolHash;
	public static volatile SingularAttribute<PoolUpdate, Long> poolHashId;

	public static final String VRF_KEY_HASH = "vrfKeyHash";
	public static final String MARGIN = "margin";
	public static final String REWARD_ADDR_ID = "rewardAddrId";
	public static final String REWARD_ADDR = "rewardAddr";
	public static final String FIXED_COST = "fixedCost";
	public static final String CERT_INDEX = "certIndex";
	public static final String ACTIVE_EPOCH_NO = "activeEpochNo";
	public static final String META = "meta";
	public static final String PLEDGE = "pledge";
	public static final String REGISTERED_TX = "registeredTx";
	public static final String REGISTERED_TX_ID = "registeredTxId";
	public static final String POOL_HASH = "poolHash";
	public static final String POOL_HASH_ID = "poolHashId";

}

