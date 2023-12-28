package toy.ojm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import toy.ojm.controller.dto.MealRecommendationResponse;


@Controller  // @Controller 어노테이션을 사용. 클래스를 Spring MVC의 컨트롤러로 선언하기 위해서
public class PageController { // PageController 클래스를 선언

    @GetMapping("/recommend")  // HTTP GET 메서드. "/recommend" 경로로 들어오면 getRecommend 메서드가 호출된다.
    public String goRecommend() { // 메서드
        return "recommend"; // recommend라는 문자열을 반환한다.
    }

    @GetMapping("/result-page") // HTTP GET 메서드. "/result-page" 경로로 들어오면 goResult 메서드가 호출된다.
    public String goResult(Model model, MealRecommendationResponse recommendationResponse) { // Model과 MealRecommendationResponse 객체를 매개변수로 받는 메서드
        if (recommendationResponse.getList() != null && !recommendationResponse.getList().isEmpty()) { // 결과가 있는 경우
            model.addAttribute("recommendedItems", recommendationResponse.getList()); //Spring MVC의 Model 객체에 속성(attribute)을 추가하는 작업
            // recommendedItems 이름을 통해 뷰에서 해당 속성을 참조할 수 있다.
            // recommendationResponse의 list필드 값을 가져와 recommendItems라는 속성으로 설정하고 있다.
            // recommendItems라는 이름으로 list의 내용을 뷰로 전달 할 수 있게 된다.
            // 즉, recommendationResponse 객체의 list를 recommendedItems라는 이름으로 뷰에 전달하여, 뷰에서 이 정보를 활용할 수 있도록 하는 역할.

            return "result"; // 문자열 "result"를 반환하고, 해당하는 view를 보여준다.
        }
        else { // 결과가 없는 경우
            return "noresult"; // 문자열 "noresult"를 반환하고, 해당하는 view를 보여준다.
        }
    }
}
