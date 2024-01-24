package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AdaPots.class)
public abstract class AdaPots_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AdaPots, BigInteger> utxo;
	public static volatile SingularAttribute<AdaPots, Long> blockId;
	public static volatile SingularAttribute<AdaPots, BigInteger> fees;
	public static volatile SingularAttribute<AdaPots, Integer> epochNo;
	public static volatile SingularAttribute<AdaPots, BigInteger> treasury;
	public static volatile SingularAttribute<AdaPots, BigInteger> reserves;
	public static volatile SingularAttribute<AdaPots, Block> block;
	public static volatile SingularAttribute<AdaPots, Long> slotNo;
	public static volatile SingularAttribute<AdaPots, BigInteger> rewards;
	public static volatile SingularAttribute<AdaPots, BigInteger> deposits;

	public static final String UTXO = "utxo";
	public static final String BLOCK_ID = "blockId";
	public static final String FEES = "fees";
	public static final String EPOCH_NO = "epochNo";
	public static final String TREASURY = "treasury";
	public static final String RESERVES = "reserves";
	public static final String BLOCK = "block";
	public static final String SLOT_NO = "slotNo";
	public static final String REWARDS = "rewards";
	public static final String DEPOSITS = "deposits";

}

