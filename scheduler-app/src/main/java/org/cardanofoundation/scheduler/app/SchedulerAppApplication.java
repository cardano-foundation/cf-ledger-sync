package org.cardanofoundation.scheduler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.cardanofoundation.*")
@EntityScan("org.cardanofoundation.*")
public class SchedulerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerAppApplication.class, args);
	}

}
