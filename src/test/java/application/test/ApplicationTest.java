package application.test;

import application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Created by magomed on 30.03.17.
 */

@SpringBootApplication
@ConfigurationProperties("spring.datasource")
@Import(Application.class)
public class ApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
    }
}
