package toy.ojm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {


    @GetMapping("/")
    public String home() {
        return "recommend";
    }

    @GetMapping("/location")
    public String location() {
        return "location";
    }


    @GetMapping("/map0")
    public String map0() {
        return "map0";
    }

    @GetMapping("/map1")
    public String map2() {
        return "map1";
    }

    @GetMapping("/map3")
    public String map3() {
        return "map3";
    }

    @GetMapping("/map4")
    public String map4() {
        return "map4";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
