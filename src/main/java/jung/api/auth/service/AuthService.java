package jung.api.auth.service;

import io.jsonwebtoken.Claims;
import jung.api.auth.AuthLimiter;
import jung.api.auth.controller.response.LoginResponse;
import jung.api.auth.controller.request.LoginRequest;
import jung.global.error.CommonErrorCode;
import jung.global.exception.BusinessException;
import jung.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static jung.global.error.BusinessErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthLimiter authLimiter;
    private final StringRedisTemplate redisTemplate;
    private final UserDetailsService  userDetailsService;


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail();
        Authentication authentication;

        authLimiter.checkFailCount(email);
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            authLimiter.recordFail(email);
            throw new BusinessException(LOGIN_BAD_REQUEST);
        } catch (Exception e) {
            throw new BusinessException(LOGIN_FAILURE);
        }

        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        redisTemplate.opsForValue().set("RefreshToken::" +email, refreshToken, Duration.ofDays(7));

        return LoginResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponse refresh(String refreshToken) {

        // 1. refresh Token 서명 / 만료 검증
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new BusinessException(CommonErrorCode.INVALID_REFRESH_TOKEN);
        }
        // 2. Claims 추출
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String type = claims.get("type",String.class);

        // 3. isRefresh 확인
        if(!"refresh".equals(type)){
            throw new BusinessException(CommonErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 4. Claims 에서 email 추출
        String  email = claims.getSubject();

        // 5. Redis 에 조회
        String storedToken = redisTemplate.opsForValue().get("RefreshToken::"+email);

        if(!refreshToken.equals(storedToken)){
            throw new BusinessException(CommonErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 6. access Token 발급위해 유저 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // 7. 새로운 refresh Token 발급 and 저장
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        redisTemplate.opsForValue().set("RefreshToken::" +email, newRefreshToken, Duration.ofDays(7));

        return LoginResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                .refreshToken(newRefreshToken)
                .build();

    }
}
