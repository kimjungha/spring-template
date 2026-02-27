package jung;

import jung.api.login.controller.LoginResponse;
import jung.api.login.controller.request.LoginRequest;
import jung.api.login.service.LoginService;
import jung.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private  JwtTokenProvider jwtTokenProvider;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        //given
        String loginId = "test";
        String password = "pw";
        String expectedToken = "mocked.jwt.token";
        LoginRequest request = LoginRequest.builder()
                .loginId(loginId)
                .password(password)
                .build();

        //stubbing
        given(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginId, password)))
                .willReturn(authentication);
        given(jwtTokenProvider.generateAccessToken(authentication))
                .willReturn(expectedToken);

        //when
        LoginResponse response = loginService.login(request);

        //then
        assertThat(response.getAccessToken()).isEqualTo(expectedToken);
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginId, password)
        );
    }

}
