package toy.ojm.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Slf4j
@Controller
public class pageController {
    @GetMapping("/recommend")
    public String recommend() {
        return "1FirstPageForm";
    }

    @GetMapping("/checkCategory")
    public String checkCategory() {
        return "2SecondPageForm";
    }

    @GetMapping("/location")
    public String location() {
        return "3ThirdPageForm";
    }
    @GetMapping("/list")
    public String list() {
        return "4FourthPageForm";
    }
}
