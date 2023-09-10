package toy.ojm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.domain.RecommendService;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/recommend")
    public ResponseEntity<MealRecommendationResponse> recommend(
        @RequestBody MealRecommendationRequest request
    ) {
        return ResponseEntity.ok(
            recommendService.recommend(request)
        );
    }
}
