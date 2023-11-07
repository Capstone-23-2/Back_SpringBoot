package capstone.rtou.api.attention.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class AttentionResponse {

    @Schema(description = "상태", example = "true, false")
    boolean status;
    @Schema(description = "상태 메시지")
    String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "전체 출석 날짜", nullable = true, example = "{20231012, 20231014}")
    ArrayList<String> attendDate;

    public AttentionResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
