package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReferenceTxIn.class)
public abstract class ReferenceTxIn_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<ReferenceTxIn, Tx> txOut;
	public static volatile SingularAttribute<ReferenceTxIn, Short> txOutIndex;
	public static volatile SingularAttribute<ReferenceTxIn, Tx> txIn;

	public static final String TX_OUT = "txOut";
	public static final String TX_OUT_INDEX = "txOutIndex";
	public static final String TX_IN = "txIn";

}

