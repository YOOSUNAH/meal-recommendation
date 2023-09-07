package toy.ojm.domain;

import java.util.HashMap;

public class listRepository {

    HashMap<String, String> map = new HashMap<String, String>();

    {
        map.put("한식", "Korean");
        map.put("일식", "Japanese");
        map.put("중식", "Chinese");
        map.put("양식", "Western");
        map.put("모두선택하기", "All");

    }
}




