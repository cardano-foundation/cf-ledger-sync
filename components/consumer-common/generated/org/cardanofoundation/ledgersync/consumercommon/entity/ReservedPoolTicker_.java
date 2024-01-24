package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReservedPoolTicker.class)
public abstract class ReservedPoolTicker_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<ReservedPoolTicker, String> name;
	public static volatile SingularAttribute<ReservedPoolTicker, String> poolHash;

	public static final String NAME = "name";
	public static final String POOL_HASH = "poolHash";

}

