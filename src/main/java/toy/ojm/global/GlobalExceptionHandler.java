package toy.ojm.global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException exception) {
        String requestURL = exception.getRequestURL();

        if (requestURL.contains("location")
            || requestURL.contains("category")
            || requestURL.contains("result")) {
            return "redirect:/home";
        }
        return "redirect:/home";
    }
}
