package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolHash.class)
public abstract class PoolHash_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolHash, String> view;
	public static volatile SingularAttribute<PoolHash, Integer> epochNo;
	public static volatile SingularAttribute<PoolHash, BigInteger> poolSize;
	public static volatile SingularAttribute<PoolHash, String> hashRaw;
	public static volatile ListAttribute<PoolHash, Delegation> delegations;

	public static final String VIEW = "view";
	public static final String EPOCH_NO = "epochNo";
	public static final String POOL_SIZE = "poolSize";
	public static final String HASH_RAW = "hashRaw";
	public static final String DELEGATIONS = "delegations";

}

