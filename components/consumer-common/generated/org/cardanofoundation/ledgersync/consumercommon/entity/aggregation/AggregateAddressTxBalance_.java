package org.cardanofoundation.ledgersync.consumercommon.entity.aggregation;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AggregateAddressTxBalance.class)
public abstract class AggregateAddressTxBalance_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AggregateAddressTxBalance, BigInteger> balance;
	public static volatile SingularAttribute<AggregateAddressTxBalance, Long> stakeAddressId;
	public static volatile SingularAttribute<AggregateAddressTxBalance, LocalDate> day;
	public static volatile SingularAttribute<AggregateAddressTxBalance, Long> addressId;

	public static final String BALANCE = "balance";
	public static final String STAKE_ADDRESS_ID = "stakeAddressId";
	public static final String DAY = "day";
	public static final String ADDRESS_ID = "addressId";

}

