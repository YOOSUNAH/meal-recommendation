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
        private String title; //  조선옥
        private String link; // ??
        private String category; //  한식&gt;육류,고기요리
        private String description; //  연탄불 한우갈비 전문점.
        private String telephone; // ??
        private String address; //  서울특별시 중구 을지로3가 229-1
        private String roadAddress; //  서울특별시 중구 을지로15길 6-5
        private String mapx; //  311277
        private String mapy; //  552097
    }
}
