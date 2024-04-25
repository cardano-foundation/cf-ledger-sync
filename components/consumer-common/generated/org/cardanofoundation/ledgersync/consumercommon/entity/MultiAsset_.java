package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.sql.Timestamp;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MultiAsset.class)
public abstract class MultiAsset_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

    	public static volatile SingularAttribute<MultiAsset, String> unit;
	public static volatile SingularAttribute<MultiAsset, String> nameView;
	public static volatile ListAttribute<MultiAsset, AddressToken> addressToken;
	public static volatile SingularAttribute<MultiAsset, String> name;
	public static volatile SingularAttribute<MultiAsset, String> fingerprint;
	public static volatile SingularAttribute<MultiAsset, Timestamp> time;
	public static volatile SingularAttribute<MultiAsset, BigInteger> supply;
	public static volatile SingularAttribute<MultiAsset, String> policy;

    	public static final String UNIT = "unit";
	public static final String NAME_VIEW = "nameView";
	public static final String ADDRESS_TOKEN = "addressToken";
	public static final String NAME = "name";
	public static final String FINGERPRINT = "fingerprint";
	public static final String TIME = "time";
	public static final String SUPPLY = "supply";
	public static final String POLICY = "policy";

}

