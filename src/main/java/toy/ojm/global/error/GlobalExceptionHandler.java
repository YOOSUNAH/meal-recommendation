package toy.ojm.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException exception) {

        String requestURL = exception.getRequestURL();

        // 무한 루프를 방지
        if ("/home".equals(requestURL)) {
            return "home";
        }

        if (requestURL.startsWith("/home")) {
            return "redirect:/home";
        }

        if (requestURL.contains("login")) {
            log.debug("# exception 의 URL " + requestURL);
            return "login";

        } else if (requestURL.contains("/boss")) {
            return "login";

        } else if (requestURL.contains("/adminPage")) {
            return "adminPage";

        } else if (requestURL.contains("admin/get-session")) {
            return "forward:/v1/admin/get-session";

        } else if (requestURL.contains("admin/encodePassword")) {
            return "forward:/v1/admin/encodePassword";

        } else if (requestURL.contains("admin/restaurants")) {
            return "forward:/v1/admin/restaurants";

        } else if (requestURL.startsWith("/css") || requestURL.startsWith("/js")) {
            return "forward:" + requestURL;
        }
        return "redirect:/home";
    }
}
