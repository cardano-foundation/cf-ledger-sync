package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolHistoryCheckpoint.class)
public abstract class PoolHistoryCheckpoint_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolHistoryCheckpoint, String> view;
	public static volatile SingularAttribute<PoolHistoryCheckpoint, Boolean> isSpendableReward;
	public static volatile SingularAttribute<PoolHistoryCheckpoint, Integer> epochCheckpoint;

	public static final String VIEW = "view";
	public static final String IS_SPENDABLE_REWARD = "isSpendableReward";
	public static final String EPOCH_CHECKPOINT = "epochCheckpoint";

}

