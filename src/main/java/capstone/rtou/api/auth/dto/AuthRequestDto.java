package capstone.rtou.api.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRequestDto {

    @Schema(name = "sso", description = "카카오, 네이버 등", defaultValue = "Kakao, Naver etc.")
    @NotBlank(message = "sso 토큰은 필수 입니다.")
    private String sso;
    @Schema(name = "userId", description = "사용자 아이디")
    @NotBlank(message = "사용자 아이디는 필수 입니다.")
    private String userId;
}
