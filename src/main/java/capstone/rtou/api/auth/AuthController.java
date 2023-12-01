package capstone.rtou.api.auth;

import capstone.rtou.api.attention.dto.AttentionResponse;
import capstone.rtou.api.auth.dto.AuthRequestDto;
import capstone.rtou.api.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "회원 아이디 저장", description = "사용자가 출석 등록")
    @ApiResponse(responseCode = "200", description = "status가 true인 경우, 회원 정보 정상 등록, false인 경우, 존재하는 계정.", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Validated AuthRequestDto authRequestDto) throws IOException {
        log.info("회원 등록 시작");

        AuthResponse authResponse = authService.saveUser(authRequestDto);

        return ResponseEntity.ok().body(authResponse);
    }
}
