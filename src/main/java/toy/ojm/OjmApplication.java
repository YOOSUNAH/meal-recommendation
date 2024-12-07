package toy.ojm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@Slf4j
@EntityScan(basePackages = "toy.ojm.domain.entity")
public class OjmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjmApplication.class, args);
    }
}
