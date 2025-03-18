package jung.api.login.service;

import jung.api.login.controller.LoginResponse;
import jung.api.login.controller.request.LoginRequest;
import jung.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) throws Exception {

        var loginId = request.getLoginId();
        var passWord = request.getPassword();
        Authentication authentication;

        try {
            // 로그인
            authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginId,
                    passWord
                )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("사용자 정보가 옳지 않습니다.");
        }

        // 인증된 사용자 정보 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        System.out.println("로그인 성공");
        return LoginResponse.builder()
            .accessToken(accessToken)
            .build();

    }
}
