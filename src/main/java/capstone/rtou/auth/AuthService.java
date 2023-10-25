package capstone.rtou.auth;

import capstone.rtou.auth.dto.AuthRequestDto;
import capstone.rtou.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Transactional
    public void save(AuthRequestDto authRequestDto) throws IOException {
        User user = new User();

        user.setUserId(authRequestDto.getId());
        authRepository.save(user);

        log.info("User Info={} 저장 완료", user);
    }
}
