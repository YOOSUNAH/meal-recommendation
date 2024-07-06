package toy.ojm.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException exception) {
        if (exception.getRequestURL().contains("/admin")){
            log.debug("# exception 의 URL " + exception.getRequestURL());
            return "adminLogin";
        }
        return "redirect:/home";
    }
}
