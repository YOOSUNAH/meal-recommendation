package toy.ojm.domain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
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


    @RequestMapping("/error")
    public String handleError() {
        return "home";
    }

    public String getErrorPath() {
        return "/error";
    }

}
