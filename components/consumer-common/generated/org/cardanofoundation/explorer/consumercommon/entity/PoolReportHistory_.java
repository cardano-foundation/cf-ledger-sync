package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolReportHistory.class)
public abstract class PoolReportHistory_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolReportHistory, Boolean> eventPoolUpdate;
	public static volatile SingularAttribute<PoolReportHistory, Boolean> isPoolSize;
	public static volatile SingularAttribute<PoolReportHistory, Boolean> eventReward;
	public static volatile SingularAttribute<PoolReportHistory, Boolean> eventDeregistration;
	public static volatile SingularAttribute<PoolReportHistory, Integer> endEpoch;
	public static volatile SingularAttribute<PoolReportHistory, String> poolView;
	public static volatile SingularAttribute<PoolReportHistory, Integer> beginEpoch;
	public static volatile SingularAttribute<PoolReportHistory, Boolean> isFeesPaid;
	public static volatile SingularAttribute<PoolReportHistory, Boolean> eventRegistration;
	public static volatile SingularAttribute<PoolReportHistory, ReportHistory> reportHistory;

	public static final String EVENT_POOL_UPDATE = "eventPoolUpdate";
	public static final String IS_POOL_SIZE = "isPoolSize";
	public static final String EVENT_REWARD = "eventReward";
	public static final String EVENT_DEREGISTRATION = "eventDeregistration";
	public static final String END_EPOCH = "endEpoch";
	public static final String POOL_VIEW = "poolView";
	public static final String BEGIN_EPOCH = "beginEpoch";
	public static final String IS_FEES_PAID = "isFeesPaid";
	public static final String EVENT_REGISTRATION = "eventRegistration";
	public static final String REPORT_HISTORY = "reportHistory";

}

