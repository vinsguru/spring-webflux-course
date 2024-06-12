package com.vinsguru.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "com.vinsguru.playground.${sec}")
@EnableR2dbcRepositories(basePackages = "com.vinsguru.playground.${sec}")
public class WebFluxPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxPlaygroundApplication.class, args);
	}

}
