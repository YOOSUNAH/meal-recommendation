package toy.ojm.domain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(("/refactor"))
public class RefactorPageController {

    @Value("${kakao.appkey}")
    private String kakaoAppKey;

    @GetMapping("/")
    public String homepage() {
        return "refactor/home";
    }

    @GetMapping("/home")
    public String home() {
        return "refactor/home";
    }

    @GetMapping("/location")
    public String location(Model model) {
        model.addAttribute("kakaoAppKey", "cf620856f46d903f5b7f04166e0f9ea3");
        return "refactor/location";
    }

    @GetMapping("/category")
    public String category() {
        return "refactor/category";
    }

    @GetMapping("/result")
    public String result() {
        return "refactor/result";
    }

    @GetMapping("/boss")
    public String adminHome() {
        return "refactor/login";
    }

    @GetMapping("/boss/login")
    public String login() {
        return "refactor/login";
    }

    @GetMapping("/boss/adminPage")
    public String adminPage() {
        return "refactor/adminPage";
    }


    @GetMapping("/boss/allRestaurants")
    public String allRestaurants() {
        return "refactor/allRestaurants";
    }
}
