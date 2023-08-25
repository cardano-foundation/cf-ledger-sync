package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Address, Boolean> verifiedContract;
	public static volatile SingularAttribute<Address, String> address;
	public static volatile SingularAttribute<Address, BigInteger> balance;
	public static volatile SingularAttribute<Address, Long> stakeAddressId;
	public static volatile SingularAttribute<Address, Long> txCount;
	public static volatile SingularAttribute<Address, StakeAddress> stakeAddress;
	public static volatile SingularAttribute<Address, Boolean> addressHasScript;

	public static final String VERIFIED_CONTRACT = "verifiedContract";
	public static final String ADDRESS = "address";
	public static final String BALANCE = "balance";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String TX_COUNT = "txCount";
	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String ADDRESS_HAS_SCRIPT = "addressHasScript";

}

