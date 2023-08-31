package org.cardanofoundation.ledgersync.explorerconsumer.integration;

import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.explorer.consumercommon.enumeration.TokenType;
import org.cardanofoundation.ledgersync.common.common.kafka.CommonBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.config.RedisTestConfig;
import org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka.KafkaProducer;
import org.cardanofoundation.ledgersync.explorerconsumer.integration.kafka.TestBlockListener;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.*;
import org.cardanofoundation.ledgersync.explorerconsumer.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@Slf4j
//@Profile("test-integration")
//@ActiveProfiles({"test-integration", "redis-standalone","caching"})
//@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
//@SpringBootTest(classes = RedisTestConfig.class)
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@EmbeddedKafka(partitions = 1,
//    brokerProperties = {"listeners=PLAINTEXT://localhost:29999", "port=29999"})
//@TestInstance(Lifecycle.PER_CLASS)
//@EnabledIf(value = "#{environment['spring.profiles.active'] == 'test-integration'}", loadContext = true)
class BlockSyncServiceIntegrationTest {

    //TODO -- refactor fix tests

//  @Value("${test.blocks.json-path}")
//  String blocksJsonPath;
//
//  @Value("${test.topic1}")
//  String testTopic;
//
//  @Autowired
//  KafkaProducer kafkaProducer;
//
//  @Autowired
//  TestBlockListener testBlockListener;
//
//  @Autowired
//  BlockRepository blockRepository;
//
//  @Autowired
//  TxRepository txRepository;
//
//  @Autowired
//  TxOutRepository txOutRepository;
//
//  @Autowired
//  MultiAssetTxOutRepository multiAssetTxOutRepository;
//
//  @Autowired
//  MultiAssetRepository multiAssetRepository;
//
//  @Autowired
//  StakeAddressRepository stakeAddressRepository;
//
//  @Autowired
//  PoolHashRepository poolHashRepository;
//
//  List<CommonBlock> commonBlocks;
//
//  @BeforeAll
//  void init() throws Exception {
//    log.info("Initializing test environment");
//    String blocksJson = FileUtil.readFile(blocksJsonPath);
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//    commonBlocks = Arrays.asList(
//        objectMapper.readValue(blocksJson, CommonBlock[].class));
//
//    initializeEntities();
//  }
//
//  private void initializeEntities() {
//    // Fake blocks
//    Block block = Block.builder()
//        .hash("43d58aa00099c44787fdb174db22823494814eb2cdf209b044ca20cc5cf62b25")
//        .epochNo(0)
//        .epochSlotNo(3312)
//        .slotNo(3312L)
//        .blockNo(3313L)
//        .previous(null)
//        .slotLeader(null)
//        .size(910)
//        .time(Timestamp.valueOf(LocalDateTime.now()))
//        .txCount(1L)
//        .protoMajor(0)
//        .protoMinor(0)
//        .build();
//    blockRepository.save(block);
//
//    block = Block.builder()
//        .hash("1a1f7e1f1bd447586c998872527341f455379dfb7ade21ddb05735d42166cbc1")
//        .epochNo(31)
//        .epochSlotNo(10359)
//        .slotNo(11760759L)
//        .blockNo(230163L)
//        .previous(null)
//        .slotLeader(null)
//        .size(734)
//        .time(Timestamp.valueOf(LocalDateTime.now()))
//        .txCount(2L)
//        .protoMajor(7)
//        .protoMinor(0)
//        .vrfKey("vrf_vk1qw0zp2fknntl8yf60wfm452sa6puzqkr3g3yp8xf7yhg0u22s3mqa2z7f7")
//        .opCert("89a362db11cbd020ab8c2d63062fad8398a5391df3a0f699948db9bb2fe5536d")
//        .opCertCounter(0L)
//        .build();
//    blockRepository.save(block);
//
//    // Fake txs, tx outs, stake addresses and assets
//    Tx tx = Tx.builder()
//        .hash("6d2174d3956d8eb2b3e1e198e817ccf1332a599d5d7320400bfd820490d706be")
//        .block(block)
//        .blockIndex(0L)
//        .outSum(BigInteger.valueOf(9987552528L))
//        .fee(BigInteger.valueOf(168581L))
//        .validContract(true)
//        .deposit(0L)
//        .size(3850)
//        .build();
//    txRepository.save(tx);
//
//    TxOut txOut = TxOut.builder()
//        .tx(tx)
//        .index((short) 0)
//        .dataHash("15461aa490b224fe541f3568e5d7704e0d88460cde9f418f700e2b6864d8d3c9")
//        .tokenType(TokenType.NATIVE_TOKEN)
//        .addressHasScript(true)
//        .stakeAddress(null)
//        .paymentCred("51936f3c98a04b6609aa9b5c832ba1182cf43a58e534fcc05db09d69")
//        .address("addr_test1wpgexmeunzsykesf42d4eqet5yvzeap6trjnflxqtkcf66g0kpnxt")
//        .addressRaw(
//            HexUtil.decodeHexString("7051936f3c98a04b6609aa9b5c832ba1182cf43a58e534fcc05db09d69"))
//        .value(BigInteger.valueOf(50000000L))
//        .build();
//    txOutRepository.save(txOut);
//
//    tx = Tx.builder()
//        .hash("69a4199509a6bc81daf91eea261f14b8e67870fa501accbad154cd8857d5a257")
//        .block(block)
//        .blockIndex(1L)
//        .outSum(BigInteger.valueOf(1784474087L))
//        .fee(BigInteger.valueOf(473713L))
//        .validContract(true)
//        .deposit(2000000L)
//        .build();
//    txRepository.save(tx);
//
//    StakeAddress stakeAddress = StakeAddress.builder()
//        .hashRaw("e0f9ac6b84f2d668887b31ca229eb7c8bd8d20d547c49ab446f9f3456f")
//        .view("stake_test1uru6c6uy7ttx3zrmx89z984hez7c6gx4glzf4dzxl8e52mctdgwz9")
//        .balance(BigInteger.valueOf(1332275528L))
//        .availableReward(BigInteger.ZERO)
//        .build();
//    stakeAddressRepository.save(stakeAddress);
//
//    txOut = TxOut.builder()
//        .tx(tx)
//        .index((short) 2)
//        .dataHash(null)
//        .tokenType(TokenType.NATIVE_TOKEN)
//        .addressHasScript(false)
//        .stakeAddress(stakeAddress)
//        .paymentCred("0a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274")
//        .address(
//            "addr_test1qq9q99av8jgqf5urqlyxqy63majnj22jms83aenxjnwjya8e434cfukkdzy8kvw2y20t0j9a35sd237yn26yd70ng4hsxwj6lg")
//        .addressRaw(HexUtil.decodeHexString(
//            "000a0297ac3c9004d38307c8601351df65392952dc0f1ee66694dd2274f9ac6b84f2d668887b31ca229eb7c8bd8d20d547c49ab446f9f3456f"))
//        .value(BigInteger.valueOf(1769474087L))
//        .build();
//    txOutRepository.save(txOut);
//
//    tx = Tx.builder()
//        .hash("a20898253a60bf3a60d97927a4155457a1ab9fadf3cb0d87a5e597e26aacabd9")
//        .block(block)
//        .blockIndex(2L)
//        .outSum(BigInteger.valueOf(1999754498L))
//        .fee(BigInteger.valueOf(245502L))
//        .validContract(true)
//        .deposit(0L)
//        .size(846)
//        .build();
//    txRepository.save(tx);
//
//    txOut = TxOut.builder()
//        .tx(tx)
//        .index((short) 0)
//        .dataHash(null)
//        .tokenType(TokenType.ALL_TOKEN_TYPE)
//        .addressHasScript(true)
//        .stakeAddress(null)
//        .paymentCred("e85ab8fc9feaa85089aeced37ee61a7c201cb2952d40725e85e829c8")
//        .address(
//            "addr_test1wr594w8unl42s5yf4m8dxlhxrf7zq89jj5k5quj7sh5znjq53crdt")
//        .addressRaw(
//            HexUtil.decodeHexString("70e85ab8fc9feaa85089aeced37ee61a7c201cb2952d40725e85e829c8"))
//        .value(BigInteger.valueOf(2000000L))
//        .build();
//    txOutRepository.save(txOut);
//
//    MultiAsset multiAsset = MultiAsset.builder()
//        .policy("b700cb0db84509fe6b35028a6c4a71c1ae11251dddf63cccf35f295d")
//        .name(HexUtil.encodeHexString("treasury".getBytes()))
//        .fingerprint("asset1959pjyfwey3s7h7wzgj5yygssr2ftwajylery9")
//        .supply(BigInteger.ONE)
//        .totalVolume(BigInteger.ONE)
//        .txCount(1L)
//        .time(block.getTime())
//        .build();
//    multiAssetRepository.save(multiAsset);
//
//    MaTxOut maTxOut = MaTxOut.builder()
//        .txOut(txOut)
//        .ident(multiAsset)
//        .quantity(BigInteger.ONE)
//        .build();
//    multiAssetTxOutRepository.save(maTxOut);
//
//    tx = Tx.builder()
//        .hash("99c2ef8e340d5991b1acfcb2a3bf06145ab139f0b10837fb8862cf0ec2324d03")
//        .block(block)
//        .blockIndex(3L)
//        .outSum(BigInteger.valueOf(97941331L))
//        .fee(BigInteger.valueOf(167965L))
//        .validContract(true)
//        .deposit(0L)
//        .build();
//    txRepository.save(tx);
//
//    txOut = TxOut.builder()
//        .tx(tx)
//        .index((short) 0)
//        .dataHash(null)
//        .tokenType(TokenType.NATIVE_TOKEN)
//        .addressHasScript(false)
//        .stakeAddress(null)
//        .paymentCred("09d12eeb563f8d9ccf83c7e9c6e20105ad2298e2ed6340b6686ee723")
//        .address(
//            "addr_test1vqyaztht2clcm8x0s0r7n3hzqyz66g5cutkkxs9kdphwwgczq2j89")
//        .addressRaw(
//            HexUtil.decodeHexString("6009d12eeb563f8d9ccf83c7e9c6e20105ad2298e2ed6340b6686ee723"))
//        .value(BigInteger.valueOf(10000000L))
//        .build();
//    txOutRepository.save(txOut);
//
//    tx = Tx.builder()
//        .hash("a12a839c25a01fa5d118167db5acdbd9e38172ae8f00e5ac0a4997ef792a2007")
//        .block(block)
//        .blockIndex(4L)
//        .outSum(BigInteger.valueOf(1000000L))
//        .fee(BigInteger.ZERO)
//        .validContract(true)
//        .deposit(0L)
//        .build();
//    txRepository.save(tx);
//
//    txOut = TxOut.builder()
//        .tx(tx)
//        .index((short) 0)
//        .dataHash(null)
//        .tokenType(TokenType.NATIVE_TOKEN)
//        .addressHasScript(false)
//        .stakeAddress(null)
//        .address("Ae2tdPwUPEZ3DdaWu8jn553npu6jwEPAJiahruj3xQjPXxgoxfYDWusJz7x")
//        .addressRaw(
//            HexUtil.decodeHexString(
//                "82d818582183581c4041adf6b03851a9c85db3f028995504fb4ba48b50703ab1b9841350a0021ad658e71f"))
//        .value(BigInteger.valueOf(1000000L))
//        .build();
//    txOutRepository.save(txOut);
//
//    // Fake pool hash
//    PoolHash poolHash = PoolHash.builder()
//        .hashRaw("8aa469088eaf5c38c3d4faf0d3516ca670cd6df5545fafea2f70258b")
//        .poolSize(BigInteger.ZERO)
//        .epochNo(28)
//        .view("pool132jxjzyw4awr3s75ltcdx5tv5ecv6m042306l630wqjckhfm32r")
//        .build();
//    poolHashRepository.save(poolHash);
//  }
//
//  @Test
//  void blockListenerIntegrationTest() throws InterruptedException {
//    commonBlocks.forEach(commonBlock -> {
//      log.info("Sending block number: {}", commonBlock.getBlockNumber());
//      kafkaProducer.send(testTopic, commonBlock);
//    });
//
//    testBlockListener.setCountdown(commonBlocks.size());
//    boolean consumed = testBlockListener.getLatch().await(60, TimeUnit.SECONDS);
//    Assertions.assertTrue(consumed);
//  }
}
