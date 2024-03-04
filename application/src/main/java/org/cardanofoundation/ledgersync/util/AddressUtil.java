package org.cardanofoundation.ledgersync.util;

import com.bloxbean.cardano.client.address.AddressProvider;
import com.bloxbean.cardano.client.address.Credential;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;

public class AddressUtil {

    public static String getRewardAddressString(StakeCredential credential, int protocolMagic) {
        Network network = getNetwork(protocolMagic);
        if (credential.getType() == StakeCredType.ADDR_KEYHASH) {
            return HexUtil.encodeHexString(AddressProvider.getRewardAddress(Credential.fromKey(credential.getHash()), network).getBytes());
        } else if (credential.getType() == StakeCredType.SCRIPTHASH) {
            return HexUtil.encodeHexString(AddressProvider.getRewardAddress(Credential.fromScript(credential.getHash()), network).getBytes());
        } else {
            throw new IllegalArgumentException("Invalid credential type, should be either Key or Script. Stake Credential: "
                    + credential);
        }
    }

    public static Network getNetwork(int protocolMagic) {
        if (Networks.mainnet().getProtocolMagic() == protocolMagic) {
            return Networks.mainnet();
        } else
            return Networks.testnet();
    }

}
