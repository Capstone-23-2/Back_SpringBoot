package capstone.rtou.api.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRequestDto {

    @NotBlank(message = "sso 토큰은 필수 입니다.")
    private String sso;
    @NotBlank(message = "사용자 아이디는 필수 입니다.")
    private String userId;
}
