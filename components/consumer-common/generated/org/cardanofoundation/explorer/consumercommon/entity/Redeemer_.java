package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptPurposeType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Redeemer.class)
public abstract class Redeemer_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<Redeemer, Long> unitSteps;
	public static volatile SingularAttribute<Redeemer, Tx> tx;
	public static volatile SingularAttribute<Redeemer, ScriptPurposeType> purpose;
	public static volatile SingularAttribute<Redeemer, String> scriptHash;
	public static volatile SingularAttribute<Redeemer, BigInteger> fee;
	public static volatile SingularAttribute<Redeemer, RedeemerData> redeemerData;
	public static volatile SingularAttribute<Redeemer, Integer> index;
	public static volatile SingularAttribute<Redeemer, Long> unitMem;

	public static final String UNIT_STEPS = "unitSteps";
	public static final String TX = "tx";
	public static final String PURPOSE = "purpose";
	public static final String SCRIPT_HASH = "scriptHash";
	public static final String FEE = "fee";
	public static final String REDEEMER_DATA = "redeemerData";
	public static final String INDEX = "index";
	public static final String UNIT_MEM = "unitMem";

}

