package capstone.rtou.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Getter
public class AuthResponse {

    private boolean status;
    private String message;

    public AuthResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
