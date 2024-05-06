package org.cardanofoundation.ledgersync.verifier.data.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

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
