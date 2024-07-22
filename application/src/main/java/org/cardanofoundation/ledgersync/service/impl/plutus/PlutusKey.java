package org.cardanofoundation.ledgersync.service.impl.plutus;

public enum PlutusKey {
    PLUTUS_V1("PlutusV1"),
    PLUTUS_V2("PlutusV2"),
    PLUTUS_V3("PlutusV3");

    public final String value;

    PlutusKey(String value) {
        this.value = value;
    }
}
