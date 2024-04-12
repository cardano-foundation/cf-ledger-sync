package org.cardanofoundation.ledgersync.verifier.data.app.service;

import java.util.Set;

public interface AddressService {
    Set<String> getRandomAddresses(int maxNumOfAddresses);
}
