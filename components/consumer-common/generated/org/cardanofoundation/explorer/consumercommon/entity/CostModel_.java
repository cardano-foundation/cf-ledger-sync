package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CostModel.class)
public abstract class CostModel_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<CostModel, String> costs;
	public static volatile SingularAttribute<CostModel, String> hash;

	public static final String COSTS = "costs";
	public static final String HASH = "hash";

}

