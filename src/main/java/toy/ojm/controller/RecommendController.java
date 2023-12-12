package toy.ojm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.domain.RecommendService;
import toy.ojm.excel.ExcelToDatabaseService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final ExcelToDatabaseService excelToDatabaseService;


    @PostMapping("/api/recommend")
    public String getRecommendation(
        @RequestBody MealRecommendationRequest request,
        Model model
    ) {
        // 추천된 음식점 목록을 서비스에서 가져옴
        MealRecommendationResponse recommendation = recommendService.recommend(request);
        log.info("요청 들어옴");

        // 모델에 음식점 목록을 추가하여 Thymeleaf 템플릿에 전달
        model.addAttribute("result", recommendation);

        return "result"; // result.html 템플릿을 렌더링
    }

    @PostMapping("/api/saveExcelToDatabase")
    public String saveExcelToDatabase() {
        try {
            excelToDatabaseService.readFromExcelAndSave("/Users/yuseon-a/Downloads/서울강남구영업중인음식점.xlsx");
            return "데이터 저장 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "데이터 저장 실패";
        }
    }
}

