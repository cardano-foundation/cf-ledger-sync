package org.cardanofoundation.ledgersync.explorerconsumer.unit.address;

import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
 class AddressTest {
  @Test
   void baseAddressShouldIncludeStakeAddress(){
    String[] baseAddresses = new String[]{
        "addr_test1qphhd9z2vsxasf090h2u9fyy82u6r5knyvfnvhs56setg6x4k0mpq9j2ke9hpzys3r648cslux2d34qh50ksh704atws8pm0zf",
        "addr_test1xzphfvnxwncw8038c40qlz59qrhrhszw0l9gdpzg7gq6wyvrwjexva8suwlz0327p79g2q8w80qyul72s6zy3usp5ugsvrdl8e"
    };

    for(String baseAddress : baseAddresses){
      assertTrue(ShelleyAddress.checkBech32HasStakeAddress(baseAddress));
    }
  }
}
