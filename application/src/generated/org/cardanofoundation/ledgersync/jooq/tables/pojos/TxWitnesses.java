/*
 * This file is generated by jOOQ.
 */
package org.cardanofoundation.ledgersync.jooq.tables.pojos;


import java.io.Serializable;
import java.util.Arrays;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TxWitnesses implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long txId;
    private String key;
    private String signature;
    private Integer[] indexArr;
    private Integer indexArrSize;
    private String type;

    public TxWitnesses() {}

    public TxWitnesses(TxWitnesses value) {
        this.id = value.id;
        this.txId = value.txId;
        this.key = value.key;
        this.signature = value.signature;
        this.indexArr = value.indexArr;
        this.indexArrSize = value.indexArrSize;
        this.type = value.type;
    }

    public TxWitnesses(
        Long id,
        Long txId,
        String key,
        String signature,
        Integer[] indexArr,
        Integer indexArrSize,
        String type
    ) {
        this.id = id;
        this.txId = txId;
        this.key = key;
        this.signature = signature;
        this.indexArr = indexArr;
        this.indexArrSize = indexArrSize;
        this.type = type;
    }

    /**
     * Getter for <code>tx_witnesses.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>tx_witnesses.id</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>tx_witnesses.tx_id</code>.
     */
    public Long getTxId() {
        return this.txId;
    }

    /**
     * Setter for <code>tx_witnesses.tx_id</code>.
     */
    public void setTxId(Long txId) {
        this.txId = txId;
    }

    /**
     * Getter for <code>tx_witnesses.key</code>.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>tx_witnesses.key</code>.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter for <code>tx_witnesses.signature</code>.
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Setter for <code>tx_witnesses.signature</code>.
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Getter for <code>tx_witnesses.index_arr</code>.
     */
    public Integer[] getIndexArr() {
        return this.indexArr;
    }

    /**
     * Setter for <code>tx_witnesses.index_arr</code>.
     */
    public void setIndexArr(Integer[] indexArr) {
        this.indexArr = indexArr;
    }

    /**
     * Getter for <code>tx_witnesses.index_arr_size</code>.
     */
    public Integer getIndexArrSize() {
        return this.indexArrSize;
    }

    /**
     * Setter for <code>tx_witnesses.index_arr_size</code>.
     */
    public void setIndexArrSize(Integer indexArrSize) {
        this.indexArrSize = indexArrSize;
    }

    /**
     * Getter for <code>tx_witnesses.type</code>.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>tx_witnesses.type</code>.
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TxWitnesses other = (TxWitnesses) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.txId == null) {
            if (other.txId != null)
                return false;
        }
        else if (!this.txId.equals(other.txId))
            return false;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.signature == null) {
            if (other.signature != null)
                return false;
        }
        else if (!this.signature.equals(other.signature))
            return false;
        if (this.indexArr == null) {
            if (other.indexArr != null)
                return false;
        }
        else if (!Arrays.deepEquals(this.indexArr, other.indexArr))
            return false;
        if (this.indexArrSize == null) {
            if (other.indexArrSize != null)
                return false;
        }
        else if (!this.indexArrSize.equals(other.indexArrSize))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.txId == null) ? 0 : this.txId.hashCode());
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.signature == null) ? 0 : this.signature.hashCode());
        result = prime * result + ((this.indexArr == null) ? 0 : Arrays.deepHashCode(this.indexArr));
        result = prime * result + ((this.indexArrSize == null) ? 0 : this.indexArrSize.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TxWitnesses (");

        sb.append(id);
        sb.append(", ").append(txId);
        sb.append(", ").append(key);
        sb.append(", ").append(signature);
        sb.append(", ").append(Arrays.deepToString(indexArr));
        sb.append(", ").append(indexArrSize);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }
}
