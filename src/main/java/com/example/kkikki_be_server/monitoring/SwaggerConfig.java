package com.example.kkikki_be_server.monitoring;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI kkikkiOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("KkiKki API")
						.description("KkiKki API 명세서")
						.version("v1")
						.contact(new Contact().name("Kkikki Team"))
						.license(new License().name("Internal Use")))
				.addServersItem(new Server().url("/"));
	}
}
