package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Meta.class)
public abstract class Meta_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Meta, String> networkName;
	public static volatile SingularAttribute<Meta, Timestamp> startTime;
	public static volatile SingularAttribute<Meta, String> version;

	public static final String NETWORK_NAME = "networkName";
	public static final String START_TIME = "startTime";
	public static final String VERSION = "version";

}

