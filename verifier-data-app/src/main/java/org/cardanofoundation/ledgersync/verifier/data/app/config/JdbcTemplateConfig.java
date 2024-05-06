package org.cardanofoundation.ledgersync.verifier.data.app.config;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfig {
  @Bean(name = "ledgerSyncJdbcTemplate")
  public JdbcTemplate ledgerSyncJdbcTemplate(
      @Qualifier("ledgerSyncDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean(name = "dbSyncJdbcTemplate")
  public JdbcTemplate dbSyncJdbcTemplate(@Qualifier("dbSyncDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
