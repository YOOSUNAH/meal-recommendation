package toy.ojm.controller;


import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

// @Component
    // @Configuration
    // @Controller
    // @Service
    // @Repository
// @Bean

// Dependency Injection - 의존성 주입
// 'DI Container' - 의존성 주입을 도와주는 컨테이너
//

