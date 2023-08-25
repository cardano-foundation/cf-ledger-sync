package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AssetMetadata.class)
public abstract class AssetMetadata_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<AssetMetadata, String> ticker;
	public static volatile SingularAttribute<AssetMetadata, String> subject;
	public static volatile SingularAttribute<AssetMetadata, Integer> decimals;
	public static volatile SingularAttribute<AssetMetadata, String> name;
	public static volatile SingularAttribute<AssetMetadata, String> description;
	public static volatile SingularAttribute<AssetMetadata, String> logo;
	public static volatile SingularAttribute<AssetMetadata, String> logoHash;
	public static volatile SingularAttribute<AssetMetadata, String> url;
	public static volatile SingularAttribute<AssetMetadata, String> policy;

	public static final String TICKER = "ticker";
	public static final String SUBJECT = "subject";
	public static final String DECIMALS = "decimals";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String LOGO = "logo";
	public static final String LOGO_HASH = "logoHash";
	public static final String URL = "url";
	public static final String POLICY = "policy";

}

