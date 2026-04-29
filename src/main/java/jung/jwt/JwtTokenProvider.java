package jung.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
        @Value("${jwt.secret_key}") String secret_key,
        @Value("${jwt.expired}") long accessTokenExpirationMs,
        @Value("${jwt.refresh-expired}") long refreshTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret_key));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(Authentication authentication) {

        String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> a.startsWith("ROLE_"))
            .findFirst()
            .orElse("ROLE_USER");

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> !a.startsWith("ROLE_"))
            .collect(Collectors.joining(","));

        Date now = new Date();
        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("role", role)
            .claim("authorities", authorities)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + accessTokenExpirationMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(Authentication authentication) {

        Date now = new Date();

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("type", "refresh")
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenExpirationMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 JWT : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("문법에 맞지 않은 JWT : {}", e.getMessage());
        }
        return false;
    }

    public void validateTokenThrowException(String jwtToken) {
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken);
    }

    public Claims getClaimsIgnoreExpiry(String jwtToken) {
        try {
            return  Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }  catch (ExpiredJwtException e) {
           return e.getClaims(); // 로그아웃에서 사용할 예정이어 만료된 토큰도 Claims 반환
        }
    }

    public void setAuthentication(String accessToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        List<GrantedAuthority> authorities = getRoleAndAuthorities(claims);
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private List<GrantedAuthority> getRoleAndAuthorities(Claims claims){

        List<GrantedAuthority> result = new ArrayList<>();

        String role = claims.get("role", String.class);
        if (role != null) {
            result.add(new SimpleGrantedAuthority(role));
        }

        String raw = claims.get("authorities",String.class);
        List<String> authorities = (raw != null && !raw.isBlank())?
                Arrays.asList(raw.split(",")):
                List.of();

        if (!authorities.isEmpty()) {
            authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(result::add);
        }
        return result;
    }

    public Claims getClaims(String token){
       return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
