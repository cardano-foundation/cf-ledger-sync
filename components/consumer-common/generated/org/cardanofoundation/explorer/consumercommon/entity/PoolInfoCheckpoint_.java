package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolInfoCheckpoint.class)
public abstract class PoolInfoCheckpoint_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolInfoCheckpoint, String> view;
	public static volatile SingularAttribute<PoolInfoCheckpoint, Integer> epochCheckpoint;

	public static final String VIEW = "view";
	public static final String EPOCH_CHECKPOINT = "epochCheckpoint";

}

