package org.cardanofoundation.ledgersync.consumercommon.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public enum TypeVote {
    VOTING,
    GOV_ACTION,
    DREP_REGISTRATION,
    CONSTITUTION,
    COMMITTEE_DEREGISTRATION
}
