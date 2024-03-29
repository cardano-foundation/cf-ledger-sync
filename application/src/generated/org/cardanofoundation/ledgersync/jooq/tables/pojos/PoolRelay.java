/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PoolRelay implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String dnsName;
    private String dnsSrvName;
    private String ipv4;
    private String ipv6;
    private Integer port;
    private Long updateId;

    public PoolRelay() {}

    public PoolRelay(PoolRelay value) {
        this.id = value.id;
        this.dnsName = value.dnsName;
        this.dnsSrvName = value.dnsSrvName;
        this.ipv4 = value.ipv4;
        this.ipv6 = value.ipv6;
        this.port = value.port;
        this.updateId = value.updateId;
    }

    public PoolRelay(
        Long id,
        String dnsName,
        String dnsSrvName,
        String ipv4,
        String ipv6,
        Integer port,
        Long updateId
    ) {
        this.id = id;
        this.dnsName = dnsName;
        this.dnsSrvName = dnsSrvName;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.port = port;
        this.updateId = updateId;
    }

    /**
     * Getter for <code>pool_relay.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>pool_relay.id</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>pool_relay.dns_name</code>.
     */
    public String getDnsName() {
        return this.dnsName;
    }

    /**
     * Setter for <code>pool_relay.dns_name</code>.
     */
    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    /**
     * Getter for <code>pool_relay.dns_srv_name</code>.
     */
    public String getDnsSrvName() {
        return this.dnsSrvName;
    }

    /**
     * Setter for <code>pool_relay.dns_srv_name</code>.
     */
    public void setDnsSrvName(String dnsSrvName) {
        this.dnsSrvName = dnsSrvName;
    }

    /**
     * Getter for <code>pool_relay.ipv4</code>.
     */
    public String getIpv4() {
        return this.ipv4;
    }

    /**
     * Setter for <code>pool_relay.ipv4</code>.
     */
    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    /**
     * Getter for <code>pool_relay.ipv6</code>.
     */
    public String getIpv6() {
        return this.ipv6;
    }

    /**
     * Setter for <code>pool_relay.ipv6</code>.
     */
    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    /**
     * Getter for <code>pool_relay.port</code>.
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Setter for <code>pool_relay.port</code>.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Getter for <code>pool_relay.update_id</code>.
     */
    public Long getUpdateId() {
        return this.updateId;
    }

    /**
     * Setter for <code>pool_relay.update_id</code>.
     */
    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PoolRelay other = (PoolRelay) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.dnsName == null) {
            if (other.dnsName != null)
                return false;
        }
        else if (!this.dnsName.equals(other.dnsName))
            return false;
        if (this.dnsSrvName == null) {
            if (other.dnsSrvName != null)
                return false;
        }
        else if (!this.dnsSrvName.equals(other.dnsSrvName))
            return false;
        if (this.ipv4 == null) {
            if (other.ipv4 != null)
                return false;
        }
        else if (!this.ipv4.equals(other.ipv4))
            return false;
        if (this.ipv6 == null) {
            if (other.ipv6 != null)
                return false;
        }
        else if (!this.ipv6.equals(other.ipv6))
            return false;
        if (this.port == null) {
            if (other.port != null)
                return false;
        }
        else if (!this.port.equals(other.port))
            return false;
        if (this.updateId == null) {
            if (other.updateId != null)
                return false;
        }
        else if (!this.updateId.equals(other.updateId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.dnsName == null) ? 0 : this.dnsName.hashCode());
        result = prime * result + ((this.dnsSrvName == null) ? 0 : this.dnsSrvName.hashCode());
        result = prime * result + ((this.ipv4 == null) ? 0 : this.ipv4.hashCode());
        result = prime * result + ((this.ipv6 == null) ? 0 : this.ipv6.hashCode());
        result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
        result = prime * result + ((this.updateId == null) ? 0 : this.updateId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PoolRelay (");

        sb.append(id);
        sb.append(", ").append(dnsName);
        sb.append(", ").append(dnsSrvName);
        sb.append(", ").append(ipv4);
        sb.append(", ").append(ipv6);
        sb.append(", ").append(port);
        sb.append(", ").append(updateId);

        sb.append(")");
        return sb.toString();
    }
}
