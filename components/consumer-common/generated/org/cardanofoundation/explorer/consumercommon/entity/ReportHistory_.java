package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;
import org.cardanofoundation.explorer.consumercommon.enumeration.ReportStatus;
import org.cardanofoundation.explorer.consumercommon.enumeration.ReportType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReportHistory.class)
public abstract class ReportHistory_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<ReportHistory, Timestamp> createdAt;
	public static volatile SingularAttribute<ReportHistory, String> reportName;
	public static volatile SingularAttribute<ReportHistory, Timestamp> uploadedAt;
	public static volatile SingularAttribute<ReportHistory, ReportType> type;
	public static volatile SingularAttribute<ReportHistory, String> storageKey;
	public static volatile SingularAttribute<ReportHistory, String> username;
	public static volatile SingularAttribute<ReportHistory, ReportStatus> status;

	public static final String CREATED_AT = "createdAt";
	public static final String REPORT_NAME = "reportName";
	public static final String UPLOADED_AT = "uploadedAt";
	public static final String TYPE = "type";
	public static final String STORAGE_KEY = "storageKey";
	public static final String USERNAME = "username";
	public static final String STATUS = "status";

}

