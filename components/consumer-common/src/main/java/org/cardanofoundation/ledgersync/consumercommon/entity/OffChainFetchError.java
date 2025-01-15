package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.sql.Timestamp;
import java.util.Objects;

import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorId;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.hibernate.Hibernate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "off_chain_fetch_error", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_fetch_error",
        columnNames = {"anchor_url", "anchor_hash", "type"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainFetchError extends BaseEntity {

    @Column(name = "anchor_url")
    private String anchorUrl;

    @Column(name = "anchor_hash")
    private String anchorHash;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Embedded
    @AttributeOverrides({@AttributeOverride(
        name = "anchor_url",
        column = @Column(
            name = "anchor_url",
            insertable = false,
            updatable = false
        )
    ), @AttributeOverride(
        name = "anchor_hash",
        column = @Column(
            name = "anchor_hash",
            insertable = false,
            updatable = false
        )
    ), @AttributeOverride(
        name = "type",
        column = @Column(
            name = "type",
            insertable = false,
            updatable = false
        )
    )})
    private OffChainFetchErrorId offChainFetchErrorId;

    @Column(name = "fetch_error", nullable = false, length = 65535)
    private String fetchError;

    @Column(name = "fetch_time", nullable = false)
    private Timestamp fetchTime;

    @Word31Type
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OffChainFetchError that = (OffChainFetchError) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
