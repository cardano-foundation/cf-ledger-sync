package org.cardanofoundation.ledgersync.common.common;

import com.bloxbean.cardano.client.plutus.spec.PlutusScript;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cardanofoundation.ledgersync.common.common.kafka.serializer.PlutusScriptDeserializer;
import org.cardanofoundation.ledgersync.common.common.nativescript.NativeScript;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Witnesses {

  private List<VkeyWitness> vkeyWitnesses = new ArrayList<>();

  private List<NativeScript> nativeScripts = new ArrayList<>();

  private List<BootstrapWitness> bootstrapWitnesses = new ArrayList<>();

  //Alonzo
  @JsonDeserialize(contentUsing = PlutusScriptDeserializer.class)
  private List<PlutusScript> plutusV1Scripts = new ArrayList<>();

  private List<Datum> datums = new ArrayList<>();

  private List<Redeemer> redeemers = new ArrayList<>();

  @JsonDeserialize(contentUsing = PlutusScriptDeserializer.class)
  private List<PlutusScript> plutusV2Scripts = new ArrayList<>();
}
