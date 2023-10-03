package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Block.class)
public abstract class Block_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Block, Integer> epochSlotNo;
	public static volatile SingularAttribute<Block, Block> previous;
	public static volatile SingularAttribute<Block, String> vrfKey;
	public static volatile SingularAttribute<Block, Integer> protoMinor;
	public static volatile SingularAttribute<Block, Long> slotLeaderId;
	public static volatile SingularAttribute<Block, String> opCert;
	public static volatile ListAttribute<Block, Tx> txList;
	public static volatile SingularAttribute<Block, Integer> epochNo;
	public static volatile SingularAttribute<Block, Integer> size;
	public static volatile SingularAttribute<Block, Integer> protoMajor;
	public static volatile SingularAttribute<Block, Long> blockNo;
	public static volatile SingularAttribute<Block, Long> txCount;
	public static volatile SingularAttribute<Block, SlotLeader> slotLeader;
	public static volatile SingularAttribute<Block, Long> opCertCounter;
	public static volatile SingularAttribute<Block, Timestamp> time;
	public static volatile SingularAttribute<Block, Long> slotNo;
	public static volatile SingularAttribute<Block, String> hash;

	public static final String EPOCH_SLOT_NO = "epochSlotNo";
	public static final String PREVIOUS = "previous";
	public static final String VRF_KEY = "vrfKey";
	public static final String PROTO_MINOR = "protoMinor";
	public static final String SLOT_LEADER_ID = "slotLeaderId";
	public static final String OP_CERT = "opCert";
	public static final String TX_LIST = "txList";
	public static final String EPOCH_NO = "epochNo";
	public static final String SIZE = "size";
	public static final String PROTO_MAJOR = "protoMajor";
	public static final String BLOCK_NO = "blockNo";
	public static final String TX_COUNT = "txCount";
	public static final String SLOT_LEADER = "slotLeader";
	public static final String OP_CERT_COUNTER = "opCertCounter";
	public static final String TIME = "time";
	public static final String SLOT_NO = "slotNo";
	public static final String HASH = "hash";

}

