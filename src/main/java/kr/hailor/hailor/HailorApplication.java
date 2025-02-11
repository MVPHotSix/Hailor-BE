package kr.hailor.hailor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HailorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HailorApplication.class, args);
    }

}
