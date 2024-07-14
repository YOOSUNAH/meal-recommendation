package toy.ojm.domain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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

    @GetMapping("/boss")
    public String login() {
      return "login";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return  "adminPage";
    }


    @GetMapping("/allRestaurants")
    public String allRestaurants() {
        return  "allRestaurants";
    }

//    @RequestMapping("/error")
//    public String handleError() {
//        return "redirect:/home";
//    }
//
//    public String getErrorPath() {
//        return "/error";
//    }
}
