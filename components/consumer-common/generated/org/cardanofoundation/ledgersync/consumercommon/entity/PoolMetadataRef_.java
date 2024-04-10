package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolMetadataRef.class)
public abstract class PoolMetadataRef_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolMetadataRef, Tx> registeredTx;
	public static volatile SingularAttribute<PoolMetadataRef, String> url;
	public static volatile SingularAttribute<PoolMetadataRef, String> hash;
	public static volatile SingularAttribute<PoolMetadataRef, PoolHash> poolHash;

	public static final String REGISTERED_TX = "registeredTx";
	public static final String URL = "url";
	public static final String HASH = "hash";
	public static final String POOL_HASH = "poolHash";

}

