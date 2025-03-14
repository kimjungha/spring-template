package jung.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.accessToken.expired}")
    private long accessTokenExpired; // 인증 만료 시간

    @Value("${jwt.key}")
    private String key;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    /**
     * 정보를 가지고 AccessToken 생성
     */
    public String generateAccessToken(Authentication authentication){
        return Jwts.builder()
            .setSubject("username") // 사용자 이름 : 토큰의 주체 (사용자 id 저장)
//            .setClaim() // 추가 정보로 사용자의 권한 정보 저장하겠다.
            .setIssuedAt(new Date()) // 토큰 발급 시간
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간 유효 (현재 시간 + 토큰 유효 시간)
            .signWith(secretKey,SignatureAlgorithm.HS256) //토큰에 서명 추가, 토큰 진위 검증할때 지정 (비밀키와 알고리즘 사용해서 서명을 생성한다)
            .compact();  // 설정한 정보 바탕으로 JWT 생성 -> 문자열로 반환
    }
}
