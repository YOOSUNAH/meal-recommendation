package toy.ojm.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {


    @GetMapping("/")
    public String home() {
        return "recommend";
    }


    @GetMapping("/pageForm1")
    public String pageForm() {
        return "fragments/page-form-1";
    }

    @GetMapping("/pageForm2")
    public String pageForm2() {
        return "fragments/page-form-2";
    }
    @GetMapping("/pageForm3")
    public String pageForm3() {
        return "fragments/page-form-3";
    }


    @GetMapping("/page1")
    public String page() {
        return "page1";
    }

    @GetMapping("/page2")
    public String page2() {
        return "page2";
    }
    @GetMapping("/page3")
    public String page3() {
        return "page3";
    }


    @GetMapping("/location")
    public String location() {
        return "location";
    }

    @GetMapping("/map0")
    public String map0() {
        return "map/map0";
    }

    @GetMapping("/map1")
    public String map2() {
        return "map/map1";
    }

    @GetMapping("/map3")
    public String map3() {
        return "map/map3";
    }

    @GetMapping("/map4")
    public String map4() {
        return "map/map4";
    }

    @GetMapping("/locationSample")
    public String locationSample() {
        return "map/geolocationSample";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
