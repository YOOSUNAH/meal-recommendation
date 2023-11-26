package toy.ojm.controller.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import toy.ojm.domain.ListDto;

import java.io.IOException;
import java.util.List;

@Component
public class NaverClient {
    // yaml 파일 사용하는데 @Value 어노테이션을 사용하며
    // 내부에 "${}" 형태로 yaml에 설정한 대로 기입
    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;

    public List<ListDto> search(String query) throws IOException {
        // Naver API 호출을 위한 코드를 구현
        // query를 이용하여 Naver API를 호출하고 결과를 List<ListDto> 형태로 변환하여 반환

        // 예시 코드
        var uri = UriComponentsBuilder.fromUriString(naverLocalSearchUrl)
                .queryParam("query", query)
                .build()
                .encode()
                .toUri();
        var headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<List<ListDto>>() {};

        var responseEntity = new RestTemplate().exchange(
                uri, HttpMethod.GET, httpEntity, responseType
        );

        return responseEntity.getBody();
    }
}

