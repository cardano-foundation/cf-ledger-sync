package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StakeAddress.class)
public abstract class StakeAddress_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<StakeAddress, String> view;
	public static volatile SingularAttribute<StakeAddress, BigInteger> availableReward;
	public static volatile ListAttribute<StakeAddress, Address> addresses;
	public static volatile SingularAttribute<StakeAddress, String> scriptHash;
	public static volatile SingularAttribute<StakeAddress, String> hashRaw;

	public static final String VIEW = "view";
	public static final String AVAILABLE_REWARD = "availableReward";
	public static final String ADDRESSES = "addresses";
	public static final String SCRIPT_HASH = "scriptHash";
	public static final String HASH_RAW = "hashRaw";

}

