package toy.ojm.domain;

import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component  // Spring Framework에 해당 클래스가 Component임을 선언
public class ListRepository { // ListRepository 클래스 정의

    HashMap<String, String> map = new HashMap<String, String>();{   // HashMap 생성 (Key-Value 형태의 데이터 구조)
        // HashMap에 Key와 Value 쌍을 추가
        map.put("한식", "Korean");
        map.put("일식", "Japanese");
        map.put("중식", "Chinese");
        map.put("양식", "Western");
        map.put("모두선택하기", "All");
    }

    public String getCategory(String requestedCategory) { // getCategory 메서드 정의, requestedCategory를 전달값으로 받는 메서드  // 회색인데 지울까?
        // 요청된 카데고리(key)에 해당하는 값을 찾아 반환하는 메서드.
        return map.getOrDefault(requestedCategory, null);
        // getOrDefault메서드를 사용해서 HashMap에서 key에 대응하는 value를 가져오고, 만약 해당 key가 없으면 null을 반환한다.
    }
}




