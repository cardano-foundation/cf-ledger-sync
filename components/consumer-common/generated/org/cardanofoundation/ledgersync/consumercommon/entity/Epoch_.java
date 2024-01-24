package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;
import org.cardanofoundation.explorer.consumercommon.enumeration.EraType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Epoch.class)
public abstract class Epoch_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Epoch, Integer> blkCount;
	public static volatile SingularAttribute<Epoch, Integer> no;
	public static volatile SingularAttribute<Epoch, BigInteger> fees;
	public static volatile SingularAttribute<Epoch, EraType> era;
	public static volatile SingularAttribute<Epoch, Integer> txCount;
	public static volatile SingularAttribute<Epoch, Timestamp> startTime;
	public static volatile SingularAttribute<Epoch, Timestamp> endTime;
	public static volatile SingularAttribute<Epoch, BigInteger> outSum;
	public static volatile SingularAttribute<Epoch, Integer> maxSlot;
	public static volatile SingularAttribute<Epoch, BigInteger> rewardsDistributed;

	public static final String BLK_COUNT = "blkCount";
	public static final String NO = "no";
	public static final String FEES = "fees";
	public static final String ERA = "era";
	public static final String TX_COUNT = "txCount";
	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";
	public static final String OUT_SUM = "outSum";
	public static final String MAX_SLOT = "maxSlot";
	public static final String REWARDS_DISTRIBUTED = "rewardsDistributed";

}

