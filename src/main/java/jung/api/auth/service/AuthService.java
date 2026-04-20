package jung.api.auth.service;

import jung.api.auth.AuthLimiter;
import jung.api.auth.controller.response.LoginResponse;
import jung.api.auth.controller.request.LoginRequest;
import jung.global.exception.BusinessException;
import jung.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static jung.global.error.BusinessErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthLimiter authLimiter;


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) throws Exception {

        Authentication authentication;
        authLimiter.checkFailCount(request.getEmail());
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            authLimiter.recordFail(request.getEmail());
            throw new BusinessException(LOGIN_BAD_REQUEST);
        } catch (Exception e) {
            throw new BusinessException(LOGIN_FAILURE);
        }

        return LoginResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                .build();
    }
}