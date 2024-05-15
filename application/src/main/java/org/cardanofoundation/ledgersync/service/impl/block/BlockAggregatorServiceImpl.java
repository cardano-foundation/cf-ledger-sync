package org.cardanofoundation.ledgersync.service.impl.block;

import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.yaci.core.model.*;
import com.bloxbean.cardano.yaci.core.model.certs.*;
import com.bloxbean.cardano.yaci.store.events.BlockEvent;
import com.bloxbean.cardano.yaci.store.events.EventMetadata;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.common.common.constant.Constant;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.aggregate.*;
import org.cardanofoundation.ledgersync.constant.ConsumerConstant;
import org.cardanofoundation.ledgersync.service.BlockAggregatorService;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.cardanofoundation.ledgersync.service.SlotLeaderService;
import org.cardanofoundation.ledgersync.util.AddressUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BlockAggregatorServiceImpl extends BlockAggregatorService<BlockEvent> {
    private int protocolMagic;

    public BlockAggregatorServiceImpl(
            SlotLeaderService slotLeaderService,
            BlockDataService blockDataService,
            @Value("${store.cardano.protocol-magic}") int protocolMagic
    ) {
        super(slotLeaderService, blockDataService);
        this.protocolMagic = protocolMagic;
    }

    @Override
    public AggregatedBlock aggregateBlock(BlockEvent blockEvent) {
        EventMetadata eventMetadata = blockEvent.getMetadata();
        Block blockCddl = blockEvent.getBlock();
        if (blockCddl.getTransactionBodies().size() != blockCddl.getTransactionWitness().size()) {
            log.error(
                    "Transaction body size [{}] different with transaction witness size [{}], Block no: {}, Block hash {}",
                    blockCddl.getTransactionBodies().size(), blockCddl.getTransactionWitness().size(),
                    eventMetadata.getBlock(), eventMetadata.getBlockHash());
            throw new IllegalStateException();
        }
        return mapBlockCddlToAggregatedBlock(eventMetadata, blockCddl);
    }

    /**
     * handle mapping {@link Block} to
     * {@link AggregatedBlock}
     *
     * @param blockCddl {@link Block} cddl block
     * @return {@link AggregatedBlock}
     */
    private AggregatedBlock mapBlockCddlToAggregatedBlock(EventMetadata eventMetadata, Block blockCddl) {

        var header = blockCddl.getHeader();
        var blockHash = header.getHeaderBody().getBlockHash();

        //var epoch = blockCddl.getHeader().getHeaderBody().getSlotId();
        //var epochNo = (int) epoch.getValue();
        var epochNo = eventMetadata.getEpochNumber();
//    var epochSlotNo = (int) epoch.getSlotOfEpoch();
        var epochSlotNo = eventMetadata.getEpochSlot();
        var slotNo = eventMetadata.getSlot();
        var blockNo = eventMetadata.getBlock();
        var prevHash = blockCddl.getHeader().getHeaderBody().getPrevHash();
        var slotLeader = slotLeaderService.getSlotLeaderHashAndPrefix(blockCddl);

        var blockSize = (int) blockCddl.getHeader().getHeaderBody().getBlockBodySize();
        var blockTime = Timestamp.valueOf(LocalDateTime.ofEpochSecond(
                eventMetadata.getBlockTime(), 0, ZoneOffset.ofHours(0)));
        var txCount = (long) blockCddl.getTransactionBodies().size();

        var headerBody = header.getHeaderBody();
        var protocolVersion = headerBody.getProtocolVersion();
        var protoMajor = (int) protocolVersion.get_1();
        var protoMinor = (int) protocolVersion.get_2();
        var vrfKey = Bech32.encode(
                HexUtil.decodeHexString(headerBody.getVrfVkey()), ConsumerConstant.VRF_KEY_PREFIX);

        //TODO: Initialize operational certificate
        var opCert = headerBody.getOperationalCert();
        var actualOpCert = opCert != null? opCert.getHotVKey(): null;
        Long opCertCounter = opCert != null? (long) opCert.getSequenceNumber(): null;

        List<AggregatedTx> txList = mapCddlTxToAggregatedTx(eventMetadata, blockCddl);
        return AggregatedBlock.builder()
                .era(Era.valueOf(blockCddl.getEra().name().toUpperCase()))
                .network(protocolMagic)
                .hash(blockHash)
                .epochNo(eventMetadata.getEpochNumber())
                .epochSlotNo((int) eventMetadata.getEpochSlot())
                .slotNo(slotNo)
                .blockNo(blockNo)
                .prevBlockHash(prevHash)
                .slotLeader(slotLeader)
                .blockSize(blockSize)
                .blockTime(blockTime)
                .txCount(txCount)
                .protoMajor(protoMajor)
                .protoMinor(protoMinor)
                .vrfKey(vrfKey)
                .opCert(actualOpCert)       //TODO: Initialize operational certificate
                .opCertCounter(opCertCounter) //TODO: Initialize operational certificate
                .txList(txList)
                .auxiliaryDataMap(blockCddl.getAuxiliaryDataMap())
                .isGenesis(Boolean.FALSE)
                .build();
    }

    /**
     * This method transforms CDDL tx data to aggregated tx objects, which
     * will be used later by block processing and transactions handling
     * services
     *
     * @param blockCddl transformed block data from CDDL, containing tx data
     * @return list of aggregated tx objects
     */
    private List<AggregatedTx> mapCddlTxToAggregatedTx(EventMetadata eventMetadata, Block blockCddl) {
        List<TransactionBody> transactionBodies = blockCddl.getTransactionBodies();
        List<Witnesses> transactionWitness = blockCddl.getTransactionWitness();
        Set<Integer> invalidTransactions = new HashSet<>();

        if (!CollectionUtils.isEmpty(blockCddl.getInvalidTransactions())) {
            invalidTransactions.addAll(blockCddl.getInvalidTransactions());
        }


        return IntStream.range(0, transactionBodies.size()).mapToObj(txIdx -> {
            boolean validContract = !invalidTransactions.contains(txIdx);
            TransactionBody transactionBody = transactionBodies.get(txIdx);
            Witnesses witnesses = transactionWitness.get(txIdx);
            AggregatedTx aggregatedTx = txToAggregatedTx(eventMetadata.getBlockHash(),
                    validContract, txIdx, transactionBody, witnesses);
            mapStakeAddressToTxHash(aggregatedTx, protocolMagic);
            aggregatedTx.setAuxiliaryDataHash(transactionBody.getAuxiliaryDataHash());
            return aggregatedTx;
        }).collect(Collectors.toList());
    }

    /**
     * This method searches for every stake address existed in an aggregated tx, then
     * mark all the stake addresses' first appeared tx hash as the hash of currently
     * processing tx, in case the stake address has not existed in any other tx before.
     * If the stake address has already been marked, it is skipped
     *
     * @param aggregatedTx aggregated tx in process
     * @param network      network magic of this tx
     */
    private void mapStakeAddressToTxHash(AggregatedTx aggregatedTx, int network) {
        String txHash = aggregatedTx.getHash();

        // From txOutputs and collateral return
        List<AggregatedTxOut> txOutputs = aggregatedTx.getTxOutputs();
        txOutputs.stream()
                .map(AggregatedTxOut::getAddress)
                .filter(AggregatedAddress::hasStakeReference)
                .forEach(aggregatedAddress -> blockDataService
                        .saveFirstAppearedTxHashForStakeAddress(aggregatedAddress.getStakeAddress(), txHash));

        AggregatedTxOut collateralReturn = aggregatedTx.getCollateralReturn();
        if (Objects.nonNull(collateralReturn)) {
            AggregatedAddress address = collateralReturn.getAddress();
            if (address.hasStakeReference()) {
                blockDataService.saveFirstAppearedTxHashForStakeAddress(address.getStakeAddress(), txHash);
            }
        }

        // Do not process further if this tx is invalid
        if (!aggregatedTx.isValidContract()) {
            return;
        }

        // From withdrawals
        if (!CollectionUtils.isEmpty(aggregatedTx.getWithdrawals())) {
            aggregatedTx.getWithdrawals().keySet().forEach(rewardAccountHex ->
                    blockDataService.saveFirstAppearedTxHashForStakeAddress(rewardAccountHex, txHash));
        }

        // From certificates
        aggregatedTx.getCertificates().forEach(certificate ->
                mapCertificateStakeAddressToTxHash(network, txHash, certificate));
    }

    /**
     * This method marks the stake address(s) inside a certificate's first appearance tx hash
     *
     * @param network     network magic of this tx
     * @param txHash      tx hash of currently processing tx
     * @param certificate certificate in process
     */
    private void mapCertificateStakeAddressToTxHash(
            int network, String txHash, Certificate certificate) {
        CertificateType certType = certificate.getType();
        switch (certType) {
            case MOVE_INSTATENEOUS_REWARDS_CERT:
                MoveInstataneous moveInstataneous = (MoveInstataneous) certificate;
                if (Objects.nonNull(moveInstataneous.getAccountingPotCoin())) {
                    break;
                }

                moveInstataneous.getStakeCredentialCoinMap()
                        .keySet().stream()
                        .map(credential -> AddressUtil.getRewardAddressString(credential, network))
                        .forEach(rewardAddress -> blockDataService
                                .saveFirstAppearedTxHashForStakeAddress(rewardAddress, txHash));
                break;
            case STAKE_REGISTRATION:
                StakeRegistration stakeRegistration = (StakeRegistration) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeRegistration.getStakeCredential(), network), txHash);
                break;
            case STAKE_DEREGISTRATION:
                StakeDeregistration stakeDeregistration = (StakeDeregistration) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeDeregistration.getStakeCredential(), network), txHash);
                break;
            case STAKE_DELEGATION:
                StakeDelegation stakeDelegation = (StakeDelegation) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeDelegation.getStakeCredential(), network), txHash);
                break;
            case POOL_REGISTRATION:
                PoolRegistration poolRegistration = (PoolRegistration) certificate;

                // Reward account
                // Workaround for bug https://github.com/input-output-hk/cardano-db-sync/issues/546
                String rewardAccountHex = poolRegistration.getPoolParams().getRewardAccount();
                byte[] rewardAccountBytes = HexUtil.decodeHexString(rewardAccountHex);
                int networkId = Constant.isTestnet(network) ? 0 : 1;
                byte header = rewardAccountBytes[0];
                if (((header & 0xff) & networkId) == 0) {
                    rewardAccountBytes[0] = (byte) ((header & ~1) | networkId);
                }
                blockDataService.saveFirstAppearedTxHashForStakeAddress(
                        HexUtil.encodeHexString(rewardAccountBytes), txHash);

                // Pool owners
                poolRegistration.getPoolParams().getPoolOwners().forEach(poolOwnerHash -> {
                    String stakeAddressHex = AddressUtil.getRewardAddressString(
                            new StakeCredential(StakeCredType.ADDR_KEYHASH, poolOwnerHash), network);
                    blockDataService.saveFirstAppearedTxHashForStakeAddress(stakeAddressHex, txHash);
                });
                break;
            case REG_CERT:
                RegCert regCert = (RegCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(regCert.getStakeCredential(), network), txHash);
                break;
            case UNREG_CERT:
                UnregCert unregCert = (UnregCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(unregCert.getStakeCredential(), network), txHash);
                break;
            case STAKE_REG_DELEG_CERT:
                StakeRegDelegCert stakeRegDelegCert = (StakeRegDelegCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeRegDelegCert.getStakeCredential(), network), txHash);
                break;
            case STAKE_VOTE_DELEG_CERT:
                StakeVoteDelegCert stakeVoteDelegCert = (StakeVoteDelegCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeVoteDelegCert.getStakeCredential(), network), txHash);
                break;
            case VOTE_REG_DELEG_CERT:
                VoteRegDelegCert voteRegDelegCert = (VoteRegDelegCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(voteRegDelegCert.getStakeCredential(), network), txHash);
                break;
            case STAKE_VOTE_REG_DELEG_CERT:
                StakeVoteRegDelegCert stakeVoteRegDelegCert = (StakeVoteRegDelegCert) certificate;
                blockDataService.saveFirstAppearedTxHashForStakeAddress(AddressUtil
                        .getRewardAddressString(stakeVoteRegDelegCert.getStakeCredential(), network), txHash);
                break;
            default:
                break;
        }
    }

    /**
     * This method transforms a single CDDL tx data to aggregated tx object
     *
     * @param blockHash       block hash where the currently processing tx lies in
     * @param validContract   currently processing tx's contract validity
     * @param idx             currently processing tx's index within a block
     * @param transactionBody transformed CDDL tx data
     * @param witnesses       currently processing tx's witnesses data
     * @return aggregated tx object
     */
    private AggregatedTx txToAggregatedTx(String blockHash, boolean validContract,
                                          int idx, TransactionBody transactionBody, Witnesses witnesses) {
        // Handle basic tx data
        var txHash = transactionBody.getTxHash();
        // Converts CDDL tx ins data to aggregated tx ins
        var txInputs = transactionBody.getInputs();
        /*
         * Converts CDDL tx outs/collateral return data to aggregated tx outs/collateral return
         * Both tx out and collateral return use the same object, their aggregated outputs are also
         * under the same object, just different field name
         */
        var aggregatedTxOuts = txOutsToAggregatedTxOuts(transactionBody.getOutputs());
        var collateralReturn = AggregatedTxOut.from(transactionBody.getCollateralReturn());
        if (Objects.nonNull(collateralReturn)) {
            collateralReturn.setIndex(aggregatedTxOuts.size());
        }
        List<AggregatedTxOut> collateralReturnsSingleList =
                (!validContract && Objects.nonNull(collateralReturn))
                        ? List.of(collateralReturn) : Collections.emptyList();
        /*
         * Handle address balance from tx outputs or collateral return
         * This is initial step of calculating balance. The same process will be
         * done when tx ins are taken into account
         */
        mapAggregatedTxOutsToAddressBalanceMap(
                validContract ? aggregatedTxOuts : collateralReturnsSingleList, txHash);

        var fee = transactionBody.getFee();
        if (!validContract && Objects.nonNull(transactionBody.getTotalCollateral())) {
            fee = transactionBody.getTotalCollateral();
        }

        var outSum = BigInteger.ZERO;
        if (validContract) { //TODO -- verify how validContract is set
            outSum = AggregatedTxOut.calculateOutSum(transactionBody.getOutputs());
        } else if (Objects.nonNull(transactionBody.getCollateralReturn())) {
            outSum = AggregatedTxOut.calculateOutSum(List.of(transactionBody.getCollateralReturn()));
        }

        AggregatedTx aggregatedTx = AggregatedTx.builder()
                .hash(txHash)
                .blockHash(blockHash)
                .blockIndex(idx)
                .validContract(validContract)
                .deposit(0)
                .txInputs(txInsToAggregatedTxIns(txInputs))
                .collateralInputs(
                        txInsToAggregatedTxIns(transactionBody.getCollateralInputs()))
                .referenceInputs(
                        txInsToAggregatedTxIns(transactionBody.getReferenceInputs()))
                .txOutputs(aggregatedTxOuts)
                .collateralReturn(collateralReturn)
                .certificates(transactionBody.getCertificates())
                .withdrawals(transactionBody.getWithdrawals())
                .update(transactionBody.getUpdate())
                .mint(transactionBody.getMint())
                .requiredSigners(transactionBody.getRequiredSigners())
                .witnesses(witnesses)
                .votingProcedures(transactionBody.getVotingProcedures())
                .proposalProcedures(transactionBody.getProposalProcedures())
                .fee(fee)
                .outSum(outSum)
                .deposit(0)
                .build();

        return aggregatedTx;
    }

    private Set<AggregatedTxIn> txInsToAggregatedTxIns(Set<TransactionInput> txInputs) {
        if (CollectionUtils.isEmpty(txInputs)) {
            return Collections.emptySet();
        }

        return txInputs.stream().map(AggregatedTxIn::of).collect(Collectors.toSet());
    }

    private List<AggregatedTxOut> txOutsToAggregatedTxOuts(List<TransactionOutput> txOutputs) {
        if (CollectionUtils.isEmpty(txOutputs)) {
            return Collections.emptyList();
        }

        List<AggregatedTxOut> aggregatedTxOuts = new ArrayList<>();
        for (int i = 0; i < txOutputs.size(); i++) {
            AggregatedTxOut aggregatedTxOut = AggregatedTxOut.from(txOutputs.get(i));
            aggregatedTxOut.setIndex(i);
            aggregatedTxOuts.add(aggregatedTxOut);
        }

        return aggregatedTxOuts;
//        return txOutputs.stream().map(AggregatedTxOut::from).collect(Collectors.toList());
    }
}
