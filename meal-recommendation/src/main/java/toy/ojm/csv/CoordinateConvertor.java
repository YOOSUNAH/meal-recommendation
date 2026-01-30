package toy.ojm.csv;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

// 중부원점 TM(EPSG:2097)좌표 → WGS84(EPSG:4326) 좌표 로 변환
@Component
@Slf4j
public class CoordinateConvertor {
  private final BasicCoordinateTransform transformer;

  public CoordinateConvertor() {
    // CRS 객체 생성
    final CRSFactory factory = new CRSFactory();
    // 중부원점 좌표계 정의
    CoordinateReferenceSystem grs80 = factory.createFromName("EPSG:2097");
    // WGS84 좌표계 정의
    CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326");
    // 좌표 변환 시스템 정의
    transformer = new BasicCoordinateTransform(grs80, wgs84);
  }

  public ProjCoordinate transformToWGS(Double x, Double y) {
    // 변환할 좌표계 정보 생성
    ProjCoordinate beforeCoord = new ProjCoordinate(x, y);
    // 변환된 좌표를 담을 객체 생성
    ProjCoordinate afterCoord = new ProjCoordinate();
    // 좌표 변환 수행
    transformer.transform(beforeCoord, afterCoord);
    return afterCoord;
  }
}
