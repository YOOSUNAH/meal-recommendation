package toy.ojm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.domain.RecommendService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/api/recommend")
    public String getRecommendation(
        MealRecommendationRequest request,
        Model model
    ) throws IOException {
        // 추천된 음식점 목록을 서비스에서 가져옴
        MealRecommendationResponse recommendation = recommendService.recommend(request);

        // 모델에 음식점 목록을 추가하여 Thymeleaf 템플릿에 전달
        model.addAttribute("result", recommendation);

        return "result"; // result.html 템플릿을 렌더링
    }
}
