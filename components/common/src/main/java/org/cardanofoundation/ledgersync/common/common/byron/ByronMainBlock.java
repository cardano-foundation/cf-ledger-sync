package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronMainBlock extends ByronBlock {

    public static final String TYPE = "ByronMainBlock";

    private ByronBlockHead header;
    private ByronMainBody body;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getBlockHash() {
        return getHeader().getBlockHash();
    }

    @Override
    public long getSlot() {
        return getHeader().getConsensusData().getSlotId().getSlotId();
    }

    @Override
    public long getBlockNumber() {
        return getHeader().getConsensusData().getDifficulty().longValue();
    }

    @Override
    public String getPreviousHash() {
        return getHeader().getPrevBlock();
    }

    @Override
    public long getEpoch() {
        return header.getConsensusData().getSlotId().getValue();
    }



    public ByronBlockHead getHeader() {
        return header;
    }
}
