package toy.ojm.domain;

import lombok.Getter;

@Getter
public class Coordinates {  // 좌표 클래스, 위도와 경도를 저장하기 위한 클래스

    private Double longitude; // 위도 변수
    private Double latitude; // 경도 변수


    public void setLongitude(Double lon) {  // 메서드
        this.longitude = lon;  // 전달받은 Lon을  클래스 내부의 longitude 변수에 저장
    }

    public void setLatitude(Double lat) {
        this.latitude = lat;   // 전달받은 lat을  클래스 내부의 longitude 변수에 저장
    }
}
