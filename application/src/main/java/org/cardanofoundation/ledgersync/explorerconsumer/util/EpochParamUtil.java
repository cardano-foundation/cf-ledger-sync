package org.cardanofoundation.ledgersync.explorerconsumer.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.ParamProposal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EpochParamUtil {
    private EpochParamUtil() {
    }

    /**
     * Gets the parameter proposal to update epoch param based on the previous parameter proposals,
     * delegation keys, and an updateQuorum value.
     *
     * @param prevParamProposals The collection of parameter proposals of previous epoch.
     * @param delegationKeys     The collection of hash of delegation keys in shelly genesis file.
     * @param updateQuorum       The updateQuorum value in shelly genesis file.
     * @return The parameter proposal to update, or null if none meets the criteria.
     */
    public static ParamProposal getParamProposalToUpdate(Collection<ParamProposal> prevParamProposals,
                                                         Collection<String> delegationKeys, int updateQuorum) {
        Set<String> paramProposalKeys = prevParamProposals.stream().map(ParamProposal::getKey)
                .collect(Collectors.toSet());
        if (paramProposalKeys.size() < updateQuorum || !delegationKeys.containsAll(paramProposalKeys)) {
            return null;
        }

        Map<String, ParamProposal> keyParamProposalMap = new HashMap<>();
        for (ParamProposal paramProposal : prevParamProposals) {
            keyParamProposalMap.put(paramProposal.getKey(), paramProposal);
        }

        Map<ParamProposalUpdate, Integer> paramProposalUpdateMap = new HashMap<>();

        for (ParamProposal proposal : keyParamProposalMap.values()) {
            ParamProposalUpdate update = new ParamProposalUpdate(proposal);
            paramProposalUpdateMap.merge(update, 1, Integer::sum);
            if (paramProposalUpdateMap.get(update) >= updateQuorum) {
                return proposal;
            }
        }

        return null;
    }

    private record ParamProposalUpdate(ParamProposal proposal) {

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            ParamProposalUpdate other = (ParamProposalUpdate) obj;
            return EqualsBuilder.reflectionEquals(proposal, other.proposal, "id", "key");
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(proposal, "id", "key");
        }
    }
}
