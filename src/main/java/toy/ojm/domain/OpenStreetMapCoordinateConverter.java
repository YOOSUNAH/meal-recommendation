package toy.ojm.domain;

import org.proj4.PJ;
import org.proj4.PJFactory;
import org.proj4.PJValues;

public class CoordinateConverter {
    public static void main(String[] args) {
        // 중부원점(Bessel): 서울 등 중부지역 EPSG:2097
        String proj1Definition = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=1 +x_0=1000000 +y_0=2000000 +ellps=bessel +units=m +no_defs";

        // WGS84 경위도: GPS가 사용하는 좌표계 EPSG:4326
        String proj2Definition = "+proj=longlat +datum=WGS84 +no_defs";

        // 좌표 변환을 위한 좌표계 생성
        PJFactory factory = new PJFactory();
        PJ proj1 = factory.fromPROJ4Specification(proj1Definition);
        PJ proj2 = factory.fromPROJ4Specification(proj2Definition);

        // 예시 좌표
        double x = 200614.498;
        double y = 451667.793;

        // 좌표 변환
        PJValues values = new PJValues(1, x, y);
        proj1.transform(proj2, values);

        // 결과 출력
        System.out.println("변환된 좌표: " + values.getX() + ", " + values.getY());
    }
}
