package toy.ojm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Spring Boot 애플리케이션의 시작점
public class OjmApplication {
    public static void main(String[] args) {  //Java 애플리케이션을 실행하기 위한 main 메서드를 정의
        SpringApplication.run(OjmApplication.class, args);  // SpringApplication 클래스의 run 메서드를 호출하여 Spring Boot 애플리케이션을 시작
        // OjmApplication 클래스를 시작 클래스로 사용하며, args 배열은 커맨드 라인에서 전달되는 인수들을 받는다.
    }
}