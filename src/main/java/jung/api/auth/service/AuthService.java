package jung.api.auth.service;

import jung.api.auth.UserEntity;
import jung.api.auth.controller.response.LoginResponse;
import jung.api.auth.controller.request.LoginRequest;
import jung.api.auth.controller.request.SignupRequest;
import jung.api.auth.repository.UserRepository;
import jung.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) throws Exception {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("사용자 아이디, 비밀번호가 다르거나 등록된 사용자가 없습니다.");
        } catch (Exception e) {
            throw new Exception("로그인 과정에서 알 수 없는 오류 발생: " + e.getMessage());
        }

        return LoginResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                .build();
    }
}