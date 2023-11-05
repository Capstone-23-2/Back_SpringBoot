package capstone.rtou.api.auth;

import capstone.rtou.api.auth.dto.AuthRequestDto;
import capstone.rtou.api.auth.dto.AuthResponse;
import capstone.rtou.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Transactional
    public AuthResponse saveUser(AuthRequestDto authRequestDto) {

        if (authRepository.existsById(authRequestDto.getUserId())) {
            log.info("Exists User={}", authRequestDto.getUserId());
            return new AuthResponse(false, "이미 존재하는 아이디입니다.");
        } else {
            User user = new User(authRequestDto.getUserId(), authRequestDto.getSso());
            authRepository.save(user);
            log.info("User Info={} 저장 완료", user);
            return new AuthResponse(true, "회원 정보가 저장되었습니다.");
        }
    }
}
