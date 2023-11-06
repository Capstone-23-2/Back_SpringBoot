package capstone.rtou.api.auth;

import capstone.rtou.api.auth.dto.AuthRequestDto;
import capstone.rtou.api.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "회원 등록 API", description = "사용자가 소셜 로그인 시 사용자 정보를 저장.")
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

        AuthResponse authResponse = authService.saveUser(authRequestDto);

        return ResponseEntity.ok().body(authResponse);
    }
}
