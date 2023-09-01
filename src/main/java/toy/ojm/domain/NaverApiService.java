package toy.ojm.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverApiService {

    @Value("gIwF8rSJfflcUcAcR8gh")
    private String clientId;

    @Value("_dGEL57Tf8")
    private String clientSecret;

    public String callNaverApi() {
        // 네이버 API 엔드포인트 URL 설정
        String apiUrl = "https://openapi.naver.com/v1/search/local.json";

        // API 호출을 위한 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);

        // API 호출 파라미터 설정 (예: query 파라미터 설정)
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("query", "검색어");

        // REST Template을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        // API 응답 데이터를 반환
        return response.getBody();
    }
}
