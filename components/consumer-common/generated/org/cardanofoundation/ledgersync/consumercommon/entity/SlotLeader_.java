package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SlotLeader.class)
public abstract class SlotLeader_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile ListAttribute<SlotLeader, Block> blocks;
	public static volatile SingularAttribute<SlotLeader, String> description;
	public static volatile SingularAttribute<SlotLeader, String> hash;
	public static volatile SingularAttribute<SlotLeader, PoolHash> poolHash;
	public static volatile SingularAttribute<SlotLeader, Long> poolHashId;

	public static final String BLOCKS = "blocks";
	public static final String DESCRIPTION = "description";
	public static final String HASH = "hash";
	public static final String POOL_HASH = "poolHash";
	public static final String POOL_HASH_ID = "poolHashId";

}

