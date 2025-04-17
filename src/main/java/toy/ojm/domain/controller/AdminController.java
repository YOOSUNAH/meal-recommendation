package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.dto.LoginRequestDto;
import toy.ojm.domain.dto.RestaurantPageableResponseDto;
import toy.ojm.domain.service.AdminService;
import toy.ojm.global.dto.ResponseDto;
import toy.ojm.global.util.SessionManager;

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
        createSession(request, requestDto.getId());
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/create-session")
    public void createSession(HttpServletRequest request, String id) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
        HttpSession session = request.getSession(true);
        // 세션에 저장될 정보 Name - Value 를 추가합니다.
        session.setAttribute(AUTHORIZATION_HEADER, "OJM Auth");
        session.setAttribute("Id", id); // userId 저장
        // 세션 만료시간 설정
        session.setMaxInactiveInterval(10800);  // 초 단위
    }

    @GetMapping("/get-session")
    public ResponseEntity<ResponseDto<String>> getSession(HttpServletRequest request) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 null 반환
        HttpSession session = request.getSession(false);

        String value = (String) session.getAttribute(
            AUTHORIZATION_HEADER); // 가져온 세션에 저장된 Value 를 Name 을 사용하여 가져옵니다.
        return ResponseDto.of(HttpStatus.OK, value);
    }

    @PostMapping("/encodePassword")
    public ResponseEntity<ResponseDto<String>> encodePassword(
        @RequestBody LoginRequestDto requestDto) {
        adminService.encodePassword(requestDto.getId(), requestDto.getPassword());
        String resultAlram = "password 암호화 성공";
        return ResponseDto.of(HttpStatus.OK, resultAlram);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<ResponseDto<RestaurantPageableResponseDto>> getAllRestaurants(
        HttpServletRequest request,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String keyword
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("Id") == null) {
            throw new IllegalArgumentException("로그인 필요");
        }
        String userId = (String) session.getAttribute("Id");

        RestaurantPageableResponseDto allRestaurants = adminService.getAllRestaurants(
            userId,
            page,
            size,
            category,
            keyword
        );
        return ResponseDto.of(HttpStatus.OK, allRestaurants);
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }
}
