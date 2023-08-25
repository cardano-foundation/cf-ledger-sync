package org.cardanofoundation.explorer.consumercommon.entity;

import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.cardanofoundation.explorer.consumercommon.enumeration.ReportStatus;
import org.cardanofoundation.explorer.consumercommon.enumeration.ReportType;
import org.hibernate.Hibernate;

@Entity
@Table(name = "report_history", uniqueConstraints = {
    @UniqueConstraint(name = "unique_storage_key",
        columnNames = {"storage_key"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ReportHistory extends BaseEntity {

  @Column(name = "storage_key", nullable = false)
  private String storageKey;

  @Column(name = "report_name", nullable = false)
  private String reportName;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "uploaded_at")
  private Timestamp uploadedAt;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportType type;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ReportHistory reportHistory = (ReportHistory) o;
    return id != null && Objects.equals(id, reportHistory.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
