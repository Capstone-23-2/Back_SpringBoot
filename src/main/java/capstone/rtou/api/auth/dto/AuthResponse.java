package capstone.rtou.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Getter
public class AuthResponse {

    @Schema(description = "상태")
    private boolean status;
    @Schema(description = "상태 메시지")
    private String message;

    public AuthResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
