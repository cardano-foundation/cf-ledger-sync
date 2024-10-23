package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;

@Entity
@Table(name = "off_chain_data_checkpoint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainDataCheckpoint extends BaseEntity {

    @Column(name = "block_no")
    private Long blockNo;

    @Column(name = "slot_no")
    private Long slotNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OffChainCheckpointType type;

    @Column(name = "update_time")
    private Timestamp updateTime;
}
