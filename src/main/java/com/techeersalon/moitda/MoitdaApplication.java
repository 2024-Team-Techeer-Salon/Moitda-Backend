package com.techeersalon.moitda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MoitdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoitdaApplication.class, args);
	}

}
