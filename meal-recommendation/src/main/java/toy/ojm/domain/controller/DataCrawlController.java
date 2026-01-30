package toy.ojm.domain.controller;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.csv.CsvCrawler;
import toy.ojm.csv.CsvProcessor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm/manual")
public class DataCrawlController {
  private final CsvCrawler csvCrawler;
  private final CsvProcessor csvProcessor;

  @GetMapping("/crawl")
  public ResponseEntity<Path> saveRestaurantByCrawling() {
    Path path = csvCrawler.downloadCsvFile();
    log.debug("##### 크롤링한 데이터 저장 완료 | 저장위치  : {}", path.toString());
    return ResponseEntity.ok(path);
  }

  @GetMapping("/csv")
  public void csvReadAndSave() {
    csvProcessor.readAndSaveCSV();
    log.debug("##### csv 읽어오기 완료");
  }

  @GetMapping("/throw-error")
  public void throwError() {
    throw new RuntimeException("##### throw 에러!! slack 알림 연동 test");
  }
}
