package toy.ojm;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toy.ojm.client.NaverClient;
import toy.ojm.client.dto.SearchLocalRes;


@Slf4j
@SpringBootTest
class NaverClientTest {

    @Autowired
    NaverClient sut;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("네이버 지역 검색 API 호출 테스트")
    void search() {
        // given

        // when
        final SearchLocalRes result = sut.search("강남구 음식점");

        // then
        System.out.println("result : \n" + serialize(result));
    }

    private String serialize(SearchLocalRes result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}