package org.cardanofoundation.ledgersync.common.common.kafka;

import com.bloxbean.cardano.client.util.JsonUtil;

public class MapKey {

  @Override
   public String toString(){
    return JsonUtil.getPrettyJson(this).replace("\n","");
  }

}
