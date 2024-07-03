package toy.ojm.domain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class PageController implements ErrorController {

    @Value("${kakao.appkey}")
    private String kakaoAppKey;

    @GetMapping("/")
    public String homepage() {
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/location")
    public String location(Model model) {
        model.addAttribute("kakaoAppKey", kakaoAppKey);
        return "location";
    }

    @GetMapping("/category")
    public String category() {
        return "category";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    @GetMapping("/test")
    public void test() {
        log.info("info");
    }

// 리다이렉션 방법 1
//    @RequestMapping("/error")
//    public String handleError() {
//        return "redirect:/home";
//    }
//
//    public String getErrorPath() {
//        return "/error";
//    }
}
