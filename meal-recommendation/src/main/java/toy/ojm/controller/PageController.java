package toy.ojm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Slf4j
@Controller
public class PageController {

    @GetMapping("/recommend")
    public String goRecommend() {
        return "recommend";
    }

    @GetMapping("/result")
    public String goResult() {
        return "result";
    }


}
