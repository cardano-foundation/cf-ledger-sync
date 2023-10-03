package org.cardanofoundation.ledgersync.common.common.address;

import static com.bloxbean.cardano.client.address.util.AddressEncoderDecoderUtil.getNetworkId;
import static com.bloxbean.cardano.client.address.util.AddressEncoderDecoderUtil.getPrefixTail;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.exception.AddressRuntimeException;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.Util;
import java.util.Arrays;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShelleyAddress extends Address {

  private ShelleyAddressType shelleyAddressType;
  private final byte[] paymentPart = new byte[28];
  private final byte[] delegationPart = new byte[28];
  private int headerPrefix;

  private String paymentPrefix = "";

  private String delegationPrefix = "";

  public ShelleyAddress(String prefix, byte[] bytes) {
    super(prefix, bytes);
    setShelley(bytes);
  }

  public static boolean checkHexHasStakeAddress(String hexData) {
    return checkDataHasStakeAddress(HexUtil.decodeHexString(hexData));
  }

  public static boolean checkBech32HasStakeAddress(String bech32Data) {
    return checkDataHasStakeAddress(Bech32.decode(bech32Data).data);
  }

  public static boolean checkDataHasStakeAddress(byte[] data) {
    if (data.length == 0) {
      return false;
    }
    switch ((data[0] & 0xF0) >> 4) {
      case 0b0000:
      case 0b0001:
      case 0b0010:
      case 0b0011:
      case 0b1110:
      case 0b1111:
        return true;
      default:
        return false;
    }
  }

  public static boolean checkHexHasPaymentCred(String data) {
    return checkDataHasPaymentCred(HexUtil.decodeHexString(data));
  }

  public static boolean checkBech32HasPaymentCred(String bech32Data) {
    return checkDataHasPaymentCred(Bech32.decode(bech32Data).data);
  }

  public static boolean checkDataHasPaymentCred(byte[] data) {
    if (data.length == 0) {
      return false;
    }
    switch ((data[0] & 0xF0) >> 4) {
      case 0b0000:
      case 0b0010://base
      case 0b0100: //pointer
      case 0b0110: //enterprise
        return true;
      default:
        return false;
    }
  }

  public boolean addressHasScript() {
    return shelleyAddressType == ShelleyAddressType.SCRIPT_AND_STAKE
        || shelleyAddressType == ShelleyAddressType.SCRIPT_AND_SCRIPT
        || shelleyAddressType == ShelleyAddressType.SCRIPT_AND_POINTER
        || shelleyAddressType == ShelleyAddressType.SCRIPT;
  }

  public boolean containStakeAddress() {
    return shelleyAddressType == ShelleyAddressType.PAYMENT_AND_STAKE
        || shelleyAddressType == ShelleyAddressType.PAYMENT_AND_SCRIPT
        || shelleyAddressType == ShelleyAddressType.SCRIPT_AND_STAKE
        || shelleyAddressType == ShelleyAddressType.SCRIPT_AND_SCRIPT
        || shelleyAddressType == ShelleyAddressType.STAKE_REWARD
        || shelleyAddressType == ShelleyAddressType.SCRIPT_REWARD;
  }

  public boolean containPaymentCred() {
    return shelleyAddressType == ShelleyAddressType.PAYMENT_AND_STAKE
        || shelleyAddressType == ShelleyAddressType.PAYMENT_AND_SCRIPT
        || shelleyAddressType == ShelleyAddressType.PAYMENT_AND_POINTER
        || shelleyAddressType == ShelleyAddressType.PAYMENT;
  }

  public ShelleyAddress(String address) {
    super(address);
    setShelley(getBytes());
  }

  public ShelleyAddress(byte[] addressBytes) {
    super(addressBytes);
    setShelley(addressBytes);
  }

  public String getHexPaymentPart() {
    if (Arrays.equals(paymentPart, new byte[28])) {
      return "";
    }
    return HexUtil.encodeHexString(paymentPart);
  }

  public String getHexDelegationPart() {
    if (Arrays.equals(delegationPart, new byte[28])) {
      return "";
    }
    return HexUtil.encodeHexString(delegationPart);
  }


  public String getBech32DelegationPart() {
    if (delegationPrefix.equals("")) {
      return "";
    }
    return Bech32.encode(
        Util.concat(getBech32Prefix(this.getNetwork()), delegationPart),
        delegationPrefix);
  }

  public byte[] getStakeReference() {
    // Converted from base address to stake address
    if (shelleyAddressType.equals(ShelleyAddressType.PAYMENT_AND_STAKE) ||
        shelleyAddressType.equals(ShelleyAddressType.SCRIPT_AND_STAKE) ||
        shelleyAddressType.equals(ShelleyAddressType.PAYMENT_AND_SCRIPT) ||
        shelleyAddressType.equals(ShelleyAddressType.SCRIPT_AND_SCRIPT)) {
      return Util.concat(getBech32Prefix(this.getNetwork()), delegationPart);
    }

    // Stake address
    if (shelleyAddressType.equals(ShelleyAddressType.STAKE_REWARD) ||
        shelleyAddressType.equals(ShelleyAddressType.SCRIPT_REWARD)) {
      return Util.concat(getBech32Prefix(this.getNetwork()), paymentPart);
    }

    return new byte[0];
  }

  public boolean hasScriptHashReference() {
    return shelleyAddressType.equals(ShelleyAddressType.PAYMENT_AND_SCRIPT)
        || shelleyAddressType.equals(ShelleyAddressType.SCRIPT_AND_SCRIPT)
        || shelleyAddressType.equals(ShelleyAddressType.SCRIPT_REWARD);
  }

  private byte[] getBech32Prefix(Network network) {
    int bech32Prefix = headerPrefix;
    bech32Prefix = bech32Prefix | network.getNetworkId();
    return new byte[]{(byte) bech32Prefix};
  }

  private void setShelley(byte[] bytes) {
    setShelleyAddressType();
    if (bytes.length < 57) {
      System.arraycopy(bytes, 1, paymentPart, 0, paymentPart.length);
    } else {
      System.arraycopy(bytes, 1, paymentPart, 0, paymentPart.length);
      System.arraycopy(bytes, 1 + paymentPart.length, delegationPart, 0, delegationPart.length);
    }
    delegationPrefix += getPrefixTail(getNetworkId(this.getNetwork()));
    paymentPrefix += getPrefixTail(getNetworkId(this.getNetwork()));
  }

  private void setShelleyAddressType() {
    var header = this.getBytes()[0];
    switch ((header & 0xF0) >> 4) {
      case 0b0000: //base
        this.shelleyAddressType = ShelleyAddressType.PAYMENT_AND_STAKE;
        this.paymentPrefix = "addr";
        this.delegationPrefix = "stake";
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b0001://base
        this.shelleyAddressType = ShelleyAddressType.SCRIPT_AND_STAKE;
        this.paymentPrefix = "stake";
        this.delegationPrefix = "stake";
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b0010://base
        this.shelleyAddressType = ShelleyAddressType.PAYMENT_AND_SCRIPT;
        this.paymentPrefix = "addr";
        this.delegationPrefix = "stake";
        this.headerPrefix = 0b1111_0000;
        return;
      case 0b0011://base
        this.shelleyAddressType = ShelleyAddressType.SCRIPT_AND_SCRIPT;
        this.paymentPrefix = "addr";
        this.delegationPrefix = "stake";
        this.headerPrefix = 0b1111_0000;
        return;
      case 0b0100: //pointer
        this.shelleyAddressType = ShelleyAddressType.PAYMENT_AND_POINTER;
        this.paymentPrefix = "stake";
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b0101://pointer
        this.shelleyAddressType = ShelleyAddressType.SCRIPT_AND_POINTER;
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b0110: //enterprise
        this.shelleyAddressType = ShelleyAddressType.PAYMENT;
        this.paymentPrefix = "stake";
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b0111://enterprise
        this.shelleyAddressType = ShelleyAddressType.SCRIPT;
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b1110: //reward
        this.shelleyAddressType = ShelleyAddressType.STAKE_REWARD;
        this.paymentPrefix = "stake";
        this.headerPrefix = 0b1110_0000;
        return;
      case 0b1111: //reward
        this.shelleyAddressType = ShelleyAddressType.SCRIPT_REWARD;
        this.paymentPrefix = "stake";
        this.headerPrefix = 0b1111_0000;
        return;
      default:
        throw new AddressRuntimeException(
            String.format("Unknown shelley address type %s", (header & 0xF0)));
    }
  }

}
