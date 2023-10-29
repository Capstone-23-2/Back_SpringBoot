package capstone.rtou.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// Registration Object
@Getter
public class AuthRequestDto {

    @NotBlank(message = "사용자 아이디는 필수입니다.")
    private String userId;
}
