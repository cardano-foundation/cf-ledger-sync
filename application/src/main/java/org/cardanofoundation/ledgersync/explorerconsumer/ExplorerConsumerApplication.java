package org.cardanofoundation.ledgersync.explorerconsumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@ComponentScan(basePackages = "org.cardanofoundation.*")
@EntityScan("org.cardanofoundation.*")
@EnableJpaRepositories("org.cardanofoundation.*")
public class ExplorerConsumerApplication {

    static {
        System.setProperty("org.jooq.no-logo", "true");
        System.setProperty("org.jooq.no-tips", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(ExplorerConsumerApplication.class, args);
    }

}
