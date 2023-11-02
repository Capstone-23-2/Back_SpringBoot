package capstone.rtou.attention.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class AttentionResponse {

    boolean status;
    String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    ArrayList<String> attendDate;

    public AttentionResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
