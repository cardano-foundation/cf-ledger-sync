package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AddressTokenBalance.class)
public abstract class AddressTokenBalance_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AddressTokenBalance, Address> address;
	public static volatile SingularAttribute<AddressTokenBalance, MultiAsset> multiAsset;
	public static volatile SingularAttribute<AddressTokenBalance, BigInteger> balance;
	public static volatile SingularAttribute<AddressTokenBalance, StakeAddress> stakeAddress;
	public static volatile SingularAttribute<AddressTokenBalance, Long> multiAssetId;
	public static volatile SingularAttribute<AddressTokenBalance, Long> addressId;

	public static final String ADDRESS = "address";
	public static final String MULTI_ASSET = "multiAsset";
	public static final String BALANCE = "balance";
	public static final String STAKE_ADDRESS = "stakeAddress";
	public static final String MULTI_ASSET_ID = "multiAssetId";
	public static final String ADDRESS_ID = "addressId";

}

