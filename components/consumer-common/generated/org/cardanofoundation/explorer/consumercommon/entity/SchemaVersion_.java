package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SchemaVersion.class)
public abstract class SchemaVersion_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<SchemaVersion, Long> stageOne;
	public static volatile SingularAttribute<SchemaVersion, Long> stageTwo;
	public static volatile SingularAttribute<SchemaVersion, Long> stageThree;

	public static final String STAGE_ONE = "stageOne";
	public static final String STAGE_TWO = "stageTwo";
	public static final String STAGE_THREE = "stageThree";

}

