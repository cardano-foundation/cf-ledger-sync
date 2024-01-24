package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;
import org.cardanofoundation.explorer.consumercommon.enumeration.TokenType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TxOut.class)
public abstract class TxOut_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<TxOut, String> address;
	public static volatile SingularAttribute<TxOut, Tx> tx;
	public static volatile SingularAttribute<TxOut, String> dataHash;
	public static volatile SingularAttribute<TxOut, Long> txId;
	public static volatile SingularAttribute<TxOut, Short> index;
	public static volatile ListAttribute<TxOut, MaTxOut> maTxOuts;
	public static volatile SingularAttribute<TxOut, String> paymentCred;
	public static volatile SingularAttribute<TxOut, Script> referenceScript;
	public static volatile SingularAttribute<TxOut, StakeAddress> stakeAddress;
	public static volatile SingularAttribute<TxOut, byte[]> addressRaw;
	public static volatile SingularAttribute<TxOut, TokenType> tokenType;
	public static volatile SingularAttribute<TxOut, BigInteger> value;
	public static volatile SingularAttribute<TxOut, Boolean> addressHasScript;
	public static volatile SingularAttribute<TxOut, Datum> inlineDatum;

	public static final String ADDRESS = "address";
	public static final String TX = "tx";
	public static final String DATA_HASH = "dataHash";
	public static final String TX_ID = "txId";
	public static final String INDEX = "index";
	public static final String MA_TX_OUTS = "maTxOuts";
	public static final String PAYMENT_CRED = "paymentCred";
	public static final String REFERENCE_SCRIPT = "referenceScript";
	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String ADDRESS_RAW = "addressRaw";
	public static final String TOKEN_TYPE = "tokenType";
	public static final String VALUE = "value";
	public static final String ADDRESS_HAS_SCRIPT = "addressHasScript";
	public static final String INLINE_DATUM = "inlineDatum";

}

