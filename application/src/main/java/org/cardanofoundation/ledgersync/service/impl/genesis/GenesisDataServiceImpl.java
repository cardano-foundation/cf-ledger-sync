package org.cardanofoundation.ledgersync.service.impl.genesis;

import com.bloxbean.cardano.client.crypto.Base58;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.*;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TokenType;
import org.cardanofoundation.ledgersync.converter.AvvmAddressConverter;
import org.cardanofoundation.ledgersync.converter.CostModelConverter;
import org.cardanofoundation.ledgersync.dto.GenesisData;
import org.cardanofoundation.ledgersync.repository.BlockRepository;
import org.cardanofoundation.ledgersync.repository.SlotLeaderRepository;
import org.cardanofoundation.ledgersync.service.*;
import org.cardanofoundation.ledgersync.service.impl.BlockDataServiceImpl;
import org.cardanofoundation.ledgersync.service.impl.block.ByronMainAggregatorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Profile("!test-integration")
public class GenesisDataServiceImpl implements GenesisDataService {

    public static final String GENESIS_HASH = "GenesisHash";
    public static final String BYRON_GENESIS_HASH = "ByronGenesisHash";
    public static final int SLOT_LEADER_LENGTH = 56;
    public static final String AVVM_DISTR = "avvmDistr";
    public static final String NON_AVVM_BALANCES = "nonAvvmBalances";
    public static final String START_TIME = "startTime";
    public static final int COIN_PER_BYTE = 4310;
    public static final String PROTOCOL_PARAMS = "protocolParams";
    public static final String MIN_FEE_A = "minFeeA";
    public static final String MIN_FEE_B = "minFeeB";
    public static final String MAX_BLOCK_BODY_SIZE = "maxBlockBodySize";
    public static final String MAX_BLOCK_HEADER_SIZE = "maxBlockHeaderSize";
    public static final String MAX_TX_SIZE = "maxTxSize";
    public static final String KEY_DEPOSIT = "keyDeposit";
    public static final String POOL_DEPOSIT = "poolDeposit";
    public static final String E_MAX = "eMax";
    public static final String N_OPT = "nOpt";
    public static final String DECENTRALISATION_PARAM = "decentralisationParam";
    public static final String A0 = "a0";
    public static final String RHO = "rho";
    public static final String EXTRA_ENTROPY = "extraEntropy";
    public static final String TAG = "tag";
    public static final String PROTOCOL_VERSION = "protocolVersion";
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String MIN_UTXO_VALUE = "minUTxOValue";
    public static final String TAU = "tau";
    public static final String MIN_POOL_COST = "minPoolCost";
    public static final String LOVELACE_PER_UTXO_WORD = "lovelacePerUTxOWord";
    public static final String COLLATERAL_PERCENTAGE = "collateralPercentage";
    public static final String MAX_BLOCK_EX_UNITS = "maxBlockExUnits";
    public static final String MAX_TX_EX_UNITS = "maxTxExUnits";
    public static final String EXECUTION_PRICES = "executionPrices";
    public static final String PR_STEPS = "prSteps";
    public static final String PR_MEM = "prMem";
    public static final String COST_MODELS = "costModels";
    public static final String MAX_COLLATERAL_INPUTS = "maxCollateralInputs";
    public static final String NUMERATOR = "numerator";
    public static final String DENOMINATOR = "denominator";
    public static final String EX_UNITS_MEM = "exUnitsMem";
    public static final String EX_UNITS_STEPS = "exUnitsSteps";
    public static final String MAX_VALUE_SIZE = "maxValueSize";
    public static final String EPOCH_LENGTH = "epochLength";
    public static final String UPDATE_QUORUM = "updateQuorum";

    public static final String DELEGATION_KEYS = "genDelegs";
    private final static String POOL_VOTING_THRESHOLDS = "poolVotingThresholds";
    private final static String PVT_COMMITTEE_NORMAL = "committeeNormal";
    private final static String PVT_COMMITTEE_NO_CONFIDENCE = "committeeNoConfidence";
    private final static String PVT_HARD_FORK_INITIATION = "hardForkInitiation";
    private final static String PVT_MOTION_NO_CONFIDENCE = "motionNoConfidence";
    private final static String PVT_PP_SECURITY_GROUP = "ppSecurityGroup";
    private final static String D_REP_VOTING_THRESHOLDS = "dRepVotingThresholds";
    private final static String DVT_MOTION_NO_CONFIDENCE = "motionNoConfidence";
    private final static String DVT_COMMITTEE_NORMAL = "committeeNormal";
    private final static String DVT_COMMITTEE_NO_CONFIDENCE = "committeeNoConfidence";
    private final static String DVT_UPDATE_TO_CONSTITUTION = "updateToConstitution";
    private final static String DVT_HARD_FORK_INITIATION = "hardForkInitiation";
    private final static String DVT_PP_NETWORK_GROUP = "ppNetworkGroup";
    private final static String DVT_PP_ECONOMIC_GROUP = "ppEconomicGroup";
    private final static String DVT_PP_TECHNICAL_GROUP = "ppTechnicalGroup";
    private final static String DVT_PP_GOV_GROUP = "ppGovGroup";
    private final static String DVT_TREASURY_WITHDRAWAL = "treasuryWithdrawal";

    private final static String COMMITTEE_MIN_SIZE = "committeeMinSize";
    private final static String COMMITTEE_MAX_TERM_LENGTH = "committeeMaxTermLength";
    private final static String GOV_ACTION_LIFETIME = "govActionLifetime";
    private final static String GOV_ACTION_DEPOSIT = "govActionDeposit";
    private final static String D_REP_DEPOSIT = "dRepDeposit";
    private final static String D_REP_ACTIVITY = "dRepActivity";

    private final static String CONSTITUTION = "constitution";
    private final static String ANCHOR = "anchor";
    private final static String URL = "url";
    private final static String DATA_HASH = "dataHash";

    private final static String COMMITTEE = "committee";
    private final static String MEMBERS = "members";
    private final static String THRESHOLD = "threshold";
    private final static String CC_THRESHOLD_NUMERATOR = "numerator";
    private final static String CC_THRESHOLD_DENOMINATOR = "denominator";

    private final static String PLUTUS_V_3_COST_MODEL = "plutusV3CostModel";
    private final static String MIN_FEE_REF_SCRIPT_COST_PER_BYTE = "minFeeRefScriptCostPerByte";
    private final DefaultErrorViewResolver conventionErrorViewResolver;

    @Value("${genesis.byron}")
    String genesisByron;
    @Value("${genesis.shelley}")
    String genesisShelley;
    @Value("${genesis.alonzo}")
    String genesisAlonzo;
    @Value("${genesis.conway}")
    String genesisConway; // conway have no data to handle
    @Value("${genesis.config}")
    String genesisConfig;

    String genesisHash;
    GenesisData genesisData;
    Integer shelleyEpochLength;
    Long byronKnownTime;
    Integer updateQuorum;
    Set<String> delegationKeyHashes;

    final ObjectMapper objectMapper;
    final BlockRepository blockRepository;
    final SlotLeaderRepository slotLeaderRepository;

    final BlockDataServiceImpl blockDataService;
    final BlockSyncService blockSyncService;
    final ByronMainAggregatorServiceImpl blockAggregatorService;
    final CostModelService costModelService;
    final EpochParamService epochParamService;
    final GenesisFetching genesisFetching;

    @PostConstruct
    void init(){
        genesisData = GenesisData.builder()
                .txs(new ArrayList<>())
                .txOuts(new ArrayList<>())
                .build();

        fetchShelleyGenesis(genesisData);
        fetchAlonzoGenesis(genesisData);
        setupBabbageGenesis(genesisData);
        fetchConwayGenesis(genesisData);

        log.info("setup shelley genesis");
        epochParamService.setDefShelleyEpochParam(genesisData.getShelley());
        log.info("setup alonzo genesis");
        epochParamService.setDefAlonzoEpochParam(genesisData.getAlonzo());
        log.info("setup babbage genesis");
        epochParamService.setDefBabbageEpochParam(genesisData.getBabbage());
        log.info("setup conway genesis");
        epochParamService.setDefConwayEpochParam(genesisData.getConway());
//        log.info("setup genesis cost model");
//        costModelService.setGenesisCostModel(genesisData.getAlonzoCostModel());
    }

    @Transactional
    public void setupData(String genesisHash) {
        if (genesisHash == null) {
            this.genesisHash = fetchGenesisHash();
        } else {
            this.genesisHash = genesisHash;
        }

        log.info("Genesis hash: {}", this.genesisHash);

        // if block table have blocks do not thing
        if (blockRepository.getBlockIdHeight().isPresent()) {
            return;
        }
        log.info("setup byron genesis data");
        fetchTransactionAndTransactionOutput(genesisData);
        fetchBlockAndSlotLeader(genesisData);

        Block block = genesisData.getBlock();

        List<AggregatedTx> aggregatedTxs = genesisData.getTxOuts()
                .stream().map(txOut -> {
                    Tx tx = txOut.getTx();

                    List<AggregatedTxOut> aggregatedTxOuts = List.of(AggregatedTxOut.builder()
                            .address(AggregatedAddress.builder()
                                    .address(txOut.getAddress())
                                    .addressRaw(txOut.getAddressRaw())
                                    .build())
                            .index(BigInteger.ZERO.intValue())
                            .amounts(Collections.emptyList())
                            .nativeAmount(txOut.getValue())
                            .build());

                    return AggregatedTx
                            .builder()
                            .hash(tx.getHash())
                            .outSum(tx.getOutSum())
                            .fee(tx.getFee())
                            .validContract(Boolean.TRUE)
                            .blockHash(block.getHash())
                            .blockIndex(tx.getBlockIndex())
                            .certificates(Collections.emptyList())
                            .requiredSigners(Collections.emptySet())
                            .referenceInputs(Collections.emptySet())
                            .withdrawals(Collections.emptyMap())
                            .txInputs(Collections.emptySet())
                            .txOutputs(aggregatedTxOuts)
                            .build();
                }).toList();

        log.info("Insert slot leader size {}", genesisData.getSlotLeaders().size());
        slotLeaderRepository.saveAll(genesisData.getSlotLeaders());

        log.info("Insert block {}", block.getHash());

        blockDataService.saveAggregatedBlock(AggregatedBlock.builder()
                .era(Era.BYRON)
                .blockTime(genesisData.getStartTime())
                .blockSize(BigInteger.ZERO.intValue())
                .slotLeader(AggregatedSlotLeader.builder()
                        .hashRaw(block.getSlotLeader().getHash())
                        .build())
                .hash(block.getHash())
                .txList(aggregatedTxs)
                .txCount(block.getTxCount())
                .isGenesis(Boolean.TRUE)
                .build());

        blockSyncService.startBlockSyncing();
    }

    @Override
    public Long getByronKnownTime() {
        return byronKnownTime;
    }

    @Override
    public Integer getShelleyEpochLength() {
        return shelleyEpochLength;
    }

    @Override
    public Integer getUpdateQuorum() {
        return updateQuorum;
    }

    @Override
    public Set<String> getDelegationKeyHashes() {
        return delegationKeyHashes;
    }

    /**
     * Fetching data from byron-genesis.json link as json string then deserialize json string into map
     * for extracting, mapping to Java object. If Webclient can't fetch data from it will retry 10
     * times after retry attempts it will stop consumed
     *
     * @param genesisData
     */
    public void fetchTransactionAndTransactionOutput(GenesisData genesisData) {
        log.info("Fetch block from url {}", genesisByron);
        String genesisByronJson = genesisFetching.getContent(genesisByron);

        try {
            Map<String, Object> genesisByronJsonMap = objectMapper.readValue(genesisByronJson,
                    new TypeReference<>() {
                    });

            Map<String, String> map = (Map<String, String>) genesisByronJsonMap.get(AVVM_DISTR);

            if (!ObjectUtils.isEmpty(map)) {
                handleAvvmDistr(map, genesisData);
            } else {
                map = (Map<String, String>) genesisByronJsonMap.get(NON_AVVM_BALANCES);
                handleNonAvvmDistr(map, genesisData);
            }

            var epochOfSeconds = Long.valueOf(genesisByronJsonMap.get(START_TIME).toString());
            byronKnownTime = epochOfSeconds;
            genesisData.setStartTime(
                    Timestamp.valueOf(LocalDateTime.ofEpochSecond(epochOfSeconds, 0, ZoneOffset.UTC)));

        } catch (Exception e) {
            log.error("Genesis data at {} can't parse from json to java object", genesisByron);
            log.error("{} value \n {}", genesisByron, genesisByronJson);
            log.error("{}", e.getMessage());
            System.exit(0);
        }

    }

    /**
     * Get slot leader hash from genesis hash first 28 bytes.
     *
     * @param genesisData
     */
    public void fetchBlockAndSlotLeader(GenesisData genesisData) {
        try {
            final String slotLeaderHash = new String(
                    Arrays.copyOf(genesisHash.getBytes(), SLOT_LEADER_LENGTH));

            final SlotLeader genesisSlotLeader = SlotLeader.builder()
                    .hash(slotLeaderHash)
                    .description("Genesis slot leader")
                    .build();

            final SlotLeader epochBoundary = SlotLeader.builder()
                    .hash("00000000000000000000000000000000000000000000000000000000")
                    .description("Epoch boundary slot leader")
                    .build();

            genesisData.setSlotLeaders(List.of(genesisSlotLeader, epochBoundary));

            Block genesisBlock = Block.builder()
                    .hash(genesisHash)
                    .protoMajor(BigInteger.ZERO.intValue())
                    .protoMinor(BigInteger.ZERO.intValue())
                    .time(genesisData.getStartTime())
                    .size(BigInteger.ZERO.intValue())
                    .slotLeader(genesisSlotLeader)
                    .txList(genesisData.getTxs())
                    .txCount((long) genesisData.getTxs().size())
                    .build();

            genesisData.setBlock(genesisBlock);
        } catch (Exception e) {
            log.error("Genesis data error");
            log.error("{}", e.getMessage());
            System.exit(0);
        }
    }

    /**
     * shelley era https://cips.cardano.org/cips/cip55/
     *
     * @param genesisData
     * @see <a href="https://cips.cardano.org/cips/cip55/">cip55</a>
     */
    public void setupBabbageGenesis(GenesisData genesisData) {

        log.info("Set up babbage genesis, change uxto for word to byte");
        EpochParam genesisShelleyProtocols = EpochParam.builder()
                .coinsPerUtxoSize(BigInteger.valueOf(COIN_PER_BYTE))
                .build();

        genesisData.setBabbage(genesisShelleyProtocols);
    }

    public void fetchConwayGenesis(GenesisData genesisData) {
        log.info("Fetch block from url {}", genesisConway);
        String genesisConwayJson = genesisFetching.getContent(genesisConway);

        try {
            Map<String, Object> genesisConwayJsonMap = objectMapper.readValue(genesisConwayJson,
                    new TypeReference<>() {
                    });
            final var poolVotingThresholds = (Map<String, Object>) genesisConwayJsonMap.get(POOL_VOTING_THRESHOLDS);
            final var dRepVotingThresholds = (Map<String, Object>) genesisConwayJsonMap.get(D_REP_VOTING_THRESHOLDS);

            EpochParam genesisConwayProtocols = EpochParam.builder()
                    .pvtCommitteeNormal(convertObjectToBigDecimal(poolVotingThresholds.get(PVT_COMMITTEE_NORMAL)).doubleValue())
                    .pvtCommitteeNoConfidence(convertObjectToBigDecimal(poolVotingThresholds.get(PVT_COMMITTEE_NO_CONFIDENCE)).doubleValue())
                    .pvtHardForkInitiation(convertObjectToBigDecimal(poolVotingThresholds.get(PVT_HARD_FORK_INITIATION)).doubleValue())
                    .pvtMotionNoConfidence(convertObjectToBigDecimal(poolVotingThresholds.get(PVT_MOTION_NO_CONFIDENCE)).doubleValue())
                    .pvtPPSecurityGroup(convertObjectToBigDecimal(poolVotingThresholds.get(PVT_PP_SECURITY_GROUP)).doubleValue())
                    .dvtMotionNoConfidence(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_MOTION_NO_CONFIDENCE)).doubleValue())
                    .dvtCommitteeNormal(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_COMMITTEE_NORMAL)).doubleValue())
                    .dvtCommitteeNoConfidence(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_COMMITTEE_NO_CONFIDENCE)).doubleValue())
                    .dvtUpdateToConstitution(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_UPDATE_TO_CONSTITUTION)).doubleValue())
                    .dvtHardForkInitiation(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_HARD_FORK_INITIATION)).doubleValue())
                    .dvtPPNetworkGroup(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_PP_NETWORK_GROUP)).doubleValue())
                    .dvtPPEconomicGroup(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_PP_ECONOMIC_GROUP)).doubleValue())
                    .dvtPPTechnicalGroup(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_PP_TECHNICAL_GROUP)).doubleValue())
                    .dvtPPGovGroup(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_PP_GOV_GROUP)).doubleValue())
                    .dvtTreasuryWithdrawal(convertObjectToBigDecimal(dRepVotingThresholds.get(DVT_TREASURY_WITHDRAWAL)).doubleValue())
                    .committeeMinSize(convertObjecToBigInteger(genesisConwayJsonMap.get(COMMITTEE_MIN_SIZE)))
                    .committeeMaxTermLength(convertObjecToBigInteger(genesisConwayJsonMap.get(COMMITTEE_MAX_TERM_LENGTH)))
                    .govActionLifetime(convertObjecToBigInteger(genesisConwayJsonMap.get(GOV_ACTION_LIFETIME)))
                    .govActionDeposit(convertObjecToBigInteger(genesisConwayJsonMap.get(GOV_ACTION_DEPOSIT)))
                    .drepDeposit(convertObjecToBigInteger(genesisConwayJsonMap.get(D_REP_DEPOSIT)))
                    .drepActivity(convertObjecToBigInteger(genesisConwayJsonMap.get(D_REP_ACTIVITY)))
                    .build();

            if (genesisConwayJsonMap.get(MIN_FEE_REF_SCRIPT_COST_PER_BYTE) != null) {
                genesisConwayProtocols.setMinFeeRefScriptCostPerByte(convertObjectToBigDecimal(genesisConwayJsonMap.get(MIN_FEE_REF_SCRIPT_COST_PER_BYTE)).doubleValue());
            }

            if (genesisConwayJsonMap.get(PLUTUS_V_3_COST_MODEL) != null) {
                final Map<String, List<Long>> plutusV3CostModel = new HashMap<>();

                plutusV3CostModel.put(CostModelConverter.PLUTUS_V3, (ArrayList<Long>) genesisConwayJsonMap.get(PLUTUS_V_3_COST_MODEL));

                final var costModel = CostModel.builder()
                        .costs(objectMapper.writeValueAsString(plutusV3CostModel))
                        .hash("genesis.conway") //TODO check later
                        .build();

                genesisConwayProtocols.setCostModel(costModel);
            }

            genesisData.setConway(genesisConwayProtocols);
        } catch (Exception e) {
            log.error("Genesis data at {} can't parse from json to java object", genesisConway);
            log.error("{} value \n {}", genesisAlonzo, genesisConwayJson);
            log.error("{}", e.getMessage());
            System.exit(0);
        }
    }
    /**
     * Fetching data from alonzo-genesis.json link extracting and mapping protocol parameters in
     * alonzo era
     *
     * @param genesisData
     * @see <a href="https://cips.cardano.org/cips/cip28/">cip28</a>
     */
    public void fetchAlonzoGenesis(GenesisData genesisData) {

        log.info("Fetch block from url {}", genesisAlonzo);
        String genesisAlonzoJson = genesisFetching.getContent(genesisAlonzo);

        try {
            Map<String, Object> genesisAlonzoJsonMap = objectMapper.readValue(genesisAlonzoJson,
                    new TypeReference<>() {
                    });

            final var executionPrices = (Map<String, Object>) genesisAlonzoJsonMap.get(EXECUTION_PRICES);
            final var prSteps = (Map<String, Object>) executionPrices.get(PR_STEPS);
            final var prMem = (Map<String, Object>) executionPrices.get(PR_MEM);

            final var maxTxExUnits = (Map<String, Object>) genesisAlonzoJsonMap.get(MAX_TX_EX_UNITS);
            final var maxBlockExUnits = (Map<String, Object>) genesisAlonzoJsonMap.get(
                    MAX_BLOCK_EX_UNITS);

            final var costModel = CostModel.builder()
                    .costs(objectMapper.writeValueAsString(genesisAlonzoJsonMap.get(COST_MODELS)))
                    .hash(CostModelConverter.getCostModelHashFromGenesis(
                            (Map) genesisAlonzoJsonMap.get(COST_MODELS)))
                    .build();

            EpochParam genesisShelleyProtocols = EpochParam.builder()
                    .minUtxoValue(null)
                    .collateralPercent((Integer) genesisAlonzoJsonMap.get(COLLATERAL_PERCENTAGE))
                    .maxCollateralInputs((Integer) genesisAlonzoJsonMap.get(MAX_COLLATERAL_INPUTS))
                    .priceMem(convertObjectToBigDecimal(prMem.get(NUMERATOR))
                            .divide(convertObjectToBigDecimal(prMem.get(DENOMINATOR))).doubleValue())
                    .priceStep(convertObjectToBigDecimal(prSteps.get(NUMERATOR))
                            .divide(convertObjectToBigDecimal(prSteps.get(DENOMINATOR))).doubleValue())
                    .maxTxExMem(convertObjecToBigInteger(maxTxExUnits.get(EX_UNITS_MEM)))
                    .maxTxExSteps(convertObjecToBigInteger(maxTxExUnits.get(EX_UNITS_STEPS)))
                    .maxBlockExMem(convertObjecToBigInteger(maxBlockExUnits.get(EX_UNITS_MEM)))
                    .maxBlockExSteps(convertObjecToBigInteger(maxBlockExUnits.get(EX_UNITS_STEPS)))
                    .maxValSize(convertObjecToBigInteger(genesisAlonzoJsonMap.get(MAX_VALUE_SIZE)))
                    .costModel(costModel)
                    .coinsPerUtxoSize(
                            convertObjecToBigInteger(genesisAlonzoJsonMap.get(LOVELACE_PER_UTXO_WORD)))
                    .build();

            genesisData.setAlonzo(genesisShelleyProtocols);
            genesisData.setAlonzoCostModel(costModel);
        } catch (Exception e) {
            log.error("Genesis data at {} can't parse from json to java object", genesisAlonzo);
            log.error("{} value \n {}", genesisAlonzo, genesisAlonzoJson);
            log.error("{}", e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Fetching data from shelley-genesis.json link extracting and mapping protocol parameters in
     * shelley era
     *
     * @param genesisData
     * @see <a href="https://cips.cardano.org/cips/cip9/">cip9</a>
     */
    public void fetchShelleyGenesis(GenesisData genesisData) {

        log.info("Fetch block from url {}", genesisShelley);
        String genesisShelleyJson = genesisFetching.getContent(genesisShelley);

        try {
            Map<String, Object> genesisShelleyJsonMap = objectMapper.readValue(genesisShelleyJson,
                    new TypeReference<>() {
                    });

            shelleyEpochLength = Integer.parseInt(genesisShelleyJsonMap.get(EPOCH_LENGTH).toString());
            updateQuorum = Integer.parseInt(genesisShelleyJsonMap.get(UPDATE_QUORUM).toString());
            delegationKeyHashes = ((Map<String, Object>) genesisShelleyJsonMap.get(DELEGATION_KEYS)).keySet();
            var protocolParams = (Map<String, Object>) genesisShelleyJsonMap.get(PROTOCOL_PARAMS);
            var extraEntropy = (Map<String, Object>) protocolParams.get(EXTRA_ENTROPY);
            var protocolVersion = (Map<String, Object>) protocolParams.get(PROTOCOL_VERSION);
            EpochParam genesisShelleyProtocols = EpochParam.builder()
                    .minFeeA((Integer) protocolParams.get(MIN_FEE_A))
                    .minFeeB((Integer) protocolParams.get(MIN_FEE_B))
                    .maxBlockSize((Integer) protocolParams.get(MAX_BLOCK_BODY_SIZE))
                    .maxTxSize((Integer) protocolParams.get(MAX_TX_SIZE))
                    .maxBhSize((Integer) protocolParams.get(MAX_BLOCK_HEADER_SIZE))
                    .keyDeposit(convertObjecToBigInteger(protocolParams.get(KEY_DEPOSIT)))
                    .poolDeposit(convertObjecToBigInteger(protocolParams.get(POOL_DEPOSIT)))
                    .maxEpoch((Integer) protocolParams.get(E_MAX))
                    .optimalPoolCount((Integer) protocolParams.get(N_OPT))
                    .influence((Double) protocolParams.get(A0))
                    .monetaryExpandRate((Double) protocolParams.get(RHO))
                    .decentralisation(
                            Double.parseDouble(String.valueOf(protocolParams.get(DECENTRALISATION_PARAM))))
                    .extraEntropy(extraEntropy.get(TAG).toString())
                    .protocolMajor((Integer) protocolVersion.get(MAJOR))
                    .protocolMinor((Integer) protocolVersion.get(MINOR))
                    .minUtxoValue(convertObjecToBigInteger(protocolParams.get(MIN_UTXO_VALUE)))
                    .minPoolCost(convertObjecToBigInteger((protocolParams.get(MIN_POOL_COST))))
                    .treasuryGrowthRate((Double) protocolParams.get(TAU))
                    .build();

            genesisData.setShelley(genesisShelleyProtocols);

        } catch (Exception e) {
            log.error("Genesis data at {} can't parse from json to java object", genesisShelley);
            log.error("{} value \n {}", genesisShelley, genesisShelleyJson);
            log.error("{}", e.getMessage());
            System.exit(0);
        }
    }
    /**
     * Read genesis hash from config.json
     */
    public String fetchGenesisHash() {
        String genesisConfigJson = genesisFetching.getContent(genesisConfig);

        try {
            Map<String, Object> genesisConfigJsonMap = objectMapper.readValue(genesisConfigJson,
                    new TypeReference<>() {
                    });

            return (String) genesisConfigJsonMap.get(BYRON_GENESIS_HASH);
        } catch (Exception e) {
            log.error("Genesis data at {} can't parse from json to java object", genesisConfig);
            log.error("{} value \n {}", genesisConfig, genesisConfigJson);
            log.error("{}", e.getMessage());
            System.exit(0);
        }

        return null;
    }

    /**
     * Handle AVVM addresses and map to transaction and transaction out
     *
     * @param avvmDistr   map of avvmDistr
     * @param genesisData
     */
    private void handleAvvmDistr(Map<String, String> avvmDistr, GenesisData genesisData) {
        avvmDistr.forEach((avvmAddress, amount) -> {

            BigInteger value = new BigInteger(amount);

            final byte[] byronAddressRawCbor = AvvmAddressConverter.convertAvvmToByronAddressCbor(
                            avvmAddress)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid avvm address"));
            final String byronAddress = Base58.encode(byronAddressRawCbor);
            final String txHash = HexUtil.encodeHexString(
                    Blake2bUtil.blake2bHash256(Base58.decode(byronAddress)));

            Tx tx = Tx.builder()
                    .hash(txHash)
                    .blockIndex((long) genesisData.getTxs().size())
                    .fee(BigInteger.ZERO)
                    .size(BigInteger.ZERO.intValue())
                    .scriptSize(BigInteger.ZERO.intValue())
                    .deposit(BigInteger.ZERO.longValue())
                    .outSum(value)
                    .validContract(Boolean.TRUE)
                    .build();

            TxOut txOut = TxOut.builder()
                    .index((short) BigInteger.ZERO.intValue())
                    .address(byronAddress)
                    .addressRaw(byronAddressRawCbor)
                    .tokenType(TokenType.NATIVE_TOKEN)
                    .value(value)
                    .addressHasScript(Boolean.FALSE)
                    .tx(tx)
                    .build();

            genesisData.getTxs().add(tx);
            genesisData.getTxOuts().add(txOut);
        });
    }

    /**
     * Handle non AVVM addresses and map to transaction and transaction out
     *
     * @param nonAvvmBalances map of avvmDistr
     * @param genesisData
     */
    private void handleNonAvvmDistr(Map<String, String> nonAvvmBalances,
                                    GenesisData genesisData) {
        nonAvvmBalances.forEach((address, amount) -> {
            final var addressRaw = Base58.decode(address);
            BigInteger value = new BigInteger(amount);
            final String txHash = HexUtil.encodeHexString(
                    Blake2bUtil.blake2bHash256(addressRaw));

            Tx tx = Tx.builder()
                    .hash(txHash)
                    .blockIndex((long) genesisData.getTxs().size())
                    .fee(BigInteger.ZERO)
                    .size(BigInteger.ZERO.intValue())
                    .scriptSize(BigInteger.ZERO.intValue())
                    .deposit(BigInteger.ZERO.longValue())
                    .outSum(value)
                    .validContract(Boolean.TRUE)
                    .build();

            TxOut txOut = TxOut.builder()
                    .index(Short.valueOf((short) BigInteger.ZERO.intValue()))
                    .address(address)
                    .addressRaw(addressRaw)
                    .value(value)
                    .tokenType(TokenType.NATIVE_TOKEN)
                    .addressHasScript(Boolean.FALSE)
                    .tx(tx)
                    .build();

            genesisData.getTxs().add(tx);
            genesisData.getTxOuts().add(txOut);
        });
    }

    public static BigInteger convertObjecToBigInteger(Object o) {
        return new BigInteger(String.valueOf(o));
    }

    public static BigDecimal convertObjectToBigDecimal(Object o) {
        return new BigDecimal(String.valueOf(o));
    }


}
