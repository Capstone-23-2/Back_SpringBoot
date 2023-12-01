package capstone.rtou.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
public class AuthResponse implements Serializable {

    @Schema(name = "status", description = "정상적으로 연결된 경우 true, 아닌 경우 false")
    boolean status;
    @Schema(name = "message", description = "회원가입이 정상적으로 이루어짐. 또는 오류가 발생한 이유")
    private String message;

    public AuthResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
