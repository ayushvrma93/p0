package com.p0.calendarly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.p0.calendarly.repository")
public class Calendarly {

	public static void main(String[] args) {
		SpringApplication.run(Calendarly.class, args);
	}

}
