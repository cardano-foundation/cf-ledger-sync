package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.Hibernate;

@Entity
@Table(name = "block", uniqueConstraints = {
    @UniqueConstraint(name = "unique_block", columnNames = {"hash"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Block extends BaseEntity {


  @Column(name = "hash", nullable = false, length = 64)
  private String hash;

  @Column(name = "epoch_no")
  private Integer epochNo;

  @Column(name = "slot_no")
  private Long slotNo;

  @Column(name = "epoch_slot_no")
  private Integer epochSlotNo;

  @Column(name = "block_no")
  private Long blockNo;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "previous_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Block previous;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slot_leader_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private SlotLeader slotLeader;

  @Column(name = "slot_leader_id", updatable = false, insertable = false)
  private Long slotLeaderId;

  @Column(name = "size")
  private Integer size;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "time")
  private Timestamp time;

  @Column(name = "tx_count")
  private Long txCount;

  @Column(name = "proto_major")
  private Integer protoMajor;

  @Column(name = "proto_minor")
  private Integer protoMinor;

  @Column(name = "vrf_key", length = 65535)
  private String vrfKey;

  @Column(name = "op_cert", length = 64)
  private String opCert;

  @Column(name = "op_cert_counter")
  private Long opCertCounter;


  @OneToMany(mappedBy = "block")
  private List<Tx> txList;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Block block = (Block) o;
    return id != null && Objects.equals(id, block.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
