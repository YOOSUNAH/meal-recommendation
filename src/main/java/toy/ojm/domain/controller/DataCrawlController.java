package toy.ojm.domain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.infra.RestaurantCsvReader;
import toy.ojm.infra.RestaurantDataCrawler;

import java.nio.file.Path;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm/manual")
public class DataCrawlController {
    private final RestaurantDataCrawler restaurantDataCrawler;
    private final RestaurantCsvReader restaurantCsvReader;

    @GetMapping("/crawl")
    public ResponseEntity<Path> saveRestaurantByCrawling() {
        Path path = restaurantDataCrawler.downloadCsvFile();
        log.debug("##### 크롤링한 데이터 저장 완료 | 저장위치  : {}", path.toString());
        return ResponseEntity.ok(path);
    }

    @GetMapping("/csv")
    public void csvReadAndSave() {
        restaurantCsvReader.readAndSaveCSV();
        log.debug("##### csv 읽어오기 완료");
    }

    @GetMapping("/throw-error")
    public void throwError() {
        throw new RuntimeException("##### throw 에러!! slack 알림 연동 test");
    }
}
