package toy.ojm.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, String> sessionStore = new ConcurrentHashMap<>();

    public void expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie != null) {
            sessionStore.remove(cookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
            .filter(c -> c.getName().equals(cookieName))
            .findAny()
            .orElse(null);
    }
}
