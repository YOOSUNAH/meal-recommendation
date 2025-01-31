package toy.ojm.domain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.global.ResponseDto;
import toy.ojm.infrastructure.csv_parser.CsvReaderService;
import toy.ojm.infrastructure.restaurant_openapi.PublicDataDownloader;

import java.nio.file.Path;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm/manual")
public class OJMManualTaskController {

    private final PublicDataDownloader publicDataDownloader;
    private final CsvReaderService csvReaderService;

    @GetMapping("/crawl")
    public ResponseEntity<Path> saveRestaurantByCrawling() {
        Path path = publicDataDownloader.downloadCsvFile();
        log.debug("##### 크롤링한 데이터 저장 완료 | 저장위치  : {}", path.toString());
        return ResponseEntity.ok(path);
    }

    @GetMapping("/csv")
    public ResponseEntity<ResponseDto<Void>> csvReadAndSave() {
        csvReaderService.readAndSaveCSV();
        log.info("##### csv 읽어오기 완료");
        return ResponseDto.of(HttpStatus.OK, null);
    }

}
