package org.cardanofoundation.explorer.consumercommon.entity;

import java.sql.Timestamp;
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

@Entity
@Table(name = "stake_key_report_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StakeKeyReportHistory extends BaseEntity {

  @Column(name = "stake_key", nullable = false)
  private String stakeKey;

  @Column(name = "from_date", nullable = false)
  private Timestamp fromDate;

  @Column(name = "to_date", nullable = false)
  private Timestamp toDate;

  @Column(name = "is_ada_transfer", nullable = false)
  private Boolean isADATransfer;

  @Column(name = "is_fees_paid", nullable = false)
  private Boolean isFeesPaid;

  @Column(name = "event_registration", nullable = false)
  private Boolean eventRegistration;

  @Column(name = "event_delegation", nullable = false)
  private Boolean eventDelegation;

  @Column(name = "event_rewards", nullable = false)
  private Boolean eventRewards;

  @Column(name = "event_withdrawal", nullable = false)
  private Boolean eventWithdrawal;

  @Column(name = "event_deregistration", nullable = false)
  private Boolean eventDeregistration;

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
    StakeKeyReportHistory stakeKeyReportHistory = (StakeKeyReportHistory) o;
    return id != null && Objects.equals(id, stakeKeyReportHistory.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
