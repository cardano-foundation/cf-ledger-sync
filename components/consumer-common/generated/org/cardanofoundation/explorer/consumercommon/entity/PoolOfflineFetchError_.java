package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolOfflineFetchError.class)
public abstract class PoolOfflineFetchError_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolOfflineFetchError, String> fetchError;
	public static volatile SingularAttribute<PoolOfflineFetchError, Integer> retryCount;
	public static volatile SingularAttribute<PoolOfflineFetchError, Timestamp> fetchTime;
	public static volatile SingularAttribute<PoolOfflineFetchError, PoolHash> poolHash;
	public static volatile SingularAttribute<PoolOfflineFetchError, PoolMetadataRef> poolMetadataRef;

	public static final String FETCH_ERROR = "fetchError";
	public static final String RETRY_COUNT = "retryCount";
	public static final String FETCH_TIME = "fetchTime";
	public static final String POOL_HASH = "poolHash";
	public static final String POOL_METADATA_REF = "poolMetadataRef";

}

