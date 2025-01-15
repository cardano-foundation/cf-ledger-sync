package org.cardanofoundation.ledgersync.govoffchainscheduler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.cardanofoundation.*")
@EntityScan("org.cardanofoundation.*")
public class GovOffChainSchedulerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GovOffChainSchedulerAppApplication.class, args);
	}

}
