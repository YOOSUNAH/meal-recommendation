package toy.ojm.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLocalRes {
    // 지역 검색 출력 결과를 변수화
    private String lastBuildDate; // 검색 결과를 생성한 시간
    private int total; // 검색 결과 문서의 총 개수
    private int start; // 검색 결과 문서 중, 문서의 시작점
    private int display; // 검색된 검색 결과의 개수
    private List<SearchLocalItem> items; // XML 포맷에서는 item 태그로, JSON 포맷에서는 items 속성

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchLocalItem {
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        private String roadAddress;
        private String mapx;
        private String mapy;
    }
}