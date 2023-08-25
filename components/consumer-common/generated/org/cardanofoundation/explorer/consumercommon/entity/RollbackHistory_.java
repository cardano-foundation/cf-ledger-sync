package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RollbackHistory.class)
public abstract class RollbackHistory_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<RollbackHistory, String> blockHash;
	public static volatile SingularAttribute<RollbackHistory, Long> blockNo;
	public static volatile SingularAttribute<RollbackHistory, Timestamp> rollbackTime;
	public static volatile SingularAttribute<RollbackHistory, Long> slotNo;

	public static final String BLOCK_HASH = "blockHash";
	public static final String BLOCK_NO = "blockNo";
	public static final String ROLLBACK_TIME = "rollbackTime";
	public static final String SLOT_NO = "slotNo";

}

