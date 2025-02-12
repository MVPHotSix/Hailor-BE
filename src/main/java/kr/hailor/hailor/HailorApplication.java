package kr.hailor.hailor;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HailorApplication {

    public static void main(String[] args) {

        // 1) .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // .env 파일이 프로젝트 루트에 있는지 확인
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        // 2) .env의 모든 키-값을 시스템 속성에 등록
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        // 3) 스프링 부트 애플리케이션 실행
        SpringApplication.run(HailorApplication.class, args);
    }
}
