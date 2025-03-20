package jung.api.login.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jung.api.login.controller.request.LoginRequest;
import jung.api.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest request) throws Exception {
        log.info("로그인 컨트롤러 진입");
        return loginService.login(request);
    }

}
