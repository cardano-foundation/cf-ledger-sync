package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Datum.class)
public abstract class Datum_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Datum, Tx> tx;
	public static volatile SingularAttribute<Datum, byte[]> bytes;
	public static volatile SingularAttribute<Datum, String> value;
	public static volatile SingularAttribute<Datum, String> hash;

	public static final String TX = "tx";
	public static final String BYTES = "bytes";
	public static final String VALUE = "value";
	public static final String HASH = "hash";

}

