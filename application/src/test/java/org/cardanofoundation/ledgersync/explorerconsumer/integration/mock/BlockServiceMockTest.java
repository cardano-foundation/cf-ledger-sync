package org.cardanofoundation.ledgersync.explorerconsumer.integration.mock;

import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


@Disabled
@Profile("test-integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BlockServiceMockTest {

  @Autowired
  private BlockRepository blockRepository;

  static Block genesisBlock;

  @BeforeAll
  static void setup() {
    genesisBlock = Block.builder()
        .blockNo(null)
        .epochNo(null)
        .hash("25de6b50531be2ead4d3ffff7b3f0318e227017e1991ac34fda8d3bc344241d9")
        .epochSlotNo(null)
        .previous(null)
        .opCert("8a1d44b3f2fbfe9fc854ed1b8140debed0276e30c19113b6bbaf1dd6649f42ac")
        .slotLeaderId(1L)
        .size(0)
        .protoMajor(0)
        .protoMinor(0)
        .time(Timestamp.valueOf(LocalDateTime.now()))
        .build();
  }

  @BeforeEach
  void setupGenesis(){
    blockRepository.save(genesisBlock);
  }

  @AfterEach
  void destroyGenesis(){
    blockRepository.deleteAll();
  }


  @Test
  void insertSuccessGenesis() {
    var genesis = blockRepository.findBlockByHash(genesisBlock.getHash());
   Assertions.assertTrue(genesis.isPresent());

     Block block = Block.builder()
        .blockNo(null)
        .epochNo(null)
        .hash("25de6b20531be2ead4d3ffff7b3f0318e227017e1991ac34fda8d3bc344241d1")
        .epochSlotNo(null)
        .previous(genesis.get())
        .opCert("8a1d44b3f2fbfe9fc854ed1b8140debed0276e30c19113b6bbaf1dd6649f42ac")
        .slotLeaderId(1L)
        .size(0)
        .protoMajor(0)
        .protoMinor(0)
        .time(Timestamp.valueOf(LocalDateTime.now()))
        .build();

    Assertions.assertNotNull(blockRepository.save(block));
  }

  @Test
  void insertSuccessFindGenesis() {
    Optional<Block> actual = blockRepository.findById(genesisBlock.getId());
    Assertions.assertTrue(actual.isPresent());
  }

}
