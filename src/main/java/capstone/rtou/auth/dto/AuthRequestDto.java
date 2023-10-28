package capstone.rtou.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// Registration Object
@Getter
public class AuthRequestDto {

    @NotBlank
    private String userId;
}
