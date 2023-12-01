package capstone.rtou.api.attention.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class AttentionResponse implements Serializable {

    @Schema(name = "status", description = "정상적으로 연결된 경우 true, 아닌 경우 false")
    boolean status;
    @Schema(name = "message", description = "정상적으로 출석이 되거나 전체 출결 결과가 조회됨. 또는 오류가 발생한 이유")
    String message;
    @Schema(name = "attendDate", description = "전체 출석 결과가 전송됨", defaultValue = "{2023-11-22, 2023-11-27}")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    ArrayList<String> attendDate;

    public AttentionResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
