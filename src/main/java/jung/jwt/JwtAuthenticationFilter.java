package jung.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
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
        final String authHeader = request.getHeader("Authorization");

        if(ObjectUtils.isEmpty(authHeader)||!authHeader.startsWith(BEARER)){
            filterChain.doFilter(request,response);
            log.warn("토큰정보가 유효하지 않습니다.");
            return;
        }
        String token = authHeader.substring(7);

        if(jwtTokenProvider.validateToken(token)){
            jwtTokenProvider.setAuthentication(token);
        }

        filterChain.doFilter(request,response);
    }

}