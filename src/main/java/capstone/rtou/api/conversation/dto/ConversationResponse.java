package capstone.rtou.api.conversation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class ConversationResponse {

    @Schema(description = "상태")
    boolean status;
    @Schema(description = "상태 메시지")
    String message;
    @Schema(description = "오디오 url. null 값인 경우에는 response에 없음.")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String url;

    public ConversationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ConversationResponse(boolean status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}
