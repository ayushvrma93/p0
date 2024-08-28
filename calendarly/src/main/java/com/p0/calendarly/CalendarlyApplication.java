package com.p0.calendarly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CalendarlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarlyApplication.class, args);
	}

}
