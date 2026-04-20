package jung.api.user;

import jung.api.auth.UserEntity;
import jung.api.auth.controller.request.SignupRequest;
import jung.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        String encodePw = passwordEncoder.encode(request.getPassword());
        userRepository.save(UserEntity.create(request,encodePw));
    }
}
