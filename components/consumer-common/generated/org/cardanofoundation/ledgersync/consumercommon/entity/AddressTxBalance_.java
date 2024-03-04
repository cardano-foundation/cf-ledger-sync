package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AddressTxBalance.class)
public abstract class AddressTxBalance_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AddressTxBalance, Address> address;
	public static volatile SingularAttribute<AddressTxBalance, Tx> tx;
	public static volatile SingularAttribute<AddressTxBalance, BigInteger> balance;
	public static volatile SingularAttribute<AddressTxBalance, Long> txId;
	public static volatile SingularAttribute<AddressTxBalance, StakeAddress> stakeAddress;
	public static volatile SingularAttribute<AddressTxBalance, Timestamp> time;
	public static volatile SingularAttribute<AddressTxBalance, Long> addressId;

	public static final String ADDRESS = "address";
	public static final String TX = "tx";
	public static final String BALANCE = "balance";
	public static final String TX_ID = "txId";
	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String TIME = "time";
	public static final String ADDRESS_ID = "addressId";

}

