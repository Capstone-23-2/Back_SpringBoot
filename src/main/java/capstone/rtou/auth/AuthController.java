package capstone.rtou.auth;

import capstone.rtou.auth.dto.AuthResponse;
import capstone.rtou.auth.dto.AuthRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Validated AuthRequestDto authRequestDto) throws IOException {
        log.info("회원 등록 시작");

        authService.save(authRequestDto);

        return ResponseEntity.ok().body(new AuthResponse(HttpStatus.OK.value(),"회원 정보가 등록되었습니다."));
    }
}
