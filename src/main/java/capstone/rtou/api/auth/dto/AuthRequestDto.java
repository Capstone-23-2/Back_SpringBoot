package capstone.rtou.api.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRequestDto {

    @NotBlank(message = "SSO token cannot be null")
    @Schema(description = "SSO 토큰", example = "Kakao, Naver, Google .etc")
    private String sso;
    @NotBlank(message = "UserId cannot be null")
    @Schema(description = "사용자 아이디")
    private String userId;
}
