package capstone.rtou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private boolean status;
    private String message;
    private String error;

    public AuthResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
