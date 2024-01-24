package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tx_metadata_hash")
public class TxMetadataHash extends BaseEntity {

  @Column(name = "hash", nullable = false, length = 64)
  private String hash;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }

    TxMetadataHash that = (TxMetadataHash) o;
    if (Objects.isNull(id)) {
      return this.getHash().equals(that.getHash());
    }

    return Objects.equals(id, that.id);
  }
}
