package jung.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        // 1. 요청 헤더에서 Authorization 값 가져옴
        final String authHeader = request.getHeader("Authorization");

        // 2. jwt 토큰 없는 경우 다음 필터로 넘김
        if(ObjectUtils.isEmpty(authHeader)||!authHeader.startsWith(BEARER)){
            filterChain.doFilter(request,response);
            return;
        }
        //3. Bearer 이후 실제 jwt 토큰 값 추출
        String token = authHeader.substring(7);

        // 4. 토큰 검증
        if(jwtTokenProvider.validateToken(token)){
            // 유효하면 인증 정보 설정
            jwtTokenProvider.setAuthentication(token);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request,response);
    }

}
