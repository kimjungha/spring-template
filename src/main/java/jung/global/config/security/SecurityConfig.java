package jung.global.config.security;

import jung.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] IGNORES = new String[] {
        "/healthcheck",
        "/login",
        "/signup",
        "/swagger-ui/**",
        "swagger-ui/index.html",
        "/swagger-resources/**",  //  Swagger 설정
        "/docs/**",
        "/products/**",
        "/webhook/**"   ,
        "/actuator/health",
        "/v3/api-docs/**",     // OpenAPI JSON 데이터 엔드포인트
        "/webjars/**",          // Swagger UI에서 사용되는 웹 리소스
        "/erp/read/book/**"     // 임시 test 용으로 인증제외
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable)      //base authentication 사용안함
            .formLogin(AbstractHttpConfigurer::disable)      //form login 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용 -> 프론트엔드 접근 허용
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션없이 사용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(IGNORES).permitAll()   // 로그인, 회원가입은 인증 없이 사용
                .anyRequest().authenticated())  // 그 외 모든 요청은 인증 필요
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   //JWT 필터 추가
            .build();
    }


    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:3002"
        ));

        configuration.setAllowedMethods(
            Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(
            Arrays.asList(
                "Authorization", "TOKEN_ID", "X-Requested-With", "Auth-Key",
                "Content-Type", "Content-Length", "Cache-Control", "Content-Disposition",
                "x-auth-token", "Access-Control-Allow-Origin", "client-ip",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Credentials",
                "x-xsrf-token","ODA-API-KEY","ODA-LOGIN-ID"
            )
        );
        configuration.setExposedHeaders(Arrays.asList(
            "Content-Disposition",
            "Auth-Key"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // password 비밀번호 암호화와 비교
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
