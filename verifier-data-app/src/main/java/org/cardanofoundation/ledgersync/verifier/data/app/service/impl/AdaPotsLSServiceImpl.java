package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.cardanofoundation.ledgersync.verifier.data.app.service.AdaPotsService;

@Service
@Primary
public class AdaPotsLSServiceImpl extends AdaPotsService {

  public AdaPotsLSServiceImpl(
      @Qualifier("ledgerSyncJdbcTemplate") JdbcTemplate ledgerSyncJdbcTemplate) {
    super(ledgerSyncJdbcTemplate);
  }
}
