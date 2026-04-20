package jung.api.login.controller;

import jakarta.validation.Valid;
import jung.api.login.controller.request.LoginRequest;
import jung.api.login.controller.request.SignupRequest;
import jung.api.login.controller.response.LoginResponse;
import jung.api.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(loginService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request){
        loginService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
