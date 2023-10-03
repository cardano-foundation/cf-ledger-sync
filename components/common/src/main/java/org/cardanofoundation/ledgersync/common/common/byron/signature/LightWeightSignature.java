package org.cardanofoundation.ledgersync.common.common.byron.signature;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LightWeightSignature extends CommonSignature {
  private EpochRange epochRange;

  @Override
  public String getType() {
    return ByronSigType.LWDLG_SIGNATURE;
  }

  public static LightWeightSignature deserialize(DataItem dataItem){

    List<DataItem> dataItems = ((Array) dataItem).getDataItems();
    // extract signature
    String signature = HexUtil.encodeHexString(((ByteString) dataItems.get(1)).getBytes());
    // extract lwdlg
    List<DataItem> lwdlg = ((Array) dataItems.get(0)).getDataItems();
    List<DataItem> epochs = ((Array) lwdlg.get(0)).getDataItems();

    EpochRange epochRange = EpochRange.builder()
        .start(((UnsignedInteger) epochs.get(0)).getValue())
        .end(((UnsignedInteger) epochs.get(1)).getValue())
        .build();
    String issuer = HexUtil.encodeHexString(((ByteString) lwdlg.get(1)).getBytes());
    String delegate = HexUtil.encodeHexString(((ByteString) lwdlg.get(2)).getBytes());
    String certificate = HexUtil.encodeHexString(((ByteString) lwdlg.get(3)).getBytes());

    LightWeightSignature lightWeightSignature = new LightWeightSignature();

    lightWeightSignature.setSignature(signature);
    lightWeightSignature.setEpochRange(epochRange);
    lightWeightSignature.setIssuer(issuer);
    lightWeightSignature.setDelegate(delegate);
    lightWeightSignature.setCertificate(certificate);

    return lightWeightSignature;
  }
}
