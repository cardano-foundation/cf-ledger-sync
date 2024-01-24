package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Treasury.class)
public abstract class Treasury_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Treasury, Integer> certIndex;
	public static volatile SingularAttribute<Treasury, BigInteger> amount;
	public static volatile SingularAttribute<Treasury, Tx> tx;
	public static volatile SingularAttribute<Treasury, StakeAddress> addr;

	public static final String CERT_INDEX = "certIndex";
	public static final String AMOUNT = "amount";
	public static final String TX = "tx";
	public static final String ADDR = "addr";

}

