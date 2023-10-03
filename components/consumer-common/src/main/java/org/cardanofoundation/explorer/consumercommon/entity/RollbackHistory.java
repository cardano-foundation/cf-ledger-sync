package org.cardanofoundation.explorer.consumercommon.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "rollback_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RollbackHistory extends BaseEntity {

  @Column(name = "block_no", nullable = false)
  private Long blockNo;

  @Column(name = "block_hash", nullable = false, length = 64)
  private String blockHash;

  @Column(name = "slot_no", nullable = false)
  private Long slotNo;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "rollback_time", nullable = false)
  private Timestamp rollbackTime;

  @PrePersist
  private void prePersist() {
    rollbackTime = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
  }
}