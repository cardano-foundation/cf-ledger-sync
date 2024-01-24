package org.cardanofoundation.ledgersync.consumercommon.entity.aggregation;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AggregateAddressToken.class)
public abstract class AggregateAddressToken_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AggregateAddressToken, BigInteger> balance;
	public static volatile SingularAttribute<AggregateAddressToken, Long> ident;
	public static volatile SingularAttribute<AggregateAddressToken, LocalDate> day;

	public static final String BALANCE = "balance";
	public static final String IDENT = "ident";
	public static final String DAY = "day";

}

