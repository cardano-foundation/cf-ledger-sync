package org.cardanofoundation.ledgersync.explorerconsumer.util;

import java.util.Set;

import com.bloxbean.cardano.yaci.core.model.PoolParams;
import com.bloxbean.cardano.yaci.core.model.certs.PoolRegistration;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeCredential;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDelegation;
import com.bloxbean.cardano.yaci.core.model.certs.StakePoolId;
import com.bloxbean.cardano.yaci.core.model.certs.StakeRegistration;

public final class CertificateUtil {

  private CertificateUtil() {}

  public static StakeRegistration buildStakeRegistrationCert(StakeCredType type, String hash) {
    StakeCredential credential = new StakeCredential(type, hash);

    return new StakeRegistration(credential);
  }

  public static PoolRegistration buildPoolRegistrationCert(String rewardAccount,
                                                           String... poolOwners) {
    PoolParams poolParams = PoolParams.builder()
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(poolOwners))
        .build();

    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();

    return poolRegistration;
  }

  public static StakeDelegation buildStakeDelegationCert(StakeCredType type,
                                                         String hash) {
    StakeCredential credential = new StakeCredential(type, hash);
    StakeDelegation stakeDelegation = new StakeDelegation(credential, new StakePoolId());

    return stakeDelegation;
  }
}
