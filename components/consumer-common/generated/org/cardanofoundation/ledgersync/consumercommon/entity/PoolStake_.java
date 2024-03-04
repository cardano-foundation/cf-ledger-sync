package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolStake.class)
public abstract class PoolStake_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolStake, BigInteger> amount;
	public static volatile SingularAttribute<PoolStake, PoolHash> pool;

	public static final String AMOUNT = "amount";
	public static final String POOL = "pool";

}

