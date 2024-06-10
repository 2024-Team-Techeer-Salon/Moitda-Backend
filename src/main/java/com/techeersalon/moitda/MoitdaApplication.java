package com.techeersalon.moitda;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MoitdaApplication {
	public static void main(String[] args) {

		// .env 파일이 존재하는지 확인
		if (isEnvFileExists()) {
			Dotenv dotenv = Dotenv.configure()
					.directory(".")
					.load();

			// 모든 환경 변수를 시스템 속성으로 설정
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		}

		// Spring Boot 애플리케이션 실행
		SpringApplication.run(MoitdaApplication.class, args);
	}

	private static boolean isEnvFileExists() {
		// .env 파일이 현재 디렉터리에 있는지 확인
		// 또는 파일 경로를 정확하게 지정할 수도 있습니다.
		File envFile = new File(".env");
		return envFile.exists();
	}

}
