package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.sql.Timestamp;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainFetchErrorCpId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.hibernate.Hibernate;

@Entity
@Table(name = "off_chain_fetch_error", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_fetch_error",
        columnNames = {"anchor_url", "anchor_hash", "type_vote"})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type_vote", nullable = false)
    private TypeVote typeVote;

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
        name = "type_vote",
        column = @Column(
            name = "type_vote",
            insertable = false,
            updatable = false
        )
    )})
    private OffChainFetchErrorCpId cpId;

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
