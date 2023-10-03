package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Reserve.class)
public abstract class Reserve_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Reserve, Integer> certIndex;
	public static volatile SingularAttribute<Reserve, BigInteger> amount;
	public static volatile SingularAttribute<Reserve, Tx> tx;
	public static volatile SingularAttribute<Reserve, StakeAddress> addr;

	public static final String CERT_INDEX = "certIndex";
	public static final String AMOUNT = "amount";
	public static final String TX = "tx";
	public static final String ADDR = "addr";

}

