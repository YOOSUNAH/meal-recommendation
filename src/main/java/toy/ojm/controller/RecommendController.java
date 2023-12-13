package toy.ojm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.domain.RecommendService;
import toy.ojm.excel.ExcelToDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final ExcelToDatabaseService excelToDatabaseService;

    @Value("${resources.file.path}") // application.properties 또는 application.yml에서 파일 경로 읽기
    private String filePath;



    @PostMapping("/api/recommend")
    public ResponseEntity<MealRecommendationResponse> getRecommendation(
        @RequestBody MealRecommendationRequest request
    ) {
        // 추천된 음식점 목록을 서비스에서 가져옴
        MealRecommendationResponse recommendation = recommendService.recommend(request);
        log.info("요청 들어옴");

//        모델에 음식점 목록을 추가하여 Thymeleaf 템플릿에 전달
//        model.addAttribute("result", recommendation);
//        return "result"; // result.html 템플릿을 렌더링

        // ResponseEntity로 JSON 형식으로 응답 반환
        return ResponseEntity.ok().body(recommendation);
    }


    @PostMapping("/api/saveExcelToDatabase")
    public String saveExcelToDatabase(@RequestBody MealRecommendationRequest request) {
        try {
            excelToDatabaseService.readFromExcelAndSave(filePath, request);
            return "데이터 저장 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "데이터 저장 실패";
        }
    }
}

