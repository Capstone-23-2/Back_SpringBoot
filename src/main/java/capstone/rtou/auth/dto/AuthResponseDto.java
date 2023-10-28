package capstone.rtou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private int code;
    private String message;
    @Nullable
    private String error;
}
