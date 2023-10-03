package org.cardanofoundation.ledgersync.common.common;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Relay {

  private Integer port;
  private String ipv4;
  private String ipv6;
  private String dnsName;
  private RelayType relayType;
}
