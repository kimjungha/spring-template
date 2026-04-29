package jung.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jung.api.auth.controller.request.LoginRequest;
import jung.api.auth.controller.response.LoginResponse;
import jung.api.auth.service.AuthService;
import jung.global.annotation.DuplicateAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @DuplicateAnnotation
    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "accessToken 은 body, refreshToken 은 cookie 에 넣어 응답한다. ")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) throws Exception {

       LoginResponse result =  authService.login(request);
       setRefreshTokenCookie(response,result.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {

        LoginResponse result = authService.refresh(refreshToken);
        setRefreshTokenCookie(response, result.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken);
        clearRefreshTokenCookie(response);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    /**
     * 헤더 쿠키에 refresh Token 셋팅
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * 로그아웃시 쿠키 다른 값으로 대체
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}