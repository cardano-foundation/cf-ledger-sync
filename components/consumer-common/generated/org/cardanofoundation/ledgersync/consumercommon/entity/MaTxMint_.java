package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MaTxMint.class)
public abstract class MaTxMint_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<MaTxMint, BigInteger> quantity;
	public static volatile SingularAttribute<MaTxMint, Tx> tx;
	public static volatile SingularAttribute<MaTxMint, MultiAsset> ident;
	public static volatile SingularAttribute<MaTxMint, Long> identId;

	public static final String QUANTITY = "quantity";
	public static final String TX = "tx";
	public static final String IDENT = "ident";
	public static final String IDENT_ID = "identId";

}

