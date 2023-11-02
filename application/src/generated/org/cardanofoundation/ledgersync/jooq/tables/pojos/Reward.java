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
public class Reward implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private BigInteger amount;
    private Long earnedEpoch;
    private Long spendableEpoch;
    private String type;
    private Long addrId;
    private Long poolId;

    public Reward() {}

    public Reward(Reward value) {
        this.id = value.id;
        this.amount = value.amount;
        this.earnedEpoch = value.earnedEpoch;
        this.spendableEpoch = value.spendableEpoch;
        this.type = value.type;
        this.addrId = value.addrId;
        this.poolId = value.poolId;
    }

    public Reward(
        Long id,
        BigInteger amount,
        Long earnedEpoch,
        Long spendableEpoch,
        String type,
        Long addrId,
        Long poolId
    ) {
        this.id = id;
        this.amount = amount;
        this.earnedEpoch = earnedEpoch;
        this.spendableEpoch = spendableEpoch;
        this.type = type;
        this.addrId = addrId;
        this.poolId = poolId;
    }

    /**
     * Getter for <code>reward.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>reward.id</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>reward.amount</code>.
     */
    public BigInteger getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>reward.amount</code>.
     */
    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    /**
     * Getter for <code>reward.earned_epoch</code>.
     */
    public Long getEarnedEpoch() {
        return this.earnedEpoch;
    }

    /**
     * Setter for <code>reward.earned_epoch</code>.
     */
    public void setEarnedEpoch(Long earnedEpoch) {
        this.earnedEpoch = earnedEpoch;
    }

    /**
     * Getter for <code>reward.spendable_epoch</code>.
     */
    public Long getSpendableEpoch() {
        return this.spendableEpoch;
    }

    /**
     * Setter for <code>reward.spendable_epoch</code>.
     */
    public void setSpendableEpoch(Long spendableEpoch) {
        this.spendableEpoch = spendableEpoch;
    }

    /**
     * Getter for <code>reward.type</code>.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>reward.type</code>.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for <code>reward.addr_id</code>.
     */
    public Long getAddrId() {
        return this.addrId;
    }

    /**
     * Setter for <code>reward.addr_id</code>.
     */
    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    /**
     * Getter for <code>reward.pool_id</code>.
     */
    public Long getPoolId() {
        return this.poolId;
    }

    /**
     * Setter for <code>reward.pool_id</code>.
     */
    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Reward other = (Reward) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!this.amount.equals(other.amount))
            return false;
        if (this.earnedEpoch == null) {
            if (other.earnedEpoch != null)
                return false;
        }
        else if (!this.earnedEpoch.equals(other.earnedEpoch))
            return false;
        if (this.spendableEpoch == null) {
            if (other.spendableEpoch != null)
                return false;
        }
        else if (!this.spendableEpoch.equals(other.spendableEpoch))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.addrId == null) {
            if (other.addrId != null)
                return false;
        }
        else if (!this.addrId.equals(other.addrId))
            return false;
        if (this.poolId == null) {
            if (other.poolId != null)
                return false;
        }
        else if (!this.poolId.equals(other.poolId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.earnedEpoch == null) ? 0 : this.earnedEpoch.hashCode());
        result = prime * result + ((this.spendableEpoch == null) ? 0 : this.spendableEpoch.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.addrId == null) ? 0 : this.addrId.hashCode());
        result = prime * result + ((this.poolId == null) ? 0 : this.poolId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Reward (");

        sb.append(id);
        sb.append(", ").append(amount);
        sb.append(", ").append(earnedEpoch);
        sb.append(", ").append(spendableEpoch);
        sb.append(", ").append(type);
        sb.append(", ").append(addrId);
        sb.append(", ").append(poolId);

        sb.append(")");
        return sb.toString();
    }
}