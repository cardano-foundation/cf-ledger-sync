package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import org.cardanofoundation.ledgersync.verifier.data.app.service.AdaPotsService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdaPotsDbSyncServiceImpl extends AdaPotsService {

  public AdaPotsDbSyncServiceImpl(
      @Qualifier("dbSyncJdbcTemplate") JdbcTemplate dbSyncJdbcTemplate) {
    super(dbSyncJdbcTemplate);
  }
}
