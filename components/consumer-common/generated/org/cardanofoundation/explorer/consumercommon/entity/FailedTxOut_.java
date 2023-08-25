package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FailedTxOut.class)
public abstract class FailedTxOut_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<FailedTxOut, String> address;
	public static volatile SingularAttribute<FailedTxOut, String> paymentCred;
	public static volatile SingularAttribute<FailedTxOut, Tx> tx;
	public static volatile SingularAttribute<FailedTxOut, String> multiAssetsDescr;
	public static volatile SingularAttribute<FailedTxOut, String> dataHash;
	public static volatile SingularAttribute<FailedTxOut, Script> referenceScript;
	public static volatile SingularAttribute<FailedTxOut, Short> index;
	public static volatile SingularAttribute<FailedTxOut, StakeAddress> stakeAddress;
	public static volatile SingularAttribute<FailedTxOut, byte[]> addressRaw;
	public static volatile SingularAttribute<FailedTxOut, BigInteger> value;
	public static volatile SingularAttribute<FailedTxOut, Boolean> addressHasScript;
	public static volatile SingularAttribute<FailedTxOut, Datum> inlineDatum;

	public static final String ADDRESS = "address";
	public static final String PAYMENT_CRED = "paymentCred";
	public static final String TX = "tx";
	public static final String MULTI_ASSETS_DESCR = "multiAssetsDescr";
	public static final String DATA_HASH = "dataHash";
	public static final String REFERENCE_SCRIPT = "referenceScript";
	public static final String INDEX = "index";
	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String ADDRESS_RAW = "addressRaw";
	public static final String VALUE = "value";
	public static final String ADDRESS_HAS_SCRIPT = "addressHasScript";
	public static final String INLINE_DATUM = "inlineDatum";

}

