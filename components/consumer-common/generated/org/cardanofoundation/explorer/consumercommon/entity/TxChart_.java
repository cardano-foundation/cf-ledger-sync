package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TxChart.class)
public abstract class TxChart_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<TxChart, BigInteger> hour;
	public static volatile SingularAttribute<TxChart, BigInteger> month;
	public static volatile SingularAttribute<TxChart, BigInteger> year;
	public static volatile SingularAttribute<TxChart, Long> txCount;
	public static volatile SingularAttribute<TxChart, Long> txSimple;
	public static volatile SingularAttribute<TxChart, Long> txWithMetadataWithoutSc;
	public static volatile SingularAttribute<TxChart, BigInteger> day;
	public static volatile SingularAttribute<TxChart, Long> txWithSc;
	public static volatile SingularAttribute<TxChart, BigInteger> minute;

	public static final String HOUR = "hour";
	public static final String MONTH = "month";
	public static final String YEAR = "year";
	public static final String TX_COUNT = "txCount";
	public static final String TX_SIMPLE = "txSimple";
	public static final String TX_WITH_METADATA_WITHOUT_SC = "txWithMetadataWithoutSc";
	public static final String DAY = "day";
	public static final String TX_WITH_SC = "txWithSc";
	public static final String MINUTE = "minute";

}

