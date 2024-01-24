package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TxIn.class)
public abstract class TxIn_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<TxIn, Tx> txOut;
	public static volatile SingularAttribute<TxIn, Short> txOutIndex;
	public static volatile SingularAttribute<TxIn, Long> txOutputId;
	public static volatile SingularAttribute<TxIn, Tx> txInput;
	public static volatile SingularAttribute<TxIn, Redeemer> redeemer;
	public static volatile SingularAttribute<TxIn, Long> txInputId;

	public static final String TX_OUT = "txOut";
	public static final String TX_OUT_INDEX = "txOutIndex";
	public static final String TX_OUTPUT_ID = "txOutputId";
	public static final String TX_INPUT = "txInput";
	public static final String REDEEMER = "redeemer";
	public static final String TX_INPUT_ID = "txInputId";

}

