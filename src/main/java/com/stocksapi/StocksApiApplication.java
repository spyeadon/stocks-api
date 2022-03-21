package com.stocksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StocksApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(StocksApiApplication.class, args);
	}
}
