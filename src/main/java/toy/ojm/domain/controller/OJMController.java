package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.global.ResponseDto;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.service.OJMService;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {

    private final OJMService OJMService;

    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    @PostMapping("/category")
    public ResponseEntity<ResponseDto<Void>> getRecommendation(
        @RequestBody CategoryRequestDto request,
        HttpSession session
    ){
        OJMService.recommend(request, session);
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/category")
    public FoodCategory getCategory(
        HttpSession session
    ) {
        FoodCategory selectedCategory = OJMService.getLastCategory();
        return selectedCategory;
    }


    @GetMapping("/restaurant")
    public List<Restaurant> getRestaurant(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam String category
    ) {

        return OJMService.searchRestaurant(latitude, longitude, category);
    }


}

//
//    @PostMapping("/lunch")
//    private ResponseEntity<String> searchLunch(HttpServletRequest request) {
//        String latitude = request.getParameter("latitude");
//        String longitude = request.getParameter("longitude");
//        String page = request.getParameter("page");
//
//        return OJMService.getSearchLunchList(latitude, longitude, page, "15");
//    }



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
////
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
//    @GetMapping("/recommend")
//    public String goRecommend() {
//        return "recommend";
//    }
//
//    @GetMapping("/result-page")
//    public String goResult(Model model, RecommendResponseDto recommendationResponse) {
//        if (recommendationResponse.getList() != null && !recommendationResponse.getList().isEmpty()) {
//            model.addAttribute("recommendedItems", recommendationResponse.getList()); // recommendItems라는 이름으로 list의 내용을 뷰로 전달 할 수 있게 된다.
//            // 즉, recommendationResponse 객체의 list를 recommendedItems라는 이름으로 뷰에 전달하여, 뷰에서 이 정보를 활용할 수 있도록 하는 역할.
//            return "result";
//        }
//        else {
//            return "noresult";
//        }
//    }
