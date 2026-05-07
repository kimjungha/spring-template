package jung.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jung.global.error.CommonErrorCode;
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
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/refresh");
    }

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

        try{
            jwtTokenProvider.validateTokenThrowException(token);
            jwtTokenProvider.setAuthentication(token);
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, CommonErrorCode.EXPIRED_ACCESS_TOKEN);
            return;
        } catch (SecurityException | JwtException e) {
            setErrorResponse(response, CommonErrorCode.INVALID_ACCESS_TOKEN);
            return;
        }
        filterChain.doFilter(request,response);
    }

    private void setErrorResponse(HttpServletResponse response, CommonErrorCode errorCode){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"error\": \"" + errorCode.getMessage() + "\"}");
        } catch (IOException e) {
            log.error("Error writing error response: {}", e.getMessage());
        }
    }

}