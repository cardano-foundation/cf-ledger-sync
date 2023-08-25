package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ExtraKeyWitness.class)
public abstract class ExtraKeyWitness_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<ExtraKeyWitness, Tx> tx;
	public static volatile SingularAttribute<ExtraKeyWitness, String> hash;

	public static final String TX = "tx";
	public static final String HASH = "hash";

}

