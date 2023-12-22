package toy.ojm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import toy.ojm.controller.dto.MealRecommendationResponse;

//@Slf4j
@Controller
public class PageController {

    @GetMapping("/recommend")
    public String goRecommend() {
        return "recommend";
    }

    @GetMapping("/result-page")
    public String goResult(Model model, MealRecommendationResponse recommendationResponse) {
        if (recommendationResponse.getList() != null && !recommendationResponse.getList().isEmpty()) {
            // 결과가 있는 경우 - result.html 리턴
            model.addAttribute("recommendedItems", recommendationResponse.getList());
            return "result";
        } else {
            // 결과가 없는 경우 - noresult.html 리턴
            return "noresult";
        }
    }


}
