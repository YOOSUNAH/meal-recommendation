package toy.ojm.controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.global.ResponseDto;
import toy.ojm.controller.dto.CategoryRequestDto;
import toy.ojm.service.OJMService;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {

    private final OJMService OJMService;

//    private final JdbcTemplate jdbcTemplate;
//    private final TransCoordination transCoordination;

    @PostMapping("/recommend")
    public ResponseEntity<ResponseDto<Void>> getRecommendation(
        @RequestBody CategoryRequestDto request
    ){
        OJMService.recommend(request);
        return ResponseDto.of(HttpStatus.OK, null);
    }
//
//
//    public ResponseEntity<List<ContentDto>> myMapList(
//        @RequestParam final Double x, @RequestParam final Double y
//    ) {
//     List<ContentDto>  recommendation =  recommendService.listMyMap(x, y);
//        return ResponseEntity.ok().body(recommendation);
//
//    }
//
//
//
//    @GetMapping("/result")
//    public String showRecommendedRestaurants(Model model) {  // Model model: 뷰에 데이터를 전달하기 위한 Spring의 모델 객체
//        // 사용자의 위치 정보를 얻어온다
//        Coordinates currentLocation = getCurrentUserLocation();
//
//        // NearbyRestaurantService 클래스의 인스턴스 생성
//        NearbyRestaurantService nearbyRestaurantService = new NearbyRestaurantService(jdbcTemplate, transCoordination);
//
//        // 데이터베이스에서 100m 이내의 식당 정보를 가져온다.
//        List<Restaurant> recommendedRestaurants = nearbyRestaurantService.getNearbyRestaurants(currentLocation);
//
//        // recommendedRestaurants를 result.html 페이지에 전달한다.
//        model.addAttribute("recommendedRestaurants", recommendedRestaurants);  // 뷰로 식당 정보를 전달합니다.
//        return "result";
//    }
//    private Coordinates getCurrentUserLocation() {
//        // 예시: 임의의 위치 정보 생성
//        Double latitude = 37.5811955;
//        Double longitude = 127.0165415;
//        Coordinates currentLocation = new Coordinates(latitude, longitude);
//        return currentLocation;
//    }


//    @PostMapping("/saveExcelToDatabase")
//    public String saveExcelToDatabase(@RequestBody OJMRequestDto request) {
//        try {
//            excelToDatabaseService.readFromExcelAndSave("C:/my-files", request);
//            return "데이터 저장 성공";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "데이터 저장 실패";
//        }
//    }
}
