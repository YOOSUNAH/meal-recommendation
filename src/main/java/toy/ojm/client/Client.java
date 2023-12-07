package toy.ojm.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import toy.ojm.client.dto.SearchLocalRes;

import java.util.List;

@Component
public class Client {

    private final OpenApiProperties openApiProperties;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    @Autowired
    public Client(OpenApiProperties openApiProperties, RestTemplate restTemplate, XmlMapper xmlMapper) {
        this.openApiProperties = openApiProperties;
        this.restTemplate = restTemplate;
        this.xmlMapper = xmlMapper;
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

        try {
            ResponseEntity<SearchLocalRes> responseEntity = restTemplate.exchange(
                uri, HttpMethod.GET, httpEntity, SearchLocalRes.class
            );

            if (responseEntity != null && responseEntity.getBody() != null) {
                return responseEntity.getBody(); // OpenAPI 응답을 SearchLocalRes로 직렬화
            } else {
                // Response가 null인 경우 처리
                // 예외 처리 또는 로그 등의 작업 추가
                throw new RestClientException("REST API 응답이 null입니다.");
            }
        } catch (RestClientException ex) {
            // REST API 호출이 실패한 경우 처리
            // 여기서 적절한 예외 처리나 로깅 등을 수행합니다.
            System.err.println("REST API 호출이 실패했습니다: " + ex.getMessage());
            throw ex; // 예외를 다시 던져서 상위로 전달할 수도 있습니다.
        }
    }


    private HttpHeaders headers() {
        var headers = new HttpHeaders();
        headers.set("X-Client-Id", openApiProperties.getClientId());
        headers.set("X-Client-Secret", openApiProperties.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
