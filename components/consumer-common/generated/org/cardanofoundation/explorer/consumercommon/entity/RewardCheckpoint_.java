package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RewardCheckpoint.class)
public abstract class RewardCheckpoint_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<RewardCheckpoint, String> stakeAddress;
	public static volatile SingularAttribute<RewardCheckpoint, Integer> epochCheckpoint;

	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String EPOCH_CHECKPOINT = "epochCheckpoint";

}

