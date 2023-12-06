package toy.ojm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import toy.ojm.client.dto.SearchLocalRes;

@Component
public class Client {

    private final OpenApiProperties openApiProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public Client(OpenApiProperties openApiProperties, RestTemplate restTemplate) {
        this.openApiProperties = openApiProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * OpenAPI를 통해 음식점 목록을 가져옴
     */
    public SearchLocalRes search(String query) {
        var uri = UriComponentsBuilder.fromUriString(openApiProperties.getUrl())
            .queryParam("query", query)
            .build()
            .encode()
            .toUri();

        var headers = headers(); // 헤더 설정

        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<SearchLocalRes>() {};

        var responseEntity = restTemplate.exchange(
            uri, HttpMethod.GET, httpEntity, responseType
        );

        return responseEntity.getBody();
    }

    private HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.set("X-Client-Id", openApiProperties.getClientId());
        headers.set("X-Client-Secret", openApiProperties.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
