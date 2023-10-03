package org.cardanofoundation.ledgersync.common.common.byron;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.cardanofoundation.ledgersync.common.common.byron.payload.ByronSscPayload;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = ByronSscPayload.TYPE)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ByronPkWitness.class, name = ByronPkWitness.TYPE),
    @JsonSubTypes.Type(value = ByronRedeemWitness.class, name = ByronRedeemWitness.TYPE),
    @JsonSubTypes.Type(value = ByronScriptWitness.class, name = ByronScriptWitness.TYPE),
    @JsonSubTypes.Type(value = ByronUnknownWitness.class, name = ByronUnknownWitness.TYPE)
})
public interface ByronTxWitnesses {

  String TYPE = "type";

  @JsonIgnore
  String getType();
}
