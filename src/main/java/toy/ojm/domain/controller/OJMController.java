package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.service.CsvToExcelService;
import toy.ojm.domain.service.ExcelReaderService;
import toy.ojm.domain.service.OJMService;
import toy.ojm.global.ResponseDto;
import toy.ojm.infrastructure.restaurant_openapi.PublicDataDownloader;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {

    private final OJMService ojmService;
    private final ExcelReaderService excelReaderService;
    private final PublicDataDownloader publicDataDownloader;
    private final CsvToExcelService csvToExcelService;

    @PostMapping("/category")
    public ResponseEntity<ResponseDto<Void>> getRecommendation(
        @RequestBody CategoryRequestDto request,
        HttpSession session
    ) {
        ojmService.recommend(request, session);
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/category")
    public FoodCategory getCategory(
        HttpSession session
    ) {
        return ojmService.getLastCategory();
    }

    // sample 엑셀 java로 불러와서 저장하기
    @PostMapping("/restaurant")
    public ResponseEntity<ResponseDto<Void>> uploadExcel() {
        try {
//            Path path = Path.of("src/main/resources/sample.xlsx");
            Path path = Path.of("out/production/resources/csv-data/data.csv");
            excelReaderService.readExcelAndSaveTo(path);
            csvToExcelService.changeCSVToExcel();

            return ResponseDto.of(HttpStatus.OK, null);
        } catch (IOException e) {
            return ResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transCoordinate")
    public void transCoordinate(
    ) {
        excelReaderService.transCoordinate();
        log.info("좌표 변환 요청 API 성공!");
    }

    // 크롤링한 데이터 java로 불러와서 , 저장하기
    @PostMapping("/restaurant/crawl")
    public ResponseEntity<Path> saveRestaurantByCrawling() throws Exception {
        Path path = publicDataDownloader.downloadCsvFile();
        log.info("크롤링한  데이터 java로 불러와서, 저장하기");
        // csv -> excel
        csvToExcelService.changeCSVToExcel();
        log.info("csv -> 엑셀로 변환하기");
//        // excel -> DB에 저장
//        excelReaderService.readExcelAndSaveTo(path);
        return ResponseEntity.ok(path);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> saveRestaurant() {
        List<Restaurant> restaurants = excelReaderService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
}
