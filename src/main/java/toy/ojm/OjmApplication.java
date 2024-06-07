package toy.ojm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class OjmApplication {

    public static void main(String[] args) {

        log.info("시작 하기 전에 로그 남기기 ");
        SpringApplication.run(OjmApplication.class, args);

    }
}
