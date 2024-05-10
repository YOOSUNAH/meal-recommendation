//package toy.ojm.domain;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.locationtech.proj4j.ProjCoordinate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@Slf4j
//@SpringBootTest
//class TransCoordinationTest {
//
//    @Autowired
//    TransCoordination transCoordination;
//
//    @Test
//    void transformToGCS() {
//        ProjCoordinate projCoordinate1 = transCoordination.transformToGCS(203882.77798934, 444136.167028607);
//        ProjCoordinate projCoordinate2 = transCoordination.transformToGCS(204228.237285272, 446332.692695117);
//        ProjCoordinate projCoordinate3 = transCoordination.transformToGCS(203379.505483989, 447099.455688672);
//
//        log.info("1 : {},{}", projCoordinate1.x, projCoordinate1.y);
//        log.info("2 : {},{}", projCoordinate2.x, projCoordinate2.y);
//        log.info("3 : {},{}", projCoordinate3.x, projCoordinate3.y);
//
//    }
//}
