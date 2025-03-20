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
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.accessToken.expired}")
    private long accessTokenExpired; // 인증 만료 시간

    @Value("${jwt.key}")
    private String secret_key;

    private static SecretKey secretKey;
    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret_key));
    }

    /**
     * 정보를 가지고 AccessToken 생성
     * Jwt Builder 가 헤더, 서명은 자동으로 생성함
     * payload 와 서명키만 지정해주면 된다.
     * signWith 메서드가 비밀키와 알고리즘을 사용하여 서명을 생성한다.
     */
    public String generateAccessToken(Authentication authentication){
        String username = authentication.getName();

        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return Jwts.builder()
            .setSubject(username) // 사용자 이름 : 토큰의 주체 (사용자 id 저장)
            .claim("roles",roles)   // role 저장  -> List<String> 으로 저장
            .setIssuedAt(new Date()) // 토큰 발급 시간
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간 유효 (현재 시간 + 토큰 유효 시간)
            .signWith(secretKey,SignatureAlgorithm.HS256) //토큰에 서명 추가, 토큰 진위 검증할때 지정
            .compact();  // 설정한 정보 바탕으로 JWT 생성 -> 문자열로 반환
    }

  /** JWT 토큰 검증 */
  public boolean validateToken(String jwtToken){
      try{
        Jwts.parserBuilder()    //jwt parser(검증기) create
            .setSigningKey(secretKey) //서명 검증 위해서 키 설정
            .build()
            .parseClaimsJws(jwtToken);  //jwt토큰 파싱하고 서명 검증
        return true;
      }catch (SecurityException | MalformedJwtException e){
          log.error("유효하지 않은 JWT : {}",e.getMessage());
      }catch (ExpiredJwtException e){
          log.error("만료된 JWT : {}",e.getMessage());
      }catch (UnsupportedJwtException e){
          log.error("지원되지 않는 JWT : {}",e.getMessage());
      } catch (IllegalArgumentException e){
          log.error("문법에 맞지 않은 JWT : {}",e.getMessage());
      }
      return false;
  }

  /**
   * Jwt 검증 후, Spring security 에서 인증 객체를 설정하는 역할 수행
   * 해당 메서드를 타야 jwt 인증이 정상적으로 동작함
   * @Param accesToken
   * @return
   */
  public void setAuthentication(String accessToken){
      // 1️⃣ JWT 검증 & 클레임(Claims) 추출
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();

      // 2️⃣ JWT에서 사용자 정보(Username, Role 등) 추출
      String username = claims.getSubject(); // 사용자 ID 또는 이메일
      List<String> roles = claims.get("roles", List.class);

      log.info("검증된 사용자 정보 ==>"+username);

      // 3️⃣ UserDetails 객체 생성 (Spring Security에서 사용)
      UserDetails userDetails = User.withUsername(username)
          .password("") // JWT 인증에서는 비밀번호 필요 없음
          .authorities(roles.toArray(new String[0])) // 권한(Role) 추가
          .build();

      // 4️⃣ Authentication 객체 생성 (인증 정보 설정)
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      // 5️⃣ SecurityContext에 저장하여 "로그인된 상태"로 인식
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }


}
