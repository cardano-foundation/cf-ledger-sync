package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AddressToken.class)
public abstract class AddressToken_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AddressToken, Address> address;
	public static volatile SingularAttribute<AddressToken, MultiAsset> multiAsset;
	public static volatile SingularAttribute<AddressToken, Tx> tx;
	public static volatile SingularAttribute<AddressToken, BigInteger> balance;
	public static volatile SingularAttribute<AddressToken, Long> txId;
	public static volatile SingularAttribute<AddressToken, Long> multiAssetId;
	public static volatile SingularAttribute<AddressToken, Long> addressId;

	public static final String ADDRESS = "address";
	public static final String MULTI_ASSET = "multiAsset";
	public static final String TX = "tx";
	public static final String BALANCE = "balance";
	public static final String TX_ID = "txId";
	public static final String MULTI_ASSET_ID = "multiAssetId";
	public static final String ADDRESS_ID = "addressId";

}

