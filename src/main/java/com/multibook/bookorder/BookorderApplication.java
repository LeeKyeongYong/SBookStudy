package com.multibook.bookorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookorderApplication.class, args);
	}

}
