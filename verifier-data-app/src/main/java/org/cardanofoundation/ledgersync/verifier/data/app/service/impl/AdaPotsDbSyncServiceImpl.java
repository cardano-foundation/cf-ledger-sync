package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.cardanofoundation.ledgersync.verifier.data.app.service.AdaPotsService;

@Service
public class AdaPotsDbSyncServiceImpl extends AdaPotsService {

  public AdaPotsDbSyncServiceImpl(
      @Qualifier("dbSyncJdbcTemplate") JdbcTemplate dbSyncJdbcTemplate) {
    super(dbSyncJdbcTemplate);
  }
}
