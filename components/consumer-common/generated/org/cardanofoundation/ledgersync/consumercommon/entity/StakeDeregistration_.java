package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StakeDeregistration.class)
public abstract class StakeDeregistration_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<StakeDeregistration, Integer> certIndex;
	public static volatile SingularAttribute<StakeDeregistration, Integer> epochNo;
	public static volatile SingularAttribute<StakeDeregistration, Tx> tx;
	public static volatile SingularAttribute<StakeDeregistration, Long> stakeAddressId;
	public static volatile SingularAttribute<StakeDeregistration, Redeemer> redeemer;
	public static volatile SingularAttribute<StakeDeregistration, Long> txId;
	public static volatile SingularAttribute<StakeDeregistration, StakeAddress> addr;
	public static volatile SingularAttribute<StakeDeregistration, Long> redeemerId;

	public static final String CERT_INDEX = "certIndex";
	public static final String EPOCH_NO = "epochNo";
	public static final String TX = "tx";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String REDEEMER = "redeemer";
	public static final String TX_ID = "txId";
	public static final String ADDR = "addr";
	public static final String REDEEMER_ID = "redeemerId";

}

