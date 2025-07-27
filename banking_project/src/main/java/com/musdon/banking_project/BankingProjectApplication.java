package com.musdon.banking_project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Java Academy Bank App",
				description = "Backend REST API for TJA Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Rajat",
						email = "rm8939072@gmail.com",
						url = "https://github.com/ByteBlitzed/tja_bank_app"
				)
		)
)
public class BankingProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingProjectApplication.class, args);
	}

}
