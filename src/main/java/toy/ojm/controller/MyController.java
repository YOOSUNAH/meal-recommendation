package toy.ojm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import toy.ojm.client.dto.SearchLocalRes;

@RestController
public class MyController {

    @GetMapping("/getDataFromApi")
    public SearchLocalRes getDataFromApi() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/6f524d4876746a73383056664d6843/xml/LOCALDATA_072404/1/5/";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SearchLocalRes> response = restTemplate.getForEntity(apiUrl, SearchLocalRes.class);

        // 가져온 응답 데이터를 Java 객체로 변환하여 반환
        return response.getBody();
    }
}
