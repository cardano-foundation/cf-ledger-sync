package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StakeKeyReportHistory.class)
public abstract class StakeKeyReportHistory_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<StakeKeyReportHistory, Timestamp> fromDate;
	public static volatile SingularAttribute<StakeKeyReportHistory, String> stakeKey;
	public static volatile SingularAttribute<StakeKeyReportHistory, Timestamp> toDate;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> eventRewards;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> eventDeregistration;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> eventDelegation;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> isFeesPaid;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> eventWithdrawal;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> isADATransfer;
	public static volatile SingularAttribute<StakeKeyReportHistory, Boolean> eventRegistration;
	public static volatile SingularAttribute<StakeKeyReportHistory, ReportHistory> reportHistory;

	public static final String FROM_DATE = "fromDate";
	public static final String STAKE_KEY = "stakeKey";
	public static final String TO_DATE = "toDate";
	public static final String EVENT_REWARDS = "eventRewards";
	public static final String EVENT_DEREGISTRATION = "eventDeregistration";
	public static final String EVENT_DELEGATION = "eventDelegation";
	public static final String IS_FEES_PAID = "isFeesPaid";
	public static final String EVENT_WITHDRAWAL = "eventWithdrawal";
	public static final String IS_AD_ATRANSFER = "isADATransfer";
	public static final String EVENT_REGISTRATION = "eventRegistration";
	public static final String REPORT_HISTORY = "reportHistory";

}

