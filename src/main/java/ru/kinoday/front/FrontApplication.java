package ru.kinoday.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@EnableCaching
@EnableWebSecurity
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class FrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontApplication.class, args);
	}



	@Bean
	@Transactional
	CommandLineRunner run(
	) {
		return args -> {
		};
	}
}
