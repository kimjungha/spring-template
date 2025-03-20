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
            // ✅ 인증된 Authentication 객체 확인
            System.out.println("로그인 성공: " + authentication);
            System.out.println("인증된 사용자: " + authentication.getPrincipal());
            System.out.println("사용자 권한: " + authentication.getAuthorities());

        } catch (BadCredentialsException e) {
            throw new Exception("사용자 정보가 옳지 않습니다.");
        }catch (Exception e) {
            // ✅ 모든 예외를 잡아서 출력
            e.printStackTrace(); // 예외 메시지를 출력하여 어떤 문제인지 확인
            throw new Exception("로그인 과정에서 알 수 없는 오류 발생: " + e.getMessage());
        }

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        System.out.println("로그인 성고오 액세스 토큰 반환"+ accessToken);
        return LoginResponse.builder()
            .accessToken(accessToken)
            .build();
    }
}
