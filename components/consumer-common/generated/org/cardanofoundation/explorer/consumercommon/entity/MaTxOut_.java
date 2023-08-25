package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MaTxOut.class)
public abstract class MaTxOut_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<MaTxOut, TxOut> txOut;
	public static volatile SingularAttribute<MaTxOut, BigInteger> quantity;
	public static volatile SingularAttribute<MaTxOut, MultiAsset> ident;
	public static volatile SingularAttribute<MaTxOut, Long> txOutId;
	public static volatile SingularAttribute<MaTxOut, Long> identId;

	public static final String TX_OUT = "txOut";
	public static final String QUANTITY = "quantity";
	public static final String IDENT = "ident";
	public static final String TX_OUT_ID = "txOutId";
	public static final String IDENT_ID = "identId";

}

