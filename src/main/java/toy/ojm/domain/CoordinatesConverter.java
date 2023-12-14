package toy.ojm.domain;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
public class CoordinatesConverter {

    public Coordinates convertToTMCoordinates(double latitude, double longitude) {
        try {
            // WGS84 좌표계 (geolocation에서 받아온 좌표)
            CoordinateReferenceSystem geoCRS = CRS.decode("EPSG:4326");

            // TM(EPSG:2097) 좌표계 (서울강남구영업중인음식점.xlsx에서 사용하는 좌표)
            CoordinateReferenceSystem tmCRS = CRS.decode("EPSG:2097");

            DirectPosition2D geoPosition = new DirectPosition2D(longitude, latitude); // WGS84 좌표 (경도, 위도)
            DirectPosition2D tmPosition = new DirectPosition2D(); // 변환된 TM 좌표를 저장할 변수

            // 좌표 변환
            MathTransform transform = CRS.findMathTransform(geoCRS, tmCRS);
            transform.transform(geoPosition, tmPosition);

            // 변환된 TM 좌표를 Coordinates 객체로 반환
            return new Coordinates(tmPosition.getY(), tmPosition.getX()); // y는 TM 좌표의 위도, x는 TM 좌표의 경도
        } catch (FactoryException | TransformException e) {
            e.printStackTrace(); // 예외 처리 필요
            return null; // 실패 시 null 반환
        }
    }
}
