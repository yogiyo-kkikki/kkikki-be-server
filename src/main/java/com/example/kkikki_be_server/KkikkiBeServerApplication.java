package com.example.kkikki_be_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KkikkiBeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkikkiBeServerApplication.class, args);
	}

}
