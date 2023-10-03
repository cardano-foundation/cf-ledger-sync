package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PotTransfer.class)
public abstract class PotTransfer_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PotTransfer, Integer> certIndex;
	public static volatile SingularAttribute<PotTransfer, Tx> tx;
	public static volatile SingularAttribute<PotTransfer, BigInteger> treasury;
	public static volatile SingularAttribute<PotTransfer, BigInteger> reserves;

	public static final String CERT_INDEX = "certIndex";
	public static final String TX = "tx";
	public static final String TREASURY = "treasury";
	public static final String RESERVES = "reserves";

}

