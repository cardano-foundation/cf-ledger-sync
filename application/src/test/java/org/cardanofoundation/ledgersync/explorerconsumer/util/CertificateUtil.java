package org.cardanofoundation.ledgersync.explorerconsumer.util;

import java.util.Set;

import org.cardanofoundation.ledgersync.common.common.PoolParams;
import org.cardanofoundation.ledgersync.common.common.certs.PoolRegistration;
import org.cardanofoundation.ledgersync.common.common.certs.StakeCredential;
import org.cardanofoundation.ledgersync.common.common.certs.StakeCredentialType;
import org.cardanofoundation.ledgersync.common.common.certs.StakeDelegation;
import org.cardanofoundation.ledgersync.common.common.certs.StakeRegistration;

public final class CertificateUtil {

  private CertificateUtil() {}

  public static StakeRegistration buildStakeRegistrationCert(int certIdx,
                                                             StakeCredentialType type,
                                                             String hash) {
    StakeCredential credential = new StakeCredential(type, hash);
    StakeRegistration stakeRegistration = new StakeRegistration();
    stakeRegistration.setStakeCredential(credential);
    stakeRegistration.setIndex(certIdx);
    return stakeRegistration;
  }

  public static PoolRegistration buildPoolRegistrationCert(int certIdx,
                                                           String rewardAccount,
                                                           String... poolOwners) {
    PoolParams poolParams = PoolParams.builder()
        .rewardAccount(rewardAccount)
        .poolOwners(Set.of(poolOwners))
        .build();

    PoolRegistration poolRegistration = PoolRegistration.builder()
        .poolParams(poolParams)
        .build();
    poolRegistration.setIndex(certIdx);
    return poolRegistration;
  }

  public static StakeDelegation buildStakeDelegationCert(int certIdx,
                                                         StakeCredentialType type,
                                                         String hash) {
    StakeCredential credential = new StakeCredential(type, hash);
    StakeDelegation stakeDelegation = new StakeDelegation();
    stakeDelegation.setStakeCredential(credential);
    stakeDelegation.setIndex(certIdx);
    return stakeDelegation;
  }
}
