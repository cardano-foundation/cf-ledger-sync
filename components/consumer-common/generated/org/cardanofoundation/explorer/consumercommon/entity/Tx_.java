package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tx.class)
public abstract class Tx_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile ListAttribute<Tx, AddressToken> addressTokens;
	public static volatile SingularAttribute<Tx, BigInteger> fee;
	public static volatile SingularAttribute<Tx, TxMetadataHash> txMetadataHash;
	public static volatile SingularAttribute<Tx, BigInteger> outSum;
	public static volatile SingularAttribute<Tx, BigInteger> invalidBefore;
	public static volatile SingularAttribute<Tx, Long> blockId;
	public static volatile SingularAttribute<Tx, Long> blockIndex;
	public static volatile SingularAttribute<Tx, Integer> size;
	public static volatile SingularAttribute<Tx, Boolean> validContract;
	public static volatile ListAttribute<Tx, AddressTxBalance> addressTxBalances;
	public static volatile SingularAttribute<Tx, Long> deposit;
	public static volatile SingularAttribute<Tx, Block> block;
	public static volatile SingularAttribute<Tx, BigInteger> invalidHereafter;
	public static volatile SingularAttribute<Tx, Integer> scriptSize;
	public static volatile SingularAttribute<Tx, String> hash;

	public static final String ADDRESS_TOKENS = "addressTokens";
	public static final String FEE = "fee";
	public static final String TX_METADATA_HASH = "txMetadataHash";
	public static final String OUT_SUM = "outSum";
	public static final String INVALID_BEFORE = "invalidBefore";
	public static final String BLOCK_ID = "blockId";
	public static final String BLOCK_INDEX = "blockIndex";
	public static final String SIZE = "size";
	public static final String VALID_CONTRACT = "validContract";
	public static final String ADDRESS_TX_BALANCES = "addressTxBalances";
	public static final String DEPOSIT = "deposit";
	public static final String BLOCK = "block";
	public static final String INVALID_HEREAFTER = "invalidHereafter";
	public static final String SCRIPT_SIZE = "scriptSize";
	public static final String HASH = "hash";

}

