package toy.ojm.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ListRepository {

    HashMap<String, String> map = new HashMap<String, String>();

    {
        map.put("한식", "Korean");
        map.put("일식", "Japanese");
        map.put("중식", "Chinese");
        map.put("양식", "Western");
        map.put("모두선택하기", "All");
    }

    public String getCategory(String requestedCategory) {
        return map.getOrDefault(requestedCategory, null);
    }
}




