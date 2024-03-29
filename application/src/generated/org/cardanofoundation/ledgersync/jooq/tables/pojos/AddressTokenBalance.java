/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.pojos;


import java.io.Serializable;
import java.math.BigInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AddressTokenBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long addressId;
    private BigInteger balance;
    private Long ident;
    private Long stakeAddressId;

    public AddressTokenBalance() {}

    public AddressTokenBalance(AddressTokenBalance value) {
        this.id = value.id;
        this.addressId = value.addressId;
        this.balance = value.balance;
        this.ident = value.ident;
        this.stakeAddressId = value.stakeAddressId;
    }

    public AddressTokenBalance(
        Long id,
        Long addressId,
        BigInteger balance,
        Long ident,
        Long stakeAddressId
    ) {
        this.id = id;
        this.addressId = addressId;
        this.balance = balance;
        this.ident = ident;
        this.stakeAddressId = stakeAddressId;
    }

    /**
     * Getter for <code>address_token_balance.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>address_token_balance.id</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>address_token_balance.address_id</code>.
     */
    public Long getAddressId() {
        return this.addressId;
    }

    /**
     * Setter for <code>address_token_balance.address_id</code>.
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    /**
     * Getter for <code>address_token_balance.balance</code>.
     */
    public BigInteger getBalance() {
        return this.balance;
    }

    /**
     * Setter for <code>address_token_balance.balance</code>.
     */
    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    /**
     * Getter for <code>address_token_balance.ident</code>.
     */
    public Long getIdent() {
        return this.ident;
    }

    /**
     * Setter for <code>address_token_balance.ident</code>.
     */
    public void setIdent(Long ident) {
        this.ident = ident;
    }

    /**
     * Getter for <code>address_token_balance.stake_address_id</code>.
     */
    public Long getStakeAddressId() {
        return this.stakeAddressId;
    }

    /**
     * Setter for <code>address_token_balance.stake_address_id</code>.
     */
    public void setStakeAddressId(Long stakeAddressId) {
        this.stakeAddressId = stakeAddressId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AddressTokenBalance other = (AddressTokenBalance) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.addressId == null) {
            if (other.addressId != null)
                return false;
        }
        else if (!this.addressId.equals(other.addressId))
            return false;
        if (this.balance == null) {
            if (other.balance != null)
                return false;
        }
        else if (!this.balance.equals(other.balance))
            return false;
        if (this.ident == null) {
            if (other.ident != null)
                return false;
        }
        else if (!this.ident.equals(other.ident))
            return false;
        if (this.stakeAddressId == null) {
            if (other.stakeAddressId != null)
                return false;
        }
        else if (!this.stakeAddressId.equals(other.stakeAddressId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.addressId == null) ? 0 : this.addressId.hashCode());
        result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
        result = prime * result + ((this.ident == null) ? 0 : this.ident.hashCode());
        result = prime * result + ((this.stakeAddressId == null) ? 0 : this.stakeAddressId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AddressTokenBalance (");

        sb.append(id);
        sb.append(", ").append(addressId);
        sb.append(", ").append(balance);
        sb.append(", ").append(ident);
        sb.append(", ").append(stakeAddressId);

        sb.append(")");
        return sb.toString();
    }
}
