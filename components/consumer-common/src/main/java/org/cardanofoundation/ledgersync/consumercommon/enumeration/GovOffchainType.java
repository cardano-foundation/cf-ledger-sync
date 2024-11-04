package org.cardanofoundation.ledgersync.consumercommon.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public enum GovOffchainType {
    VOTING(1),
    GOV_ACTION(2),
    DREP_REGISTRATION(3),
    CONSTITUTION(4),
    COMMITTEE_DEREGISTRATION(5);

    int value;
}
