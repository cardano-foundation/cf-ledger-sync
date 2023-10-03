package org.cardanofoundation.explorer.consumercommon.entity;


import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "pool_report_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolReportHistory extends BaseEntity {

  @Column(name = "pool_id", nullable = false)
  private String poolView;

  @Column(name = "is_pool_size", nullable = false)
  private Boolean isPoolSize;

  @Column(name = "is_fees_paid", nullable = false)
  private Boolean isFeesPaid;

  @Column(name = "event_registration", nullable = false)
  private Boolean eventRegistration;

  @Column(name = "event_deregistration", nullable = false)
  private Boolean eventDeregistration;

  @Column(name = "event_reward", nullable = false)
  private Boolean eventReward;

  @Column(name = "event_pool_update", nullable = false)
  private Boolean eventPoolUpdate;

  @Column(name = "begin_epoch", nullable = false)
  private Integer beginEpoch;

  @Column(name = "end_epoch", nullable = false)
  private Integer endEpoch;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "report_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private ReportHistory reportHistory;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolReportHistory poolReportHistory = (PoolReportHistory) o;
    return id != null && Objects.equals(id, poolReportHistory.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
