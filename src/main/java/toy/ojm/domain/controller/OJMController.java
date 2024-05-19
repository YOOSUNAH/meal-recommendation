package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import toy.ojm.domain.service.ExcelReaderService;
import toy.ojm.domain.service.OJMService;
import toy.ojm.global.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {

    private final OJMService ojmService;
    private final ExcelReaderService excelReaderService;

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

    @PostMapping("/restaurant")
    public ResponseEntity<ResponseDto<Void>> uploadExcel() {
        try {
            excelReaderService.readExcelAndSaveTo();
            return ResponseDto.of(HttpStatus.OK, null);
        } catch (IOException e) {
            return ResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> saveRestaurant(Restaurant restaurant) {
        List<Restaurant> restaurants = excelReaderService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

}
