package toy.ojm.domain.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.dto.LoginRequestDto;
import toy.ojm.domain.service.AdminService;
import toy.ojm.global.ResponseDto;
import toy.ojm.global.SessionManager;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/admin")
public class AdminController {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final AdminService adminService;
    private final SessionManager sessionManager;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
        @RequestBody LoginRequestDto requestDto,
        HttpServletRequest request
    ) {
        // id, pw 일치하는지 확인
        adminService.login(requestDto.getId(), requestDto.getPassword());
        // session 만들기
        createSession(request);

        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/create-session")
    public void createSession(HttpServletRequest request) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
        HttpSession session = request.getSession(true);

        // 세션에 저장될 정보 Name - Value 를 추가합니다.
        session.setAttribute(AUTHORIZATION_HEADER, "OJM Auth");
    }

    @GetMapping("/get-session")
    public void getSession(HttpServletRequest req) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 null 반환
        HttpSession session = req.getSession(false);

        String value = (String) session.getAttribute(AUTHORIZATION_HEADER); // 가져온 세션에 저장된 Value 를 Name 을 사용하여 가져옵니다.
        log.info("value = " + value);
    }

    @GetMapping("/create-cookie")
    public String createCookie(HttpServletResponse response) {
        addCookie("Admin Auth", response);
        return "createCookie";
    }

    @GetMapping("get-cookie")
    public String getCookie(@CookieValue(AUTHORIZATION_HEADER) String value){
        return "get cookie + " + value;
    }

    public static void addCookie(String cookieValue, HttpServletResponse response) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8")
                .replaceAll("\\+", "%20");  // cookie value 에는 공백이 불가능해 encoding해줘야 한다.

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, cookieValue);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);

            response.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //  ===================================================
//    @PostMapping("login")
//    public String loginV2(
//        @RequestBody LoginRequestDto requestDto,
//        HttpServletResponse response
//    ){
//        sessionManager.createSession(requestDto.getId(), response);
//        return "redirect:/";
//    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        sessionManager.expire(request);  // 방법 1 , 방법 2는 아래 expiredCookie 이용하는 것
        return "redirect:/";
    }

    private void expiredCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
