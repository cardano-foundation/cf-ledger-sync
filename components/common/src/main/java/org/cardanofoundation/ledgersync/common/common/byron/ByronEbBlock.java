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
public class ByronEbBlock extends ByronBlock{

    public static final String TYPE = "ByronEbBlock";

    private ByronEbHead header;
    private ByronEbBody body;

    @Override
    public String getType() {
        return TYPE;
    }


    @Override
    public String getBlockHash() {
        return header.getBlockHash();
    }

    @Override
    public long getSlot() {
        return header.getConsensusData().getEpochId() * 21600;
    }

    @Override
    public long getBlockNumber() {
        return 0;
    }

    @Override
    public String getPreviousHash() {
        return header.getPrevBlock();
    }

    @Override
    public long getEpoch() {
        return header.getConsensusData().getEpochId();
    }

    public ByronEbHead getHeader() {
        return header;
    }
}
