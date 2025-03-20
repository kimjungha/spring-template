package jung.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("===JWT 필터 입장 ===");
        // 1. 요청 헤더에서 Authorization 값 가져옴
        final String authHeader = request.getHeader("Authorization");

        // 2. jwt 토큰이 없거나 잘못된 경우
        if(ObjectUtils.isEmpty(authHeader)||!authHeader.startsWith(BEARER)){
            filterChain.doFilter(request,response);
            log.debug("토큰정보가 유효하지 않습니다.");
            return;
        }
        //3. Bearer 이후 실제 jwt 토큰 값 추출
        String token = authHeader.substring(7);

        // 4. 토큰 검증
        if(jwtTokenProvider.validateToken(token)){
            log.info("===JWT 필터 있음 ==>"+token);
            // 유효하면 인증 정보 설정
            jwtTokenProvider.setAuthentication(token);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request,response);
    }

}
