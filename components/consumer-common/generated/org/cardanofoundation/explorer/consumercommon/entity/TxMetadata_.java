package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TxMetadata.class)
public abstract class TxMetadata_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<TxMetadata, Tx> tx;
	public static volatile SingularAttribute<TxMetadata, byte[]> bytes;
	public static volatile SingularAttribute<TxMetadata, String> json;
	public static volatile SingularAttribute<TxMetadata, BigInteger> key;

	public static final String TX = "tx";
	public static final String BYTES = "bytes";
	public static final String JSON = "json";
	public static final String KEY = "key";

}

