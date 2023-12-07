package toy.ojm.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        // XML 변환을 위한 MappingJackson2XmlHttpMessageConverter 추가
        restTemplate.setMessageConverters(getMessageConverters());

        return restTemplate;
    }
    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2XmlHttpMessageConverter());
        return converters;
    }
}
