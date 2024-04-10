package org.cardanofoundation.ledgersync.aggregation.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.cardanofoundation.*")
@ComponentScan(basePackages = "org.cardanofoundation.*")
@EntityScan("org.cardanofoundation.*")
@EnableJpaRepositories("org.cardanofoundation.*")
public class AggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregationApplication.class, args);
    }

}
