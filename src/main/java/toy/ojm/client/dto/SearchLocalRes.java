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

        private String DTLSTATENM; //상세영업상태명 (폐업, 영업)
        private String SITEWHLADDR;  //지번주소
        private String RDNWHLADDR;   //도로명 주소
        private String BPLCNM;  //사업장명
        private String UPTAENM;  //업태구분명
        private String X; //좌표정보(X) 경도
        private String Y; //좌표정보(Y) 위도

    }
}
