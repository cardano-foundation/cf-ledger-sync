package org.cardanofoundation.ledgersync.util;

import org.cardanofoundation.ledgersync.consumercommon.entity.ParamProposal;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpochParamUtilTest {

    @Test
    void testGetParamProposalToUpdate_WhenParamProposalKeysSmallerThanUpdateQuorum() {
        int updateQuorum = 5;
        List<String> delegationKeys = List.of(
                "ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c",
                "b9547b8a57656539a8d9bc42c008e38d9c8bd9c8adbb1e73ad529497",
                "60baee25cbc90047e83fd01e1e57dc0b06d3d0cb150d0ab40bbfead1",
                "f7b341c14cd58fca4195a9b278cce1ef402dc0e06deb77e543cd1757",
                "162f94554ac8c225383a2248c245659eda870eaa82d0ef25fc7dcd82"
        );

        final Collection<ParamProposal> prevParamProposals = List.of(ParamProposal.builder()
                .key("ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c")
                .build());

        final ParamProposal result = EpochParamUtil.getParamProposalToUpdate(prevParamProposals, delegationKeys, updateQuorum);

        assertNull(result);
    }

    @Test
    void testGetParamProposalToUpdate_WhenExistParamProposalKeyNotInGenesisDelegationKeys() {
        int updateQuorum = 2;
        List<String> delegationKeys = List.of(
                "ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c",
                "b9547b8a57656539a8d9bc42c008e38d9c8bd9c8adbb1e73ad529497",
                "60baee25cbc90047e83fd01e1e57dc0b06d3d0cb150d0ab40bbfead1"
        );

        final Collection<ParamProposal> prevParamProposals = List.of(ParamProposal.builder()
                        .key("162f94554ac8c225383a2248c245659eda870eaa82d0ef25fc7dcd82")
                        .build(),
                ParamProposal.builder()
                        .key("60baee25cbc90047e83fd01e1e57dc0b06d3d0cb150d0ab40bbfead1")
                        .build(),
                ParamProposal.builder()
                        .key("b9547b8a57656539a8d9bc42c008e38d9c8bd9c8adbb1e73ad529497")
                        .build()
        );

        final ParamProposal result = EpochParamUtil.getParamProposalToUpdate(prevParamProposals, delegationKeys, updateQuorum);

        assertNull(result);
    }

    @Test
    void testGetParamProposalToUpdate_WhenProtocolUpdateCanBeUsedToUpdateEpochPram() {
        int updateQuorum = 2;
        List<String> delegationKeys = List.of(
                "ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c",
                "b9547b8a57656539a8d9bc42c008e38d9c8bd9c8adbb1e73ad529497",
                "60baee25cbc90047e83fd01e1e57dc0b06d3d0cb150d0ab40bbfead1"
        );

        final Collection<ParamProposal> prevParamProposals = List.of(
                ParamProposal.builder()
                        .key("ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c")
                        .epochNo(200)
                        .decentralisation(0.0)
                        .coinsPerUtxoSize(BigInteger.TWO)
                        .build(),
                ParamProposal.builder()
                        .key("ad5463153dc3d24b9ff133e46136028bdc1edbb897f5a7cf1b37950c")
                        .epochNo(100)
                        .decentralisation(1.0)
                        .coinsPerUtxoSize(BigInteger.ONE)
                        .build(),
                ParamProposal.builder()
                        .key("b9547b8a57656539a8d9bc42c008e38d9c8bd9c8adbb1e73ad529497")
                        .epochNo(100)
                        .decentralisation(1.0)
                        .coinsPerUtxoSize(BigInteger.ONE)
                        .build()
        );

        final ParamProposal result = EpochParamUtil.getParamProposalToUpdate(prevParamProposals, delegationKeys, updateQuorum);

        assertNotNull(result);
        assertEquals(100, result.getEpochNo());
        assertEquals(1L, result.getDecentralisation());
        assertEquals(BigInteger.ONE, result.getCoinsPerUtxoSize());
    }
}
