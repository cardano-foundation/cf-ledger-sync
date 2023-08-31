package org.cardanofoundation.ledgersync.explorerconsumer.unit.hash;

import co.nstant.in.cbor.model.ByteString;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.plutus.spec.PlutusV1Script;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.factory.NativeScriptFactory;
import org.cardanofoundation.ledgersync.explorerconsumer.factory.PlutusScriptFactory;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.ScriptRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.ScriptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScriptTest {

  private ScriptServiceImpl scriptService;

  @Mock
  NativeScriptFactory nativeScriptFactory;

  @Mock
  PlutusScriptFactory plutusScriptFactory;

  @Mock
  ScriptRepository scriptRepository;

  @BeforeEach
  void init(){
    scriptService = new ScriptServiceImpl(nativeScriptFactory,plutusScriptFactory,scriptRepository);
  }

  @Test
  void test_getHashCbor() throws CborDeserializationException {
    String cborHex = "0x4D01000033222220051200120011";
    PlutusV1Script plutusV1Script = PlutusV1Script.deserialize(
        new ByteString(HexUtil.decodeHexString(cborHex)));
    String hash = "";
    try {
      hash = HexUtil.encodeHexString(plutusV1Script.getScriptHash());
    } catch (CborSerializationException e) {
      throw new RuntimeException(e);
    }
    assertEquals("67f33146617a5e61936081db3b2117cbf59bd2123748f58ac9678656", hash);
  }

  private byte[] get_plutusV1Bytes(String cborHex) {
    byte[] first = new byte[]{(byte) 1};
    byte[] data = HexUtil.decodeHexString(cborHex);
    return ByteBuffer.allocate(first.length + data.length)
        .put(first)
        .put(data)
        .array();
  }

  @Test
  void test_scriptHashByScriptRefer() {
    String cborHex = "82014e4d01000033222220051200120011";
    assertEquals("67f33146617a5e61936081db3b2117cbf59bd2123748f58ac9678656",
        scriptService.getHashOfReferenceScript(cborHex));
  }
}
