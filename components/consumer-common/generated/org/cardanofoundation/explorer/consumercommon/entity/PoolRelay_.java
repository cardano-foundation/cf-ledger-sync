package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PoolRelay.class)
public abstract class PoolRelay_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<PoolRelay, PoolUpdate> poolUpdate;
	public static volatile SingularAttribute<PoolRelay, String> ipv4;
	public static volatile SingularAttribute<PoolRelay, Integer> port;
	public static volatile SingularAttribute<PoolRelay, String> ipv6;
	public static volatile SingularAttribute<PoolRelay, String> dnsName;
	public static volatile SingularAttribute<PoolRelay, String> dnsSrvName;

	public static final String POOL_UPDATE = "poolUpdate";
	public static final String IPV4 = "ipv4";
	public static final String PORT = "port";
	public static final String IPV6 = "ipv6";
	public static final String DNS_NAME = "dnsName";
	public static final String DNS_SRV_NAME = "dnsSrvName";

}

