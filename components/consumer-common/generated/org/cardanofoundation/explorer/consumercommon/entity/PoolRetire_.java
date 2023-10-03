package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolRetire.class)
public abstract class PoolRetire_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolRetire, Integer> certIndex;
	public static volatile SingularAttribute<PoolRetire, Long> announcedTxId;
	public static volatile SingularAttribute<PoolRetire, Integer> retiringEpoch;
	public static volatile SingularAttribute<PoolRetire, Tx> announcedTx;
	public static volatile SingularAttribute<PoolRetire, PoolHash> poolHash;
	public static volatile SingularAttribute<PoolRetire, Long> poolHashId;

	public static final String CERT_INDEX = "certIndex";
	public static final String ANNOUNCED_TX_ID = "announcedTxId";
	public static final String RETIRING_EPOCH = "retiringEpoch";
	public static final String ANNOUNCED_TX = "announcedTx";
	public static final String POOL_HASH = "poolHash";
	public static final String POOL_HASH_ID = "poolHashId";

}

