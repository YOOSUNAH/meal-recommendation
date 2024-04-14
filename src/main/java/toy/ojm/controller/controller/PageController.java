//package toy.ojm.controller.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import toy.global.ResponseDto;
//
//
//@Controller
//public class PageController {
//
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
//}
