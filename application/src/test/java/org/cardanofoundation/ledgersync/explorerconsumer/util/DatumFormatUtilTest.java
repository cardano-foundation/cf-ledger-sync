package org.cardanofoundation.ledgersync.explorerconsumer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DatumFormatUtilTest {

  @Test
  void datumJsonRemoveSpaceNotEmpty() {
    String inputJson = "{\"constructor\":  0,\"fields\":[{\" bytes\":\"b413bc466dadcb6bcf93e840a9eedabe04e83aa9d55f9deeb94d9743\"}"
        + ",{\"bytes\":\"ebc705b30531d5b34e524f94bf2f276b4593b437eba67338f9108608\"}]}";
    String expectedJson = "{\"constructor\":0,\"fields\":[{\"bytes\":\"b413bc466dadcb6bcf93e840a9eedabe04e83aa9d55f9deeb94d9743\"}"
        + ",{\"bytes\":\"ebc705b30531d5b34e524f94bf2f276b4593b437eba67338f9108608\"}]}";
    String result = DatumFormatUtil.datumJsonRemoveSpace(inputJson);
    Assertions.assertEquals(expectedJson, result);
  }

  @Test
  void datumJsonRemoveSpaceEmpty() {
    String inputJson = "";
    String result = DatumFormatUtil.datumJsonRemoveSpace(inputJson);
    Assertions.assertNull(result);
  }

  @Test
  void datumJsonRemoveSpaceNull() {
    String inputJson = null;
    String result = DatumFormatUtil.datumJsonRemoveSpace(inputJson);
    Assertions.assertNull(result);
  }
}