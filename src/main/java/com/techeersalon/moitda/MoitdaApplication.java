package com.techeersalon.moitda;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MoitdaApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory(".")
				.load();
		// 모든 환경 변수를 시스템 속성으로 설정
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		// 애플리케이션 종료 시 환경 변수를 삭제하는 후크 추가


		SpringApplication.run(MoitdaApplication.class, args);}

}
