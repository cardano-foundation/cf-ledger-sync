package org.cardanofoundation.ledgersync.common.util;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.Special;
import co.nstant.in.cbor.model.Tag;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import java.io.ByteArrayInputStream;
import java.util.List;

public class DecodePath {

  public String decodeTrace(DataItem dataItem) {
    StringBuilder tmp = new StringBuilder();
    switch (dataItem.getMajorType()) {
      case UNSIGNED_INTEGER:
        tmp = new StringBuilder(((UnsignedInteger) dataItem).getValue().toString());
        break;
      case BYTE_STRING:
        var tmpBytes = (ByteString) dataItem;

        try {
          ByteArrayInputStream bytes = new ByteArrayInputStream(tmpBytes.getBytes());
          List<DataItem> dataItems = new CborDecoder(bytes).decode();
          if (!dataItems.isEmpty()) {
            tmp = new StringBuilder(decodeTrace(dataItems.get(0)));
          } else {
            tmp = new StringBuilder("[||]");
          }
          break;
        } catch (CborException e) {
          e.printStackTrace();
          tmp = new StringBuilder("Raw data: " + HexUtil.encodeHexString(tmpBytes.getBytes()));
        }
        break;
      case ARRAY:
        var arr = (Array) dataItem;
        tmp = new StringBuilder(decodeTrace(arr.getDataItems()));
        break;
      case MAP:
        var map = (Map) dataItem;
        List<DataItem> keys = (List<DataItem>) map.getKeys();
        for (DataItem key : keys) {
          tmp.append("{").append(decodeTrace(key)).append(" : ").append(decodeTrace(map.get(key)))
              .append("}");
        }
        break;
      case TAG:
        var tag = (Tag) dataItem;
        tmp = new StringBuilder("Tag: " + tag);
        break;
      case SPECIAL:
        var special = (Special) dataItem;
        tmp = new StringBuilder("Special: " + special);
        break;
      case UNICODE_STRING:
        tmp = new StringBuilder(((UnicodeString) dataItem).getString());
        break;
      default:
        tmp = new StringBuilder("Unhandled data item major type [" + dataItem.getMajorType() + "]");

    }

    return tmp.toString();
  }

  public String decodeTrace(List<DataItem> dataItems) {
    var result = new StringBuilder();
    for (int i = 0; i < dataItems.size(); i++) {
      if (i == 0) {
        result.append("[");
      }
      result.append(decodeTrace(dataItems.get(i)));
      if (i != dataItems.size() - 1) {
        result.append(",");
      }
    }
    result.append("]");
    return result.toString();
  }

}
